import java.util.*;
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
     * @param spawnpoint the room where the player starts
     */
    public Player (String name, Room spawnpoint){
        this.name = name; //must happen before spawn
        this.currentRoom = spawnpoint;
        roomHistory = new ArrayList<>();
        inventory = new Items();
        likeness = "";
        capacity = (double) 30;
        currentRoom.spawnPlayer(this); //spawn the player in the room
    }

    /**
     * Create a player with a certain name.
     * @param name the name of the player
     */
    public Player (String name, double capacity, Room spawnpoint){
        this.name = name; //must happen before spawn
        this.currentRoom = spawnpoint;
        roomHistory = new ArrayList<>();
        inventory = new Items();
        likeness = "";
        this.capacity = capacity;
        currentRoom.spawnPlayer(this); //spawn the player in the room
    }

    
    /**
     * Get the name of the player
     * @return the name of the player
     */
    public String getName(){
        return name;
    }

    /**
     * To avoid players having the same name, we add a number to repeat names
     * 
     * E.g., adding a "Niklas" twice would make the second one "Niklas(1)"
     */
    public void rename(){
        String nameStr;
        int count;
        if(name.endsWith(")")){ // we've done this before
            nameStr = name.substring(0,name.lastIndexOf("("));
            count = Integer.parseInt(name.substring(name.lastIndexOf("(")+1,name.lastIndexOf(")")));
        } else { //first time renaming
            nameStr = name;
            count = 0;
        }
        name = nameStr+"("+(count+1)+")";
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

    /**
     * Move the player to a new room.
     * @param newRoom the room to move to
     * @return true if the move is successful
     */
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
     * Whether the player can add an item to inventory
     * 
     * False if it's too heavy or it conflicts with an existing item
     * @param item the item to check
     * @return true if the player can acquire the item
     */
    public boolean canAcquire(Item item){ //could be item or null
        return inventory.canAcquire(item) && item.getWeight()<=canCarry();
    }
    
    /**
     * Add an item to the player's inventory.
     * Primarily for setup purposes
     * @param item the item to add to the player's inventory
     * @return true if the item was successfully acquired
     */
    public boolean acquireItem(Item item){
        return inventory.acquire(item); //handles null, bad match, no key
    }

    /**
     * Removes one count of the specified item from the player's inventory
     * and returns the item.
     * 
     * Returns null if null string, empty string, invalid key, or 0 count
     * @param itemName the name of the item to retrieve
     * @return the item retrieved from the player
     */
    public Item retrieveItem(String itemName){
        return inventory.retrieve(itemName); // handles null, empty name, 0 count, no key.
    }

    /**
     * Attempt to pick up an item from the current room
     * @param itemName the name of the item you want to pick up
     * @return true if successful
     */
    public boolean pickUp(String itemName){
        Item item = currentRoom.retrieveItem(itemName); // is null or item
        if (item == null){
            return false; //nothing ever retrieved so it's fine
        } else if (!canAcquire(item)){ //too heavy or bad match
            currentRoom.acquireItem(item); //drop it back into the room
            return false;
        } else { // light enough, and not a bad match
            return acquireItem(item); // still safe if no key
        }
    }

    /**
     * Try to put an item down in the current room
     * @param itemName the name of the item to put down
     * @return true if successful
     */
    public boolean setDown(String itemName){
        Item item = retrieveItem(itemName); // handles all the bad cases
        if (item == null){ //no item to speak of
            return false;
        } else if (!currentRoom.canAcquire(item)){ //I have the item but does it work for room
            acquireItem(item); //pick it back up then
            return false;
        } else {
            currentRoom.acquireItem(item); //room gets it
            return true;
        }
    }

    /**
     * The items in the players inventory
     * @return a display string of item info
     */
    public String getInventoryList(){
        return name+"'s Inventory:\n"+inventory.list();
    }
    
    /**
     * Give an item to another player
     * @param playerName the player to give the item to
     * @param itemName the name of item to give to the other player
     * @return true if successful
     */
    public boolean givePlayerItem(String playerName, String itemName){
        Player targetPlayer = currentRoom.getPlayer(playerName);
        Item item = retrieveItem(itemName);
        if (targetPlayer == null || item == null){
            return false;
        } else if (!targetPlayer.canAcquire(item)){ // too heavy etc.
            acquireItem(item); // take it back
            return false;
        } else {
            targetPlayer.acquireItem(item);
            return true;
        }
    }
}