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
        checkoutFile("a.txt");
        printLog();
        //Repository.printGlobalLog();
    }
    /** Tests belows test the branch command and rm-branch command */
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

    @Test
    public void rmBranchTest() {
        clearTest();
        initRepo();
        File fa = join(CWD, "a.txt");
        writeContents(fa, "ver1");
        addAndCommit("a.txt");
        createBranch("brn1");
        writeContents(fa, "ver2");
        addAndCommit("a.txt");
        printStatus();
        removeBranch("master");
        printStatus();
    }
    /** The tests belows test status command */
    @Test
    public void modifiedTest1() {
        clearTest();
        initRepo();
        File fa = join(CWD, "a.txt");
        writeContents(fa, "ver1");
        addAndCommit("a.txt");
        writeContents(fa, "ver2");
        printStatus();

    }
    /* expected output:
    === Branches ===
    *master

    === Staged Files ===

    === Removed Files ===

    === Modifications Not Staged For Commit ===
    a.txt (modified)
     */

    @Test
    public void modifiedTest2() {
        clearTest();
        initRepo();
        File fa = join(CWD, "a.txt");
        writeContents(fa, "ver1");
        addAndCommit("a.txt");
        writeContents(fa, "ver2");
        addFile("a.txt");
        writeContents(fa, "ver1");
        printStatus();
    }
    /* expected output:
    === Branches ===
    *master

    === Staged Files ===

    === Removed Files ===

    === Modifications Not Staged For Commit ===
    a.txt (modified)
     */

    @Test
    public void modifiedTest3() {
        clearTest();
        initRepo();
        File fa = join(CWD, "a.txt");
        writeContents(fa, "ver1");
        addAndCommit("a.txt");
        writeContents(fa, "ver2");
        addFile("a.txt");
        restrictedDelete(fa);
        printStatus();
    }
    /* expected output:
    === Branches ===
    *master

    === Staged Files ===

    === Removed Files ===

    === Modifications Not Staged For Commit ===
    a.txt (deleted)
     */
    @Test
    public void modifiedTest4() {
        clearTest();
        initRepo();
        File fa = join(CWD, "a.txt");
        writeContents(fa, "ver1");
        addAndCommit("a.txt");
        writeContents(fa, "ver2");
        restrictedDelete(fa);
        printStatus();
    }
    /* expected output:
    === Branches ===
    *master

    === Staged Files ===

    === Removed Files ===

    === Modifications Not Staged For Commit ===
    a.txt (deleted)
     */

    @Test
    public void untrackedTest1() {
        clearTest();
        initRepo();
        quickCreate("a.txt");
        quickCreate("b.txt");
        addFile("a.txt");
        addFile("b.txt");
        commit("add a, b");
        quickCreate("c.txt");
        quickCreate("d.txt");
        printStatus();
    }
    /* expected output:
    === Branches ===
    *master

    === Staged Files ===

    === Removed Files ===

    === Modifications Not Staged For Commit ===

    === Untracked Files ===
    c.txt
    d.txt

     */

    @Test
    public void untrackedTest2() {
        clearTest();
        initRepo();
        quickCreate("a.txt");
        addAndCommit("a.txt");
        removeFile("a.txt");
        File fa = join(CWD, "a.txt");
        writeContents(fa, "ver1");
        printStatus();
    }
    /* expected output:
    === Branches ===
    *master

    === Staged Files ===

    === Removed Files ===

    === Modifications Not Staged For Commit ===

    === Untracked Files ===
    a.txt

     */

    /** Tests below test the merge command */
    @Test
    public void mergeTest1() {
        clearTest();
        initRepo();
        File fa = join(CWD, "a.txt");
        writeContents(fa, "ver1");
        addFile("a.txt");
        commit("add ver1");
        createBranch("brn1");
        writeContents(fa, "ver2");
        addFile("a.txt");
        commit("add ver2");
        checkoutBranch("master");
        writeContents(fa, "ver3");
        addFile("a.txt");
        commit("add ver3");
        merge("brn1");
        printLog();
    }

    @Test
    public void mergeTest2() {
        clearTest();
        initRepo();
        File fa = join(CWD, "a.txt");
        writeContents(fa, "ver1");
        addFile("a.txt");
        commit("add ver1");
        createBranch("brn1");
        writeContents(fa, "ver2");
        addFile("a.txt");
        commit("add ver2");
        checkoutBranch("master");
        merge("brn1");
        printLog();
    }

    @Test
    public void mergeTest3() {
        clearTest();
        initRepo();
        File fa = join(CWD, "a.txt");
        writeContents(fa, "ver1\n");
        addFile("a.txt");
        commit("add ver1");
        createBranch("brn1");
        writeContents(fa, "ver2\n");
        addFile("a.txt");
        commit("add ver2");
        checkoutBranch("master");
        removeFile("a.txt");
        commit("remove");
        merge("brn1");
        printLog();
    }

    @Test
    public void mergeTest4() {
        clearTest();
        initRepo();
        File fa = join(CWD, "a.txt");
        File fb = join(CWD, "b.txt");
        writeContents(fa, "ver1\n");
        addFile("a.txt");
        writeContents(fb, "ver1");
        addFile("b.txt");
        commit("add a,b ver1");
        createBranch("brn1");
        writeContents(fa, "ver2\n");
        addFile("a.txt");
        removeFile("b.txt");
        commit("add a ver2, remove b");
        checkoutBranch("master");
        removeFile("a.txt");
        commit("remove a");
        merge("brn1");
        printLog();
    }

    @Test
    public void mergeTest5() {
        clearTest();
        initRepo();
        File fa = join(CWD, "a.txt");
        File fb = join(CWD, "b.txt");
        writeContents(fa, "ver1");
        writeContents(fb, "ver1");
        addFile("a.txt");
        commit("add ver1");
        createBranch("brn1");
        writeContents(fa, "ver2");
        writeContents(fb, "ver2");
        addFile("a.txt");
        commit("add ver2");
        checkoutBranch("master");
        merge("brn1");
        printLog();
    }
}
