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
    // private boolean quitGame;
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
        // world = World.default();
        parser = new Parser();
    }

    // /**
    //  * Create all the rooms and link their exits together.
    //  */
    // private void createRooms()
    // {
    //     Room outside, theater, pub, lab, office;
      
    //     // create the rooms
    //     outside = new Room("outside the main entrance of the university");
    //     theater = new Room("in a lecture theater");
    //     pub = new Room("in the campus pub");
    //     lab = new Room("in a computing lab");
    //     office = new Room("in the computing admin office");
        
    //     // initialise room exits
    //     outside.setExit("east", theater);
    //     outside.setExit("south", lab);
    //     outside.setExit("west", pub);

    //     theater.setExit("west", outside);

    //     pub.setExit("east", outside);

    //     lab.setExit("north", outside);
    //     lab.setExit("east", office);

    //     office.setExit("west", lab);

    //     currentRoom = outside;  // start game outside
    // }

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
            if (command.isValid()){
                success = processCommand(command);
            } else {
                System.out.println("Didn't recognize that command.");
                System.out.println("Type 'help' to see a list of valid commands.");
                continue; // tries again
            }
            if (success){
                System.out.println(successMessage(command));
            } else{ //custom fail message
                System.out.println(failMessage(command));
            }
            if(isOver()){ //world's victory condition reached
                System.out.println("You have completed the game! Congratulations!");
                keepPlaying = false;
            }
        } // ends when keepPlaying is false. quit or win.
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
        System.out.print("You are in "+activePlayer.getCurrentRoom().getName());
        System.out.println(", "+activePlayer.getCurrentRoom().getDescription());
        System.out.println("Type 'help' if you need help.");
        System.out.println();
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
            boolean went = activePlayer.takeExit(command.getArg(0));
            if (went){
                printRoom(); //tell about the new room
            }
            return went;
        }
        else if (commandWord.equals("quit")) {
            return quit(command); // if successful, keepPlaying=false and return true
        }
        else if (commandWord.equals("clear")) {
            clearDisplay();
            return true;
        } else if (commandWord.equals("eat")){
            return activePlayer.eat(command.getArg(0));
        } else if (commandWord.equals("look")){
            System.out.print("You are in "+activePlayer.getCurrentRoom().getName()); //identify room
            System.out.println(", "+activePlayer.getCurrentRoom().getDescription());
            System.out.println("\nYou see "+activePlayer.getCurrentRoom().listOtherPlayers(activePlayer)+" in the room");
            System.out.println(activePlayer.getCurrentRoom().displayItems());
            System.out.println("\nYou can exit via "+activePlayer.getCurrentRoom().listExits());
            return true;
        } else if (commandWord.equals("inventory")){
            System.out.println(activePlayer.displayItems());
            return true;
        } else if (commandWord.equals("take")){
            return activePlayer.pickUp(command.getArg(0));
        }
        
        
        // else something went wrong
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
        printRoom();
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * Prints the room out to the player
     */
    private void printRoom(){
        System.out.print("You are in "+activePlayer.getCurrentRoom().getName()); //identify room
            System.out.println(", "+activePlayer.getCurrentRoom().getDescription());
    }

    /**
     * @return true if the win condition has been reached
     */
    public boolean isOver(){
        return activePlayer.hasItemWithAttribute("victory_token");
        // return activePlayer.getCurrentRoom().hasItemWithAttribute("squeaks");
    }

    public World getWorld(){
        return world;
    }

    private void setWorld(World world){
        this.world = world;
    }

    public void setActivePlayer(Player player){
        activePlayer = player;
    }
    public Player getActivePlayer(){
        return activePlayer;
    }

    //testing the setup. REMOVE LATER!!
    public static void main(String[] args) {
        World cheeseWorld = new World();
        Room plasmaLab = new Room("Plasma_Lab","So much fun!", cheeseWorld);
        Room vanderHall = new Room("Vanderwerf_Hallway","straight and long", cheeseWorld);
        Room mensRoom = new Room("Mens_Room","A place of business", cheeseWorld);
        Player Chii = new Player("Chii",12, mensRoom);
        Item laptop = new Item("laptop","sleek mac",8,plasmaLab);
        laptop.addAttribute("victory_token");
        Item mouse1 = new Item("mouse","goes squeak",2,mensRoom);
        mouse1.addAttribute("squeaks");
        Item mouse2 = new Item("mouse","goes click",1,plasmaLab);
        mouse2.addAttribute("clicks");
        Item mouse3 = new Item("mouse","goes squeak", 2, vanderHall);
        mouse3.addAttribute("squeaks");
        mensRoom.addExit("bath-exit", vanderHall);
        vanderHall.addExit("push_door", mensRoom);
        vanderHall.addExit("hall-door", plasmaLab);
        plasmaLab.addExit("lab-exit", vanderHall);
        Game game = new Game();
        game.setWorld(cheeseWorld);
        game.setActivePlayer(Chii);
        System.out.println(game.getActivePlayer().getName()+" is in "+Chii.getCurrentRoom().getName());
        System.out.println("Challenge: get a squeaker into the plasma lab");
        game.play();
    }

    // /** 
    //  * Try to in to one direction. If there is an exit, enter the new
    //  * room, otherwise print an error message.
    //  */
    // private void goRoom(Command command) 
    // {
    //     if(!command.hasSecondWord()) {
    //         // if there is no second word, we don't know where to go...
    //         System.out.println("Go where?");
    //         return;
    //     }

    //     String direction = command.getSecondWord();

    //     // Try to leave current room.
    //     Room nextRoom = currentRoom.getExit(direction);

    //     if (nextRoom == null) {
    //         System.out.println("There is no door!");
    //     }
    //     else {
    //         currentRoom = nextRoom;
    //         System.out.println(currentRoom.getLongDescription());
    //     }
    // }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasArgs()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            keepPlaying = false;
            return true;  // true bc command successful
        }
    }
    
    /**
     * Clear the interface
     */
    private void clearDisplay(){
        System.out.println("\n".repeat(40));
    }

    /**
     * Feedback for player when a command is successful.
     * Helpful to know the game is working
     * @param command the command to give feedback for
     * @return a string validating the success of the command
     */
    public String successMessage(Command command){
        String strOut = "";
        if (command.is("go")){
            strOut = "took " + command.getArg(0) +" to " + getActivePlayer().getCurrentRoom().getName();
        } else {
            strOut = "'"+command+"' was successful"; //toString() copies inputLine
        }
        return strOut;
    }

    /**
     * Feedback for player when a command is successful.
     * Can give tips based on attempted command
     * @param command the command to give feedback for
     * @return a string notifiying failure
     */
    public String failMessage(Command command){
        String strOut = "";
        if (command.is("go")){
            strOut += "couldn't take exit " + command.getArg(0);
        } else {
            strOut += "'"+command+"' was successful"; //toString() copies inputLine
        }
        strOut += "\n\nfor help, type 'help "+command.getCommandWord()+"'";
        return strOut;
    }

    /**
     * Help for each command.
     * @param command the command to give help for
     * @return a string with helpful information
     */
    public String helpMessage(Command command){
        // first initialize command info that might be specified in each help string
        int nargs = -1;
        ArrayList<String> parameters = new ArrayList<>();
        String explain = "";
        String tip = "";
        String example = "";
        // then filter the command names and add info accordingly
        if (command.is("go")){
            nargs = 1;
            explain = "go somewhere by taking a specified exit";
            tip = "use 'look' to see available exits";
            parameters.add("exit");
            example = "go double-doors";
        } else {
            tip = "use common sense";
        }

        if (!parameters.isEmpty()){ // if I'm as explicity as naming parameters, count them up for nargs
            nargs = parameters.size();
        }
        // finally structure the output string
        String strOut = "";
        if (nargs != -1){
            strOut += command.getCommandWord()+" ["+nargs+" argument(s)]";
        }
        if (!explain.isEmpty()){
            strOut += "\n"+explain;
        }
        if (!parameters.isEmpty()){
            strOut += "\n\nusage: "+command.getCommandWord()+" "+String.join(" ", parameters);
        }
        if (!example.isEmpty()){
            strOut += "\n\nfor example:\n"+example;
        }
        if (!tip.isEmpty()){
            strOut += "\n\nhere's a tip: "+tip;
        }
        return strOut;
    }

    
    
}




/**
 
  
 

  
 
*/
