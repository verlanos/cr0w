package verlanos.social.crow;

/**
 * This class serves as a command central for cr0w.
 *
 * @author Sefverl Balasingam
 * @since 05/08/2014
 */

public class CommandInterpreter {
    private static CommandInterpreter instance = null;

    public static final String COMMAND_FOLLOW = "follows";
    public static final String COMMAND_MESSAGE = "->";
    public static final String COMMAND_WALL = "wall";

    protected CommandInterpreter(){
        // Prevent instantiation
    }

    /**
     * Returns an instance of CommandInterpreter
     * <p>
     *     This method always returns an instance of CommandInterpreter. If an instance has been created prior
     *     that instance is returned.
     * </p>
     * @return an instance of CommandInterpreter
     */
    public static CommandInterpreter getInstance(){
        if(instance == null){
            instance = new CommandInterpreter();
        }

        return instance;
    }

    /**
     * Performs an action depending on the specified command
     * <p>
     *     This method performs either of the following actions (depending on command):
     *     "[username]" --> SHOW TIMELINE
     *     "[username] -> [message]" --> POST MESSAGE
     *     "[username] follows [otherUser]" --> FOLLOW USER
     *     "[username] wall" --> SHOW WALL
     * </p>
     * @param command a space-delimited command string
     */
    public void interpret(String command)
    {
        String sanitised = command.trim();
        String[] args = sanitised.split("\\s+");

        switch (args.length){
            case 0:
                // Do nothing
                break;
            case 1:
                // verlanos.social.crow.TODO: Display all messages in username's timeline
                Aggregator.getInstance().printTimeline(args[0]);
                break;
            case 2:
                //verlanos.social.crow.TODO: Display all messages in username's Wall
                if(args[1].equalsIgnoreCase(COMMAND_WALL)){
                    Aggregator.getInstance().printWall(args[0]);
                }
                else{
                    System.out.println("ERROR: Never heard of this command :(");
                    this.printHelp();
                }
                break;
            default:
                if(args[1].equalsIgnoreCase(COMMAND_FOLLOW)){
                    //verlanos.social.crow.TODO: Subscribe username to other person
                    Database.getInstance().open();
                    Database.getInstance().addFollowee(args[0],args[2]);
                    Database.getInstance().close();
                }
                else if(args[1].equalsIgnoreCase(COMMAND_MESSAGE)){
                    //verlanos.social.crow.TODO: Write message to people's timeline
                    Database.getInstance().open();
                    Database.getInstance().addMessage(args[0],sanitised.substring(sanitised.indexOf(COMMAND_MESSAGE)+COMMAND_MESSAGE.length()+1));
                    Database.getInstance().close();
                }
                else{
                    System.out.println("ERROR: Never heard of this command :(");
                    this.printHelp();
                }
        }


    }

    /**
     * Prints a list of available commands to standard output
     */
    public void printHelp(){
        System.out.println("COMMANDS:");
        System.out.println("\"[username]\"\n\t\tSee timeline");
        System.out.println("\"[username] "+COMMAND_MESSAGE+" [message]\"\n\t\tPost message (Use to register yourself " +
                "with cr0w)");
        System.out.println("\"[username] "+COMMAND_FOLLOW+" [otherUsername]\"\n\t\tFollow [otherUsername]");
        System.out.println("\"[username] "+COMMAND_WALL+"\"\n\t\tSee [username]'s Wall");
        System.out.println("\"exit\"\n\t\tSay Goodbye to cr0w");
        System.out.println();

    }
}
