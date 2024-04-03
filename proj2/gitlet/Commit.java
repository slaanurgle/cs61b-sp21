package gitlet;

// TODO: any imports you need here

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
    String message;
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
        Repository.setBranch("master", c);
        Repository.setHead("master");
        Repository.saveCommit(c);
    }

    public void printInfo() {
        String id = this.getID();
        System.out.println("===");
        System.out.print("commit ");
        System.out.println(id);
        if (parents.size() == 2) {
            String firstParent = parents.get(0).getID().toString().substring(0, 7);
            String secondParent = parents.get(0).getID().toString().substring(0, 7);
            System.out.print("Merge: ");
            System.out.print(firstParent);
            System.out.print(" ");
            System.out.println(secondParent);
        }
        System.out.printf("Date: %ta %tb %te %tT %tY %tZ", date ,date, date, date, date, date); // Date: Thu Nov 9 17:01:33 2017 -0800
        System.out.println();
        System.out.println(message);
        System.out.println();
    }


//    public void addParent(Commit parent) {
//        parents.add(parent);
//    }


}
