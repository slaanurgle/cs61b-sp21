package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author Slaanurgle
 */
public class Repository {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */
    /* all the file operation are done in this class */
    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File ADDED_DIR = join(GITLET_DIR, "added");
    public static final File REMOVED_DIR = join(GITLET_DIR, "removed");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File COMMITS_DIR = join(OBJECTS_DIR, "commits");
    public static final File BLOBS_DIR = join(OBJECTS_DIR, "blobs");
    public static final File BRANCHES_DIR = join(GITLET_DIR, "branches");
    public static final File HEAD = join(BRANCHES_DIR, "HEAD");

    /* TODO: fill in the rest of this class. */
    /** Remove the .gitlet folder */
    public static void clearRepo() {
        if (GITLET_DIR.exists()) {
            removeFolder(GITLET_DIR);
        }
    }
    /** Helper Method: remove the DIRECTORY */
    public static void removeFolder(File directory) {
        File[] files = directory.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                removeFolder(f);
            }
            f.delete();
        }
        directory.delete();
    }

    /** Remove the files under DIRECOTORY */
    public static void clearFiles(File directory) {
        File[] files = directory.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                f.delete();
            }
        }
    }


    /** Create a commit file and save a commit into a file */
    public static void saveCommit(Commit commit) {
        // create a folder named the first 6 digits of the commit id
        String id = commit.getID();
        File f = join(COMMITS_DIR, id.substring(0, 6));
        writeObject(f, commit);
    }
    /* Methods relevant to branches */
    /** Return NAME is a true branch in BRANCH_DIR */
    public static boolean isBranch(String name) {
        return join(BRANCHES_DIR, name).exists();
    }
    /** Set and save a branch as a file with given NAME and store the COMMIT id.
     *  NAME can not be HEAD. Use setHead instead.
     *  If the file does not exist it will create it.
     *  If already exists, overwrite. */
    public static void setBranch(String name, Commit commit) {
        File fIn = join(BRANCHES_DIR, name);
        String id = commit.getID();
        writeContents(fIn, id);
    }

    /** Set BRANCH as HEAD pointer */
    public static void setHead(String branch) {
        writeContents(HEAD, branch);
    }

    /** get the commit of the ID, ID can be abbreviated
     *  If the commit do not exists, return null. */
    public static Commit toCommit(String id) {
        File fIn = join(COMMITS_DIR, id.substring(0, 6));
        if (!fIn.exists()) {
            return null;
        }
        return readObject(fIn, Commit.class);
    }
    /** Get the branch that HEAD is pointing to */
    public static String getHead() {
        return readContentsAsString(HEAD);
    }
    /** Get the commit ID that the NAME branch pointing, HEAD is also ok */
    public static String getId(String name) {
        File fIn;
        if (name.equals("HEAD")) {
            fIn = join(BRANCHES_DIR, getHead());
        } else {
            fIn = join(BRANCHES_DIR, name);
        }
        return readContentsAsString(fIn);
    }

    /** get the commit of the BRANCH, HEAD is also ok.
     *  if the branch point to a non-exist commit, return null */
    public static Commit getCommit(String branch) {
        return toCommit(getId(branch));
    }

    /** return whether FILENAME is added */
    public static boolean isAdded(String filename) {
        return join(ADDED_DIR, filename).exists();
    }
    /** return whether FILENAME is removed */
    public static boolean isRemoved(String filename) {
        return join(REMOVED_DIR, filename).exists();
    }
    /** return whether a filename is Staged(Added or removed) */
    public static boolean isStaged(String filename) {
        return isAdded(filename) || isRemoved(filename);
    }

    /** Check if CWD has .gitlet */
    public static boolean hasGitlet() {
        return GITLET_DIR.exists();
    }
    /* Below are the command method */
    /** Initial the repo. Create all the directories needed.
     *  If .gitlet already exists, throws error.
     */
    public static void initRepo() {
        // check whether .gitlet is already exists.
        if (GITLET_DIR.exists()) {
            throw error("A Gitlet version-control system " +
                    "already exists in the current directory.\n");
        }
        // create the directories of the repo
        GITLET_DIR.mkdir();
        ADDED_DIR.mkdir();
        REMOVED_DIR.mkdir();
        OBJECTS_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BLOBS_DIR.mkdir();
        BRANCHES_DIR.mkdir();
        safetyCreate(HEAD);
        Commit.initCommit();
    }

    /** Add the file specified by the string to repo.
     *  If the file does not exist, throw an error. */
    public static void addFile(String filename) {
        File fIn = join(CWD, filename);
        if (!fIn.exists()) {
            throw error("File does not exist.");
        }
        File fAdd = join(ADDED_DIR, filename);
        File fRemove = join(REMOVED_DIR, filename);
        String contents = readContentsAsString(fIn);
        String fileId = sha1(filename + contents);
        // Check if HEAD has same version of the file.
        Commit head = getCommit("HEAD");
        if (head.blobs.get(filename) != null && head.blobs.get(filename).equals(fileId)) {
            fAdd.delete();
            fRemove.delete();
        } else {
            fRemove.delete();
            writeContents(fAdd, contents);
        }
    }

    /** Remove the file specified by FILENAME */
    public static void removeFile(String filename) {
        File fIn = join(CWD, filename);
        File fAdd = join(ADDED_DIR, filename);
        File fRemove = join(REMOVED_DIR, filename);
        // Check if HEAD has the file of same name.
        Commit head = getCommit("HEAD");
        if (head.blobs.get(filename) != null) {
            // The removed file is tracked.
            // Remove this file in CWD.
            restrictedDelete(fIn);
            // Then make sure there are only this file in removed area.
            fAdd.delete();
            safetyCreate(fRemove);
        } else {
            // The removed file is not tracked.
            // Check CWD, if the file does not exist, throw error.
            if (!fIn.exists()) {
                throw error("No reason to remove the file.");
            }
            fAdd.delete();
            fRemove.delete();
        }
    }
    /** Commit */
    public static void commit(String message) {
        Commit newCommit = new Commit(message);
        Commit head = getCommit("HEAD");
        newCommit.parents.addFirst(head);
        newCommit.blobs = new TreeMap<String, String>(head.blobs);
        // Check if there exists staged files
        if (isEmptyFolder(ADDED_DIR) && isEmptyFolder(REMOVED_DIR)) {
            throw error("No changes added to the commit.\n");
        }
        // add the staged files
        for (String addFilename : plainFilenamesIn(ADDED_DIR)) {
            // add the added file to the blobs
            String contents = readContentsAsString(join(ADDED_DIR, addFilename));
            String addFileId = sha1(addFilename + contents);
            newCommit.blobs.put(addFilename, addFileId);
            // If the blob does not exist, create a new blob.
            File newBlob = join(BLOBS_DIR, addFileId);
            if (!newBlob.exists()) {
                writeContents(newBlob, contents);
            }
        }
        // remove the removed files.
        for (String removeFilename : plainFilenamesIn(REMOVED_DIR)) {
            // remove the removed file from the blobs
            newCommit.blobs.remove(removeFilename);
        }
        // Clear the staged area.
        clearFiles(ADDED_DIR);
        clearFiles(REMOVED_DIR);
        // Save the newCommit.
        saveCommit(newCommit);
        // Move HEAD and the branch it is pointing
        String currBranch = getHead();
        setBranch(currBranch, newCommit);
    }

    public static void printLog() {
        Commit head = getCommit("HEAD");
        Commit p;
        for (p = head; !p.parents.isEmpty(); p = p.parents.get(0)) {
            p.printInfo();
        }
        p.printInfo();
    }

    public static void printGlobalLog() {
        for (String commitId : plainFilenamesIn(COMMITS_DIR)) {
            Commit commit = readObject(join(COMMITS_DIR, commitId), Commit.class);
            commit.printInfo();
        }
    }

    public static void findCommits(String message) {
        boolean commitFound = false;
        for (String commitId : plainFilenamesIn(COMMITS_DIR)) {
            Commit commit = readObject(join(COMMITS_DIR, commitId), Commit.class);
            if (commit.message.equals(message)) {
                commit.printInfo();
                commitFound = true;
            }
        }
        if (!commitFound) {
            throw error("Found no commit with that message.");
        }
    }

    public static void printStatus() {
        // Classify the files.
        ArrayList<String> added = new ArrayList<>(plainFilenamesIn(ADDED_DIR));
        ArrayList<String> removed = new ArrayList<>(plainFilenamesIn(REMOVED_DIR));
        ArrayList<String> modified = new ArrayList<>();
        ArrayList<String> untracked = new ArrayList<>();
        Commit head = getCommit("HEAD");
        TreeMap<String, String> headBlobs = head.blobs;
        // Get the files of CWD.
        HashMap<String, String> cwdFiles = getCWDFiles();
        // classify modified files.
        /* traverse the files in the working directory,
           If it is tracked in the current commit,
           changed in the working directory, but not staged.
         */
        for (Map.Entry<String, String> entry : cwdFiles.entrySet()) {
            String filename = entry.getKey();
            String fileId = entry.getValue();
            if (headBlobs.containsKey(filename) && !headBlobs.get(filename).equals(fileId)) {
                if (!isStaged(filename)) {
                    modified.add(filename + " (modified)");
                }
            }
        }
        /* Staged for addition, but with different contents than in the working directory.
           Staged for addition, but deleted in the working directory.
         */
        for (String filename : plainFilenamesIn(ADDED_DIR)) {
            String contents = readContentsAsString(join(ADDED_DIR, filename));
            String fileId = sha1(filename + contents);
            if (join(CWD, filename).exists()) {
                if (!cwdFiles.containsValue(fileId)) {
                    added.remove(filename);
                    modified.add(filename + " (modified)");
                }
            } else {
                added.remove(filename);
                modified.add(filename + " (deleted)");
            }
        }
        /* Not staged for removal, but tracked in the current commit
           and deleted from the working directory.
         */
        for (Map.Entry<String, String> entry : head.blobs.entrySet()) {
            String filename = entry.getKey();
            String fileId = entry.getValue();
            // HEAD has the files but CWD does not have
            if (!isStaged(filename) && !join(CWD, filename).exists()) {
                modified.add(filename + " (deleted)");
            }
        }
        // Classify untracked files.
        // Files present in the working directory but neither staged for addition nor tracked.
        for (Map.Entry<String, String> entry : cwdFiles.entrySet()) {
            String filename = entry.getKey();
            String fileId = entry.getValue();
            if (!isStaged(filename) && !headBlobs.containsKey(filename)) {
                untracked.add(filename);
            }
        }
        // Files that have been staged for removal, but then re-created without Gitlet’s knowledge
        for (String filename : plainFilenamesIn(REMOVED_DIR)) {
            if (join(CWD, filename).exists()) {
                removed.remove(filename);
                untracked.add(filename);
            }
        }
        Collections.sort(added);
        Collections.sort(removed);
        Collections.sort(modified);
        Collections.sort(untracked);
        // Print branches.
        System.out.println("=== Branches ===");
        String headBranch = getHead();
        for (String branch : plainFilenamesIn(BRANCHES_DIR)) {
            if (branch.equals("HEAD")) {
                continue;
            }
            if (branch.equals(headBranch)) {
                System.out.print("*");
            }
            System.out.println(branch);
        }
        System.out.println();
        // Print staged files.
        System.out.println("=== Staged Files ===");
        for (String s : added) {
            System.out.println(s);
        }
        System.out.println();
        // Print removed files.
        System.out.println("=== Removed Files ===");
        for (String s : removed) {
            System.out.println(s);
        }
        System.out.println();
        // Print modifications not staged for commit.
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String s : modified) {
            System.out.println(s);
        }
        System.out.println();
        // print untracked files.
        System.out.println("=== Untracked Files ===");
        for (String s : untracked) {
            System.out.println(s);
        }
        System.out.println();
    }
    /** Get CWD files */
    private static HashMap<String, String> getCWDFiles() {
        HashMap<String, String> cwdFiles = new HashMap<>();
        for (String cwdFilename : plainFilenamesIn(CWD)) {
            File cwdFile = join(CWD, cwdFilename);
            if (cwdFile.isFile()) {
                String cwdFileId = sha1(cwdFilename + readContentsAsString(cwdFile));
                cwdFiles.put(cwdFilename, cwdFileId);
            }
        }
        return cwdFiles;
    }
    /** Get untracked files */
    private static ArrayList<String> getUntrackedFiles() {
        ArrayList<String> untracked = new ArrayList<>();
        HashMap<String, String> cwdFiles = getCWDFiles();
        Commit head = getCommit("HEAD");
        TreeMap<String, String> headBlobs = head.blobs;
        // Classify untracked files.
        // Files present in the working directory but neither staged for addition nor tracked.
        for (Map.Entry<String, String> entry : cwdFiles.entrySet()) {
            String filename = entry.getKey();
            String fileId = entry.getValue();
            if (!isStaged(filename) && !headBlobs.containsKey(filename)) {
                untracked.add(filename);
            }
        }
        // Files that have been staged for removal, but then re-created without Gitlet’s knowledge
        for (String filename : plainFilenamesIn(REMOVED_DIR)) {
            if (join(CWD, filename).exists()) {
                untracked.add(filename);
            }
        }
        return untracked;
    }
    /** Return whether a file is an untracked file */
    public static boolean isTracked(String filename) {
        return !getUntrackedFiles().contains(filename);
    }
    /** Checkout a FILENAME to head version */
    public static void checkoutFile(String filename) {
        checkoutFile(getId("HEAD"), filename);
    }
    /** Checkout FILENAME to COMMITID version */
    public static void checkoutFile(String commitId, String filename) {
        // Move the file of the commit id to CWD.
        Commit commit = toCommit(commitId.substring(0, 6));
        if (commit == null) {
            throw error("No commit with that id exists.");
        }
        String blobid = commit.blobs.get(filename);
        if (blobid == null) {
            throw error("File does not exist in that commit.");
        }
        String contents = readContentsAsString(join(BLOBS_DIR, blobid));
        writeContents(join(CWD, filename), contents);
    }

    /** Checkout to BRANCH */
    public static void checkoutBranch(String branch) {
        if (!isBranch(branch)) {
            throw error("No such branch exists.");
        }
        if (branch.equals(getHead())) {
            throw error("No need to checkout the current branch.");
        }
        String commitId = getId(branch);
        checkoutCommit(commitId);
        // move HEAD
        setHead(branch);
    }
    /** Checkout to COMMITID's commit, helper method */
    private static void checkoutCommit(String commitId) {
        Commit head = getCommit("HEAD");
        Commit commit = toCommit(commitId);
        // Get the files of CWD.
        HashMap<String, String> cwdFiles = new HashMap<>();
        for (String cwdFilename : plainFilenamesIn(CWD)) {
            File cwdFile = join(CWD, cwdFilename);
            if (cwdFile.isFile()) {
                String cwdFileId = sha1(cwdFilename + readContentsAsString(cwdFile));
                cwdFiles.put(cwdFilename, cwdFileId);
            }
        }
        // Check whether there are untracked file and would be overwritten.
        ArrayList<String> untrackedFiles = getUntrackedFiles();
        for (String untracked : untrackedFiles) {
            if (commit.blobs.containsKey(untracked)) {
                throw error("There is an untracked file in the way; delete it, " +
                        "or add and commit it first.");
            }
        }
        /* Delete all files in CWD except untracked files,
           then write the blobs of the commit to CWD
         */
        for (String f : plainFilenamesIn(CWD)) {
            if (join(CWD, f).isFile() && !untrackedFiles.contains(f)) {
                restrictedDelete(join(CWD, f));
            }
        }
        // Write the blobs of the commit to CWD
        for (Map.Entry<String, String> entry : commit.blobs.entrySet()) {
            String filename = entry.getKey();
            String fileId = entry.getValue();
            String contents = readContentsAsString(join(BLOBS_DIR, fileId));
            writeContents(join(CWD, filename), contents);
        }
        // Clear the staged area
        for (File f : ADDED_DIR.listFiles()) {
            f.delete();
        }
        for (File f : REMOVED_DIR.listFiles()) {
            f.delete();
        }
    }
    /** Create NEWBRANCH */
    public static void createBranch(String newBranch) {
        File fBranch = join(BRANCHES_DIR, newBranch);
        if (fBranch.exists()) {
            throw error("A branch with that name already exists.");
        }
        String commitId = getId("HEAD");
        writeContents(fBranch, commitId);
        setHead(newBranch);
    }

    /** Remove branch */
    public static void removeBranch(String branch) {
        File fBranch = join(BRANCHES_DIR, branch);
        if (!fBranch.exists()) {
            throw error("A branch with that name does not exist.");
        }
        String head = getHead();
        if (head.equals(branch) && branch.equals("HEAD")) {
            throw error("Cannot remove the current branch.");
        }
        fBranch.delete();
    }

    /** Reset to COMMITID */
    public static void reset(String commitId) {
        Commit commit = toCommit(commitId);
        if (commit == null) {
            throw error("No commit with that id exists.");
        }
        checkoutCommit(commitId);
    }

    /** Merge HEAD with BRANCH */
    public static void merge(String branch) {
        if (ADDED_DIR.listFiles().length != 0 || REMOVED_DIR.listFiles().length != 0) {
            throw error("You have uncommitted changes.");
        }
        if (!join(BRANCHES_DIR, branch).exists()) {
            throw error("A branch with that name does not exist.");
        }
        if (branch.equals("HEAD") || branch.equals(getHead())) {
            throw error("Cannot merge a branch with itself.");
        }
        Commit currCommit = getCommit("HEAD");
        Commit objCommit = getCommit(branch);
        TreeMap<String, String> newBlobs = new TreeMap<>(currCommit.blobs); // store the blobs of merged commit
        TreeMap<String, String> objBlobs = objCommit.blobs; // the blobs of objCommit.
        ArrayList<String> untrackedFiles = getUntrackedFiles();
        for (String untracked : untrackedFiles) {
            if (objCommit.blobs.containsKey(untracked)) {
                throw error("There is an untracked file in the way; " +
                        "delete it, or add and commit it first.");
            }
        }
        Commit splitPoint = findSplitPoint(currCommit, objCommit);
        // If currCommit is splitPoint, then fast-forward.
        if (splitPoint.equals(currCommit)) {
            checkoutBranch(branch);
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        if (splitPoint.equals(objCommit)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        String message = "Merged " + branch + " into " + getHead() + ".";
        Commit newCommit = new Commit(message);
        TreeMap<String, String> splitPointBlobs = splitPoint.blobs;
        for (Map.Entry<String, String> entry : newBlobs.entrySet()) {
            String filename = entry.getKey();
            String fileId = entry.getValue();
            if (objBlobs.get(filename) == null) {
                if (splitPointBlobs.get(filename) != null) {
                    if (splitPointBlobs.get(filename).equals(fileId)) {
                        newBlobs.remove(filename);
                    } else {
                        // This file is present in split point but different version to curr commit.
                        System.out.println("Encountered a merge conflict.");
                        // Create conflict file
                        File conflictFile = createConflictFile(filename, fileId, null);
                        newBlobs.put(filename, sha1(filename + readContentsAsString(conflictFile)));
                    }
                }

            }
        }
        for (Map.Entry<String, String> entry : objBlobs.entrySet()) {
            String filename = entry.getKey();
            String fileId = entry.getValue();
            String currId = newBlobs.get(filename); // The file id of current commit.
            // This file in currBlobs and that in objBlobs is different version.
            // This file does not exist in objBlobs
            if (currId == null) {
                // This file also absent in split point.
                if (splitPointBlobs.get(filename) == null) {
                    newBlobs.put(filename, fileId);
                } else if (!splitPointBlobs.get(filename).equals(fileId)) {
                    // This file is present in split point but different version to object commit.
                    System.out.println("Encountered a merge conflict.");
                    // Create conflict file
                    File conflictFile = createConflictFile(filename, null, fileId);
                    newBlobs.put(filename, sha1(filename + readContentsAsString(conflictFile)));
                }
            } else {
                // This file exists, but different version to the current commit.
                if (!newBlobs.get(filename).equals(fileId)) {
                    /* split point version is the same as current commit,
                       then change the version to object commit.
                     */
                    if (splitPointBlobs.get(filename).equals(newBlobs.get(filename))) {
                        newBlobs.put(filename, fileId);
                    } else if (!splitPointBlobs.get(filename).equals(fileId)) {
                        // split point version is different to both of them.
                        System.out.println("Encountered a merge conflict.");
                        // Create conflict file
                        File conflictFile = createConflictFile(filename, null, fileId);
                        newBlobs.put(filename, sha1(filename + readContentsAsString(conflictFile)));
                    }
                }
            }
        }
        newCommit.parents.add(0, currCommit);
        newCommit.parents.add(1, objCommit);
        newCommit.blobs = newBlobs;
        // Save the new commit.
        saveCommit(newCommit);
        /* Delete all files in CWD except untracked files,
           then write the untracked files and blobs of the commit to CWD
         */
        for (String f : plainFilenamesIn(CWD)) {
            if (join(CWD, f).isFile() && !untrackedFiles.contains(f)) {
                restrictedDelete(join(CWD, f));
            }
        }
        // Write the blobs of the commit to CWD
        for (Map.Entry<String, String> entry : newCommit.blobs.entrySet()) {
            String filename = entry.getKey();
            String fileId = entry.getValue();
            String contents = readContentsAsString(join(BLOBS_DIR, fileId));
            writeContents(join(CWD, filename), contents);
        }
        // move the current branch to new commit.
        setBranch(getHead(), newCommit);
    }

    private static Commit findSplitPoint(Commit curr, Commit obj) {
        // Get all ancestors of curr. Traverse the ancestors
        HashSet<Commit> currAncestors = new HashSet<>(); // Store the ancestors.
        Stack<Commit> fringe = new Stack<>();
        fringe.push(curr);
        while (!fringe.isEmpty()) {
            Commit c = fringe.pop();
            currAncestors.add(c);
            for (Commit adj : c.parents) {
                if (!currAncestors.contains(adj)) {
                    fringe.push(adj);
                }
            }
        }
        // Use BFS to traverse the ancestors of obj. Check if the commit is curr's ancestor
        HashSet<Commit> objAncestors = new HashSet<>();
        LinkedList<Commit> bfsFringe = new LinkedList<>();
        bfsFringe.addFirst(obj);
        while (!bfsFringe.isEmpty()) {
            Commit c = bfsFringe.removeLast();
            // Check if the commit is ancestor of curr
            for (Commit a : currAncestors) {
                if (a.equals(c)) {
                    return c;
                }
            }
//            if (currAncestors.contains(c)) {
//                return c;
//            }
            objAncestors.add(c);
            for (Commit adj : c.parents) {
                if (!objAncestors.contains(adj)) {
                    bfsFringe.addFirst(adj);
                }
            }
        }
        return null;
    }

    private static File createConflictFile(String filename, String currFileId, String objFileId) {
        StringBuilder sb = new StringBuilder();
        String currContents, objContents;
        if (currFileId == null) {
            currContents = "";
        } else {
            currContents = readContentsAsString(join(BLOBS_DIR, currFileId));
        }
        if (objFileId == null) {
            objContents = "";
        } else {
            objContents = readContentsAsString(join(BLOBS_DIR, objFileId));
        }
        sb.append("<<<<<<< HEAD\n");
        sb.append(currContents);
        sb.append("=======\n");
        sb.append(objContents);
        sb.append(">>>>>>>");
        String newContents = sb.toString();
        String newFileId = sha1(filename + newContents);
        File newFile = join(BLOBS_DIR, newFileId);
        writeContents(newFile, newContents);
        return newFile;
    }

}
