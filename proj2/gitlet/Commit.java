package gitlet;

// TODO: any imports you need here

import org.antlr.v4.runtime.tree.Tree;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.TreeMap;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    /** The message of this Commit. */
    private String message;
    /** The date of this Commit */
    private Date date;
    /** the parents of this Commit */
    LinkedList<Commit> parents;
    /** the blobs of this Commit
     *  Blobs are files storing the contents.
     *  TreeMap maps the file name to file id. */
    TreeMap<String, String> blobs; //

    /* TODO: fill in the rest of this class. */
    /* constructors */
    public Commit() {
        this("initial commit", new Date(0));
    }

    public Commit(String message) {
        this(message, new Date());
    }

    public Commit(String message, Date date) {
        this.message = message;
        this.date = date;
        this.blobs = new TreeMap<>();
        this.parents = new LinkedList<>();
    }
    /** get the commit id */
    public String getID() {
        return sha1(serialize(this));
    }

    /** Initial the initial commit */
    public static void initCommit() {
        Commit c = new Commit();
        Repository.saveBranch("master", c);
        Repository.setHead("master");
        Repository.saveCommit(c);
    }

    /** Check if this commit contain FILEID in the blobs */
    public boolean contains(String fileId) {
        return blobs.containsValue(fileId);
    }

//    public void addParent(Commit parent) {
//        parents.add(parent);
//    }


}
