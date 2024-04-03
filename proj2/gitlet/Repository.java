package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
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
            RemoveFolder(GITLET_DIR);
        }
    }
    /** Helper Method: remove the DIRECTORY */
    public static void RemoveFolder(File directory) {
        File[] files = directory.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                RemoveFolder(f);
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
    /** Initial the repo. Create all the directories needed.
     *  If .gitlet already exists, throws error.
     */
    public static void initRepo() {
        // check whether .gitlet is already exists.
        if (GITLET_DIR.exists()) {
             throw error("A Gitlet version-control system already exists in the current directory.");
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

    /* Below are the command method */
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
            throw error("No changes added to the commit.");
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
        for (String commitId : plainFilenamesIn(COMMITS_DIR)) {
            Commit commit = readObject(join(COMMITS_DIR, commitId), Commit.class);
            if (commit.message.equals(message)) {
                commit.printInfo();
            }
        }
    }

    public static void printStatus() {
        // Classify the files.
        ArrayList<String> added = new ArrayList<>(plainFilenamesIn(ADDED_DIR)); // The strings to be print.
        ArrayList<String> removed = new ArrayList<>(plainFilenamesIn(REMOVED_DIR));
        ArrayList<String> modified = new ArrayList<>();
        ArrayList<String> untracked = new ArrayList<>();
        Commit head = getCommit("HEAD");
        TreeMap<String, String> headBlobs = head.blobs;
        // Get the files of CWD.
        HashMap<String, String> cwdFiles = new HashMap<>();
        for (String cwdFilename : plainFilenamesIn(CWD)) {
            File cwdFile = join(CWD, cwdFilename);
            if (cwdFile.isFile()) {
                String cwdFileId = sha1(cwdFilename + readContentsAsString(cwdFile));
                cwdFiles.put(cwdFilename, cwdFileId);
            }
        }
        // classify modified files.
        /* traverse the files in the working directory,
           If it is tracked in the current commit, changed in the working directory, but not staged.
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
            if (!cwdFiles.containsValue(fileId)) {
                added.remove(filename);
                modified.add(filename + " (modified)");
            }
        }
        // Not staged for removal, but tracked in the current commit and deleted from the working directory.
        for (Map.Entry<String, String> entry : head.blobs.entrySet()) {
            String filename = entry.getKey();
            String fileId = entry.getValue();
            // HEAD has the files but CWD does not have
            if (!join(REMOVED_DIR, filename).exists() && !join(CWD, filename).exists()) {
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
        System.out.println("=== Untracked Files` ===");
        for (String s : untracked) {
            System.out.println(s);
        }
        System.out.println();
    }

}
