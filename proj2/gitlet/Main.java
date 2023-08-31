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
                resp.staticTask();
                break;
            case "checkout":
                if(args.length == 3)
                {
                    resp.checkout1Task(args[2]);
                }
                else if(args.length == 4)
                {
                    resp.checkout2Task(args[1],args[3]);
                }
                else if(args.length == 2)
                {
                    //resp.checkout3Task();
                }
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
            System.exit(0);
        }
    }
}
