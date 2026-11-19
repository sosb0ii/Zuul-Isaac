import java.util.*;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Room 
{
    private String name;
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private Items items;        // the items in this room.
    private HashSet<Player> players;
    
    //initialize fields. Call in every constructor.
    private void initFields(){
        exits = new HashMap<>();
        items = new Items();
        players = new HashSet<>();
    }

    /**
     * Create a room named "name."
     * Description remains empty until specified.
     * @param name the room's name
     */
    public Room(String name){
        this.name = name;
        this.description = "";
        initFields();
    }
    
    /**
     * Create a room named "name" with description "description".
     * @param name the room's name
     * @param description the room's description
     */
    public Room(String name, String description){
        this.name = name;
        this.description = description;
        initFields();
    }
    
    /**
     * @return the name of the room
     */
    public String getName(){
        return name;
    }
    
    /**
     * @return the description of the room
     */
    public String getDescription(){
        return description;
    }
    
    /**
     * Whether the room has an item with the specified name
     * @param itemName: the name of the item
     * @return true if the room has an item with the specified name
     */
    public boolean hasItem(String itemName){
        return items.contains(itemName);
    }
    
    /**
     * Whether a room contains the player with the specified name
     * @param playerName the name of the player to search
     * @return true if a player with that name is in the room
     */
    public boolean hasPlayer(String playerName){
        for (Player player: players){
            if (player.getName().equals(playerName)){
                return true;
            }
        }
        return false;
    }

    public Player getPlayer(String playerName){
        for (Player player: players){
            if (player.getName().equals(playerName)){
                return player;
            }
        }
        return null;
    }



    /**
     * What items are in the room
     * @return a string of items contained in the room, and their information.
     */
    public String listItems(){
        return items.list();
    }
    
    /**
     * What players are in the room
     * @return a string of the players present in the room
     */
    public String listPlayers(){
        if (players.isEmpty()){
            return null;
        }
        String strOut = players.get(0).getName();
        for(int i=1;i<players.size();i++){
            if (players.size() != 2){ //avoid adding comma for list of 2
                strOut += ", ";
            }
            if (i == players.size()-1){
                strOut += " and ";
            }
            strOut += players.get(i).getName();
        }
        return strOut;
    }

    /**
     * Whether the room has an exit with the specified name
     * @param exitName: the name of the exit
     * @return true if the room has an exit with the specified name
     */
    public boolean hasExit(String exitName){
        return exits.containsKey(exitName);
    }

    /**
     * Define an exit from this room.
     * @param exitName The name of the exit.
     * @param nextRoom  The room to which the exit leads.
     * @return true if successful
     */
    public boolean addExit(String exitName, Room nextRoom) 
    {
        if (exits.containsKey(exitName)){
            return false; //failed to add exit
        }        
        exits.put(exitName, nextRoom);
        return true;
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exits direction.
     * @return The room in the specified direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
       
    /**
     * Remove an item from the room, and return it.
     * 
     * If there is more than one item with the specified name,
     * it will just remove/return one of them, and no particular order is guaranteed.
     * If there is no item with the specified name, return null.
     * @param itemName the name of the item to retrieve
     * @return an item with the specified name
     */
    public Item retrieveItem(String itemName){
        return items.retrieve(itemName);    
    }
    
    /**
     * Add an item to the room
     * @param item the item to add to the room
     * @return true if the item was successfully acquired
     */
    public boolean acquireItem(Item item){
        return items.acquire(item);
    }

    /**
     * Whether an item can be acquired by the room
     * @param item the item to check acquisability
     * @return true if the item can be acquired
     */
    public boolean willAcquireItem(Item item){
        return items.willAcquire(item);
    }
    
    /**
     * Remove a player from the room.
     * 
     * If the player is not in the room or the removal is unsuccessful, returns false.
     * @param player the player to remove from the room
     * @return true if the player is in the room and then is successsfully removed
     */
    public boolean removePlayer(Player player){
        return players.remove(player); //returns the boolean bc I used the element, not its index
    }
    
    /**
     * Add a player to the room
     * 
     * @param newPlayer the player to add to the room
     * @return true if the player was added successfully
     */
    public boolean addPlayer(Player newPlayer){
        for (Player already:players){
            if (already.getName().equals(newPlayer.getName())){
                // should not be possible in one World
                return false;
            }
        }
        return players.add(newPlayer); //ArrayList.add()
    }

    /**
     * Get the weight of a specified item
     * 
     */
    public double getItemWeight(String itemName){
        return items.getItemWeight(itemName);
    }

    /**
     * How many items are in the room
     * @return the number of items in the room
     */
    public int getNumItems(){
        return items.size();
    }

    public boolean hasMatchingItem(Item item){
        return items.containsMatching(item);
    }
}

