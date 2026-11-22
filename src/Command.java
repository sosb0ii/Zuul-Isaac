import java.util.*;
/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * This class holds information about a command that was issued by the user.
 * A command currently consists of two strings: a command word and a second
 * word (for example, if the command was "take map", then the two strings
 * obviously are "take" and "map").
 * 
 * The way this is used is: Commands are already checked for being valid
 * command words. If the user entered an invalid command (a word that is not
 * known) then the command word is <null>.
 *
 * If the command had only one word, then the second word is <null>.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Command
{
    private String inputLine;
    private String commandWord;
    private ArrayList<String> args; //commands with any number of words
    // private boolean isValid; //whether the command is valid or not

    public Command(String inputLine){ //have must be string, cannot be null
        this.inputLine = inputLine;
        commandWord = ""; //initialize with empty string to avoid null errors
        args = new ArrayList<>();
        Scanner tokenizer = new Scanner(inputLine);
        if(tokenizer.hasNext()) {
            commandWord = tokenizer.next().toLowerCase();      // get first word
        }
        while(tokenizer.hasNext()){
            args.add(tokenizer.next());
        }
        tokenizer.close();
    }

    public boolean isValid(){
        if (commandWord == null){
            return false;
        } else if (CommandWords.isCommand(commandWord)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return the command word (the first word) of this command. If the
     * command was not understood, the result is null.
     * @return The command word.
     */
    public String getCommandWord()
    {
        return commandWord;
    }

    /**
     * The argument at the specified index, as a string
     * @param index the index of interest
     * @return the argument as a string
     */
    public String getArg(int index){
        if (index < 0 || index >= args.size()){ // check for invalid index
            return null;
        }
        return args.get(index);
    }

    /**
     * Whether the command has arguments (other than the command word)
     * @return true if there is at least one argument
     */
    public boolean hasArgs(){
        return !args.isEmpty();
    }
    // /**
    //  * Create a command object. First and second word must be supplied, but
    //  * either one (or both) can be null.
    //  * @param firstWord The first word of the command. Null if the command
    //  *                  was not recognised.
    //  * @param secondWord The second word of the command.
    //  */
    // public Command(String firstWord, String secondWord)
    // {
    //     commandWord = firstWord;
    //     this.secondWord = secondWord;
    // }


    // /**
    //  * @return The second word of this command. Returns null if there was no
    //  * second word.
    //  */
    // public String getSecondWord()
    // {
    //     return secondWord;
    // }

    // /**
    //  * @return true if this command was not understood.
    //  */
    // public boolean isUnknown()
    // {
    //     return (commandWord == null);
    // }

    // /**
    //  * @return true if the command has a second word.
    //  */
    // public boolean hasSecondWord()
    // {
    //     return (secondWord != null);
    // }

    /**
     * Overriding the toString method.
     */
    public String toString() {
        return inputLine;
        // String strOut = "";
        // if (commandWord != null && !commandWord.isBlank()){ //add the command word to the string
        //     strOut += commandWord;
        // }

        // if(commandWord!=null) {
        //     if(secondWord!=null) {
        //         return commandWord+" "+secondWord;
        //     } else {
        //         return commandWord;
        //     }
        // } 
        
        
        // else {
        //     if(secondWord!=null) {
        //         return secondWord;
        //     } else {
        //         return "";
        //     }
        // }
    }
}

