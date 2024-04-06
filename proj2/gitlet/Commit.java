package gitlet;

import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author Slaanurgle
 */
public class Commit implements Serializable {
    /**
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Commit c = (Commit) o;
        return this.getID().equals(c.getID());
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    }

//    /** return whether two commit has equal blobs */
//    public boolean equalBlobs(Commit c) {
//        if (this.blobs.size() != c.blobs.size()) {
//            return false;
//        }
//        for ()
//    }
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
            String secondParent = parents.get(1).getID().toString().substring(0, 7);
            System.out.print("Merge: ");
            System.out.print(firstParent);
            System.out.print(" ");
            System.out.println(secondParent);
        }
        Formatter dateFormatter = new Formatter();
        // Format: Date: Thu Nov 9 17:01:33 2017 -0800
        dateFormatter.format(Locale.US, "Date: %ta %tb %te %tT %tY %tz",
                date, date, date, date, date, date);

        System.out.println(dateFormatter);
        System.out.println(message);
        System.out.println();
    }


//    public void addParent(Commit parent) {
//        parents.add(parent);
//    }


}
