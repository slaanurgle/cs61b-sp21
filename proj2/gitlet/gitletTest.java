/** This file must be run in CWD/Unittest */
package gitlet;

import org.junit.Test;
import org.junit.Assert;
import static gitlet.Utils.*;
import java.io.File;

public class gitletTest {
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static void clearTest() {
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
    }
}
