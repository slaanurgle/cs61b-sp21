package gitlet;

import java.io.File;
import java.util.TreeMap;

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
    public static final File OBJECTS_DIR = join(GITLET_DIR, "commits");
    public static final File BRANCHES_DIR = join(GITLET_DIR, "branches");
    public static final File HEAD = join(BRANCHES_DIR, "HEAD");

    /* TODO: fill in the rest of this class. */
    /** Remove the .gitlet folder */
    public static void clearRepo() {
        if (GITLET_DIR.exists()) {
            clearFolder(GITLET_DIR);
        }
    }
    /** Helper Method: remove the DIRECTORY */
    public static void clearFolder(File directory) {
        File[] files = directory.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                clearFolder(f);
            }
            f.delete();
        }
        directory.delete();
    }


    /** Create a commit file and save a commit into a file */
    public static void saveCommit(Commit commit) {
        // create a folder named the first 6 digits of the commit id
        String id = commit.getID();
        File f = join(OBJECTS_DIR, id.substring(0, 6));
        safetyCreate(f);
        writeObject(f, commit);
    }
    /* Methods relevant to branches */
    /** Save a branch as a file with given NAME and store the COMMIT id.
     *  If the file does not exist it will create it.
     *  If already exists, overwrite. */
    public static void saveBranch(String name, Commit commit) {
        File fIn = join(BRANCHES_DIR, name);
        if (!fIn.exists()) {
            safetyCreate(fIn);
        }
        String id = commit.getID();
        writeContents(fIn, id);
    }

    /** Set BRANCH as HEAD pointer */
    public static void setHead(String branch) {
        writeContents(HEAD, branch);
    }
    /** Get the commit ID that the NAME branch pointing, HEAD is also ok */
    public static String getBranch(String name) {
        File fIn;
        if (name.equals("HEAD")) {
            fIn = join(BRANCHES_DIR, readContentsAsString(HEAD));
        } else {
            fIn = join(BRANCHES_DIR, name);
        }
        return readContentsAsString(fIn);
    }
    /** get the commit of the ID, ID can be abbreviated
     *  If the commit do not exists, return null. */
    public static Commit toCommit(String id) {
        File fIn = join(OBJECTS_DIR, id.substring(0, 6));
        if (!fIn.exists()) {
            return null;
        }
        return readObject(fIn, Commit.class);
    }
    /** get the commit of the BRANCH
     *  if the branch point to a non-exist commit, return null */
    public static Commit getCommit(String branch) {
        return toCommit(getBranch(branch));
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
        BRANCHES_DIR.mkdir();
        safetyCreate(HEAD);
        Commit.initCommit();
    }

    /* Below are the command method */
    /** Add the file specified by the string to repo.
     *  If the file does not exist, throw an error. */
    public static void addFile(String filename) {
        File fIn = join(CWD, filename);
        // the file does not exist in CWD
        if (!fIn.exists()) {
            throw error("File does not exist.");
        }
        String contents = readContentsAsString(fIn); // the contents of the added file
        String id = sha1(contents); // the id of the added file
        File fOut = join(ADDED_DIR, filename); // the object file
        boolean fOutExists = fOut.exists();
        File fRemove = join(REMOVED_DIR, filename); // the file of the same name in ./removed
        boolean fRemoveExists = fRemove.exists();
        // If current branch has identical version, do not stage it and remove it if it is already in ./added
        Commit head = getCommit("HEAD");
        if (head.contains(id)) {
            if (fOutExists) {
                restrictedDelete(fOut);
            }
        }
        if (fRemoveExists) {
            restrictedDelete(fRemove);
        }
        // there exists same file in ./added
        if (!fOutExists) {
            safetyCreate(fOut);
        }
        writeContents(fOut, contents);

    }

    /** Commit */
    public static void commit(String message) {
        String currBranch = getBranch("HEAD");
        Commit newCommit = new Commit(message);
        Commit prevCommit = getCommit("HEAD");
        // set parent
        newCommit.parents.add(prevCommit);
        // move current Branch and head
        saveBranch(currBranch, newCommit);
        setHead(currBranch);
        // the Commit has same blobs as its parent
        newCommit.blobs = new TreeMap<>(prevCommit.blobs);
        for (String f : plainFilenamesIn(ADDED_DIR)) {
            if ()
        }
        // Update the new version of the blobs

    }
}
