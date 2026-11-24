import java.io.File;
import java.nio.file.Path;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 * 
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */
public class Parser 
{
    private CommandWords commands;  // holds all valid command words
    private Scanner reader;         // source of command input
    private boolean isFileInput = false; //only true if reading from file
    public static final String scriptFolder = "run-throughs";

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() 
    {
        commands = new CommandWords(); //completely unneccessaryy???
        reader = new Scanner(System.in);
    }

    /**
     * Create a parser from a file.
     */
    public Parser(String filename) {
        commands = new CommandWords();
        Path filePath = Path.of(scriptFolder,filename);
        try {
            reader = new Scanner(filePath.toFile());
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find the file "+filename);
            System.out.println("Reverting to use the command line for input.");
            reader = new Scanner(System.in);
        } catch (IOException e) {
            System.out.println("Error with File I/O:");
            e.printStackTrace();
            System.out.println("Reverting to use the command line for input.");
            reader = new Scanner(System.in);
        } 
        isFileInput=true;
    }
    
    /**
     * @return true if the input is from a file and false if it is from std.in.
     */
    public boolean usingFile() {
        return isFileInput;
    }

    /**
     * @return The next command from the user.
     */
    public Command getCommand() 
    {
        String inputLine;   // will hold the full input line
        System.out.print("> ");     // print prompt

        inputLine = reader.nextLine();
        if (inputLine == null || inputLine.isEmpty()) { //must do this because constructor won't take null
            return null;
        }
        return new Command(inputLine);
    }

    /**
     * Print out a list of valid command words.
     */
    public void showCommands()
    {
        commands.showAll();
    }
}
