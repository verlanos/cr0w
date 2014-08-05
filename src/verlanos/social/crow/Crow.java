package verlanos.social.crow;

import java.util.Scanner;

/**
 * This class is responsible for launching the Application and interacting with the User via the Console.
 *
 * @author Sefverl Balasingam
 * @since 05/08/2014
 */

public class Crow {
    public static void main(String []args)
    {
       System.out.println("############################################");
       System.out.println("# Welcome to cr0w - The CLI Social Network #");
       System.out.println("############################################");
        System.out.println();
        CommandInterpreter.getInstance().printHelp();

       Scanner kb_reader = new Scanner(System.in);

       String input;
       do{

           System.out.print(">");
           input = kb_reader.nextLine();
           if(input.trim().equalsIgnoreCase("exit"))return;
           CommandInterpreter.getInstance().interpret(input);
       }
       while(true);
    }
}
