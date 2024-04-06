package gitlet;

import java.util.List;
import java.util.Arrays;


import static gitlet.Utils.printError;
/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Slaanurgle
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    private static final List<String> COMMANDS = Arrays.asList
            ("init", "add", "commit", "rm", "log", "global-log", "status", "checkout",
                    "branch", "rm-branch", "reset", "merge");

    private static void checkOperandNum(String[] args, int expectedNum) {
        if (args.length != expectedNum + 1) {
            printError("Incorrect operands.");
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            printError("Please enter a command.");
        }
        String firstArg = args[0];
        if (COMMANDS.contains(firstArg) && !firstArg.equals("init")) {
            if (!Repository.hasGitlet()) {
                printError("Not in an initialized Gitlet directory.");
            }
        }
        switch(firstArg) {
            case "init":
                // initialize the repo
                checkOperandNum(args, 0);
                Repository.initRepo();
                break;
            case "add":
                checkOperandNum(args, 1);
                String filename = args[1];
                Repository.addFile(filename);
                break;
            case "commit":
                if (args.length == 1) {
                    printError("Please enter a commit message.");
                }
                checkOperandNum(args, 1);
                if (args[1].equals("")) {
                    printError("Please enter a commit message.");
                }
                String message = args[1];
                Repository.commit(message);
                break;
            case "rm":
                checkOperandNum(args, 1);
                filename = args[1];
                Repository.removeFile(filename);
                break;
            case "log":
                checkOperandNum(args, 0);
                Repository.printLog();
                break;
            case "global-log":
                checkOperandNum(args, 0);
                Repository.printGlobalLog();
                break;
            case "find":
                checkOperandNum(args, 1);
                Repository.findCommits(args[1]);
                break;
            case "status":
                checkOperandNum(args, 0);
                Repository.printStatus();
                break;
            case "checkout":
                switch(args.length) {
                    case 2:
                        Repository.checkoutBranch(args[1]);
                        break;
                    case 3:
                        if (!args[1].equals("--")) {
                            printError("Incorrect operands.");
                        }
                        Repository.checkoutFile(args[2]);
                        break;
                    case 4:
                        if (!args[2].equals("--")) {
                            printError("Incorrect operands.");
                        }
                        Repository.checkoutFile(args[1], args[3]);
                        break;
                    default:
                        printError("Incorrect operands.");
                }
                break;
            case "branch":
                checkOperandNum(args, 1);
                Repository.createBranch(args[1]);
                break;
            case "rm-branch":
                checkOperandNum(args, 1);
                Repository.removeBranch(args[1]);
                break;
            case "reset":
                checkOperandNum(args, 1);
                Repository.reset(args[1]);
                break;
            case "merge":
                checkOperandNum(args, 1);
                Repository.merge(args[1]);
                break;
            default:
                printError("No command with that name exists.");

        }
    }
}
