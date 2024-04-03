/** This file must be run in CWD/Unittest */
package gitlet;

import static gitlet.Repository.*;
import org.junit.Test;
import org.junit.Assert;
import static gitlet.Utils.*;
import java.io.File;

public class gitletTest {
    public static final File TEST = new File("C:\\Learning\\CS\\DataStructure\\cs61b\\cs61b-sp21\\proj2\\Unittest");
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static void clearTest() {
        Repository.clearFiles(TEST);
        Repository.clearRepo();
//        if (TEST.exists()) {
//            Repository.clearFolder(TEST);
//        }
//        TEST.mkdir();
    }
    @Test
    public void initTest() {
        clearTest();
        Repository.initRepo();
    }

    @Test
    public void singleAddTest() {
        clearTest();
        Repository.initRepo();
        File f1 = join(CWD, "a.txt");
        safetyCreate(f1);
        writeContents(f1, "a\n");
        Repository.addFile("a.txt");
    }

    @Test
    public void repeatAddTest() {
        singleAddTest();
        Repository.addFile("a.txt");
        File f2 = join(CWD, "b.txt");
        writeContents(f2, "b\n");
        Repository.addFile("b.txt");
    }

    @Test
    public void singleCommitTest() {
        singleAddTest();
        Repository.commit("Add a.txt");
    }

    public static void readCommit(File f) {
        Commit c = readObject(f, Commit.class);
        System.out.println(c.blobs.toString());
    }
    @Test
    public void versionsCommitTest() {
        clearTest();
        Repository.initRepo();
        File f1 = join(CWD, "a.txt");
        safetyCreate(f1);
        writeContents(f1, "a\n");
        Repository.addFile("a.txt");
        Repository.commit("Add a.txt");
        writeContents(f1, "a version 2\n");
        Repository.addFile("a.txt");
        Repository.commit("a.txt version  2");
        writeContents(f1, "a\n");
        Repository.addFile("a.txt");
        Repository.commit("a.txt back to initial");
        Repository.printLog();
        //Repository.printGlobalLog();
    }

    @Test
    public void findTest() {
        clearTest();
        Repository.initRepo();
        for (int i = 0; i < 3; i++) {
            File f1 = join(CWD, "a" + i + ".txt");
            writeContents(f1, "a" + i + "\n");
            addFile("a" + i + ".txt");
            commit("Add new file.");
        }
        findCommits("Add new file.");

    }
}
