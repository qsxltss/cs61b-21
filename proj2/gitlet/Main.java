package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if(args.length == 0)
        {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        Repository resp = new Repository();
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                validateNumArgs( args, 1);
                resp.initTask();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                validateNumArgs(args,2);
                resp.addTask(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                validateNumArgs(args,2);
                resp.commitTask(args[1]);
                break;
            case "rm":
                validateNumArgs(args,2);
                resp.rmTask(args[1]);
                break;
            case "log":
                validateNumArgs(args,1);
                resp.logTask();
                break;
            case "global-log":
                validateNumArgs(args,1);
                resp.globallogTask();
                break;
            case "find":
                validateNumArgs(args,2);
                resp.findTask(args[1]);
                break;
            case "status":
                validateNumArgs(args,1);
                resp.statusTask();
                break;
            case "checkout":
                if(args.length == 3)
                {
                    if(!args[1].equals("--"))
                    {
                        System.out.println("Incorrect operands.");
                        System.exit(0);
                    }
                    resp.checkout1Task(args[2]);
                }
                else if(args.length == 4)
                {
                    if(!args[2].equals("--"))
                    {
                        System.out.println("Incorrect operands.");
                        System.exit(0);
                    }
                    resp.checkout2Task(args[1],args[3]);
                }
                else if(args.length == 2)
                {
                    resp.checkout3Task(args[1]);
                }
                break;
            case "branch":
                validateNumArgs(args,2);
                resp.branchTask(args[1]);
                break;
            case "rm-branch":
                validateNumArgs(args,2);
                resp.rmbranchTask(args[1]);
                break;
            case "reset":
                validateNumArgs(args,2);
                resp.resetTask(args[1]);
                break;
            case "merge":
                validateNumArgs(args,2);
                resp.mergeTask(args[1]);
                break;
            default:
                System.out.println("No command with that name exists");
                System.exit(0);
        }
    }
    public static void validateNumArgs( String cmd,String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
    public static void validateNumArgs( String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
}
