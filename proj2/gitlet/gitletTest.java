/** This file must be run in CWD/Unittest
 */
package gitlet;

import static gitlet.Repository.*;
import org.junit.Test;

import static gitlet.Utils.*;
import java.io.File;

public class gitletTest {
    public static final String TESTPATH = "C:\\Learning\\CS\\DataStructure\\cs61b\\cs61b-sp21\\proj2\\Unittest";

    public static File CWD;

    public static void quickCreate(String filename) {
        writeContents(join(CWD, filename), "filename" + "111\n");
    }
    public static void addAndCommit(String filename) {
        addFile(filename);
        commit("add " + filename);
    }

    /** Make preparation for the test, all test should start with this. */
    public static void clearTest() {
        System.setProperty("user.dir", TESTPATH);
        CWD = new File(System.getProperty("user.dir"));
        Repository.clearRepo();
        Repository.clearFiles(CWD);
    }
    @Test
    public void initializingTest() {
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
        //printStatus();
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

    @Test
    public void singleRemoveTest() {
        clearTest();
        initRepo();
        File f1 = join(CWD, "a.txt");
        File f2 = join(CWD, "b.txt");
        writeContents(f1, "a");
        writeContents(f2, "b");
        addFile("b.txt");
        addFile("a.txt");
        removeFile("b.txt");
        commit("Add a.txt");
        removeFile("a.txt");
        commit("Remove a.txt");
        printLog();
    }

    @Test
    public void versionsCheckoutTest() {
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
        printLog();
        checkoutFile("a.txt");
        //Repository.printGlobalLog();
    }
    @Test
    public void branchesTest() {
        clearTest();
        initRepo();
        quickCreate("a.txt");
        addAndCommit("a.txt");
        createBranch("brnA");
        quickCreate("b.txt");
        addAndCommit("b.txt");
        checkoutBranch("master");
        printLog();
        printStatus();
        checkoutBranch("brnA");
        printLog();
        printStatus();
    }
}
