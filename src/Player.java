import java.util.HashMap;
import java.util.ArrayList;
/**
 * A character in the Zuul game. Can represent a player or an NPC.
 *
 * @Isaac
 * @November 6 2026
 */
public class Player
{
    private String name;
    private Room currentRoom;
    private ArrayList<Room> roomHistory;
    private Items inventory;
    private double capacity;
    private String likeness;
    
    /**
     * Create a player with a certain name.
     * @param name the name of the player
     */
    public Player (String name){
        this.name = name;
        roomHistory = new ArrayList<>();
        inventory = new Items();
        likeness = "";
        capacity = (double) 30;
    }

    /**
     * Create a player with a certain name.
     * @param name the name of the player
     */
    public Player (String name, double capacity){
        this.name = name;
        roomHistory = new ArrayList<>();
        inventory = new Items();
        likeness = "";
        this.capacity = capacity;
    }

    
    /**
     * Get the name of the player
     * @return the name of the player
     */
    public String getName(){
        return name;
    }
    
    /**
     * Get the room the player is currently in
     * @return the current room of the player
     */
    public Room getCurrentRoom(){
        return currentRoom;
    }
    
    /**
     * Get the likeness of the character
     * @return the likeness of the character
     */
    public String getLikeness(){
        return likeness;
    }

    /**
     * Set the likeness of the character
     * @param likeness the likeness of the character
     */
    public void setLikeness(String likeness){
        this.likeness = likeness;
    }
    
    /**
     * Get the carrying capacity of the player
     * @return the carrying capacity of the player
     */
    public double getCapacity(){
        return capacity;
    }
    
    /**
     * How much more weight the player can carry
     * @return player's capacity minus the weight currently carried
     */
    public double canCarry(){
        return capacity - getInventoryWeight();
    }
    
    /**
     * How much the player is carrying
     * @return the total weight of all the items in the player's inventory
     */
    public double getInventoryWeight(){
        return inventory.getTotalWeight();
    }
    
    /**
     * Set the carrying capacity of the player
     * @param capacity the new carrying capacity of the player
     */
    public void setCapacity(double capacity){
        this.capacity = capacity;
    }

    //handle room update                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        */
    private boolean goRoom(Room newRoom){
        if (newRoom == currentRoom){ //ensure it is a different room
            return false;
        }
        boolean vacated = false;
        boolean entered = false;
        boolean logged = false;
        if (currentRoom == null){ //for the player spawn-in case
            vacated = true;
        } else{
            vacated = currentRoom.removePlayer(this);
        } //ensure the rrom has been properly vacated
        entered = newRoom.addPlayer(this); //ensure the new room is properly entered
        logged = roomHistory.add(currentRoom); //add the room being left to the history
        currentRoom = newRoom; //update the field
        return (vacated && entered && logged); //false if any fail
    }
    
    /**
     * Take an exit based on the current position of the player
     * @param exitName the name of the exit to take
     * @return true if the exit is successful
     */
    public boolean takeExit(String exitName){
        if (!currentRoom.hasExit(exitName)){
            return false;
        }
        Room newRoom = currentRoom.getExit(exitName);
        return goRoom(newRoom);
    }
    
    //still might change the structure of this
    /**
     * Place the player in a room for the first time.
     * 
     * If player is already in a room, return false;
     * @param spawnpoint the room for the player to enter
     * @return true if the player successfully enters the room
     */
    public boolean spawn(Room spawnpoint){
        if (currentRoom != null){ //disallow if already in a room
            return false;
        }
        return goRoom(spawnpoint);
    }
    
    /**
     * Return to the previous room.
     * @return true if the backtrack is successful
     */
    public boolean backtrack(){
        return goRoom(roomHistory.removeLast()); // essentially resets to the previous state 
    }
    
    /**
     * Attempt to pick up an item from the current room
     * @param itemName the name of the item you want to pick up
     * @return true if successful
     */
    public boolean pickUp(String itemName){
        if (!inventory.contains(itemName)){
            return false; // 
        }
        double itemWeight = inventory.getItemWeight(itemName);
        if (itemWeight < 0){ //true when item not in inventory
            return false;
        } else if (itemWeight > this.canCarry()){ //too heavy
            return false; 
        } else { //exists, not too heavy
            Item item = // MAJOR CODING MISTAKE, SHOULD BE CHECKIGN ROOM NOT INVENTORY
        
        // verify that an item of that name exists in the current room
        // verify that the item is light enough for you to carry
        // remove the item from the room
        // add the item to your inventory
    }
    
}