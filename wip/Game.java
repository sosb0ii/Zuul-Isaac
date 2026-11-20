import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game
{
    private Parser parser;
    private World world;
    private Player activePlayer;
    private boolean quitGame;
    private boolean keepPlaying;
    //private Room currentRoom;
    //private ArrayList<Room> roomHistory;
    //private HashMap<Item, Room> itemRegistry; //in case you need to know where a specified item is
    //private HashMap<Player, Room> characterLocations; //in case you need to know where a specified player is
    //private HashSet<String, Player> playerRoster; //ensure players do not have the same name; or just check the characterLocations HashMap??
    //private Room defaultRoom;
    
    /**
     * Create the game by loading the default world and initialise its internal map.
     */
    public Game() 
    {
        world = World.default();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office;
      
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        
        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theater.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean success;
        keepPlaying = true;
        while (keepPlaying) {
            Command command = parser.getCommand();
            success = processCommand(command);
            if (!success){ // let them know if the command word was valid but didn't work
                System.out.println("That didn't work. Maybe try something different?");
            }
            if(this.isOver()){
                System.out.println("You have completed the game! Congratulations!");
                keepPlaying = false;
            }
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true if the command executed successfully
     */
    private boolean processCommand(Command command) 
    {

        if(command == null || !command.isValid()) {
            System.out.println("That didn't make sense. Type 'help' for a list of valid commands.");
            return true; //avoid printing two error messages
        }
        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
            return true;
        }
        else if (commandWord.equals("go")) {
            return activePlayer.takeExit(command.getArg(0));
        }
        else if (commandWord.equals("quit")) {
            keepPlaying = false;
            return true;
        }
        else if (commandWord.equals("clear")) {
            clearDisplay();
            return true;
        } else if (commandWord.equals("eat")){
            return activePlayer.eat(command.getArg(0));
        } else if (commandWord.equals("look")){
            System.out.print("You are in "+activePlayer.getCurrentRoom().getName()); //identify room
            System.out.println(", "+activePlayer.getCurrentRoom().getDescription());
            System.out.println("\nYou see "+activePlayer.getCurrentRoom().listPlayers()+" in the room");
            System.out.println(activePlayer.getCurrentRoom().listItems());
            System.out.println("\nYou can exit via "+activePlayer.getCurrentRoom().listExits());
            return true;
        } // else something went wrong
        return false;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /**
     * Clear the interface
     */
    private void clearDisplay(){
        System.out.println("\n".repeat(40));
    }
    
    // /**
     // * Add a player to the game, in the default room
     // */
    // public void addPlayer(Player player){
        // playerRoster.add(player);
        // player.setRoom(defaultRoom);
    // }

    // /**
     // * Add a player to the game, in a specified room
     // */
    // public void addPlayer(Player player){
        // playerRoster.add(player);
    // }
    
    
}




/**
 
  
 

  
 
*/
