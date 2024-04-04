package gitlet;

import static gitlet.Utils.error;
/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            error("Please enter a command.");
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                // initialize the repo
                Repository.initRepo();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                String filename = args[1];
                Repository.addFile(filename);
                break;
            case "commit":
                if (args.length == 1) {
                    error("Please enter a commit message.");
                }
                String message = args[1];
                Repository.commit(message);
                break;
            case "rm":
                filename = args[1];
                Repository.removeFile(filename);
            case "log":
                Repository.printLog();
                break;
            case "global-log":
                Repository.printGlobalLog();
                break;
            case "status":
                Repository.printStatus();
                break;
            case "checkout":
                switch(args.length) {
                    case 2:
                        Repository.checkoutBranch(args[1]);
                        break;
                    case 3:
                        if (!args[1].equals("--")) {
                            throw error("Incorrect operands.");
                        }
                        Repository.checkoutFile(args[2]);
                        break;
                    case 4:
                        if (!args[2].equals("--")) {
                            throw error("Incorrect operands.");
                        }
                        Repository.checkoutFile(args[1], args[3]);
                        break;
                    default:
                        throw error("Incorrect operands.");
                }
                break;
            case "branch":

                Repository.createBranch(args[1]);
                break;
            case "rm-branch":
                Repository.removeBranch(args[1]);
            default:
                error("No command with that name exists.");
            // TODO: FILL THE REST IN

        }
    }
}
