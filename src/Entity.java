import java.util.*;

/**
 * This is a parent class fro Room and Player, since they share 
 * surprisingly many functionalities
 * 
 * @author  Isaac
 * @version 1
 */

public class Entity 
{
    private String name;
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private Items items;        // the items in this room.
    private ArrayList<Player> players; // uniqueness will be enforced by World
    private final World theWorld; //must be specified on creation
    
    /**
     * Create a room named "name" in `World`.
     * Description remains empty until specified.
     * @param name the room's name
     * @param world the world this room is in
     */
    public Room(String name, World world){
        this.name = name;
        this.description = "";
        this.exits = new HashMap<>();
        this.items = new Items();
        this.players = new ArrayList<>();
        this.theWorld = world;
        theWorld.addRoom(this); //add the room to the world
    }
    
    /**
     * Create a room named "name" in `World` with description "description".
     * @param name the room's name
     * @param description the room's description
     * @param world the world this room is in
     */
    public Room(String name, String description, World world){
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new Items();
        this.players = new ArrayList<>();
        this.theWorld = world;
        theWorld.addRoom(this); //add the room to the world
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
        return "Items in "+name+":\n"+items.list();
    }
    
    /**
     * What players are in the room
     * @return a string of the players present in the room
     */
    public String listPlayers(){
        if (players.isEmpty()){
            return "nobody";
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

    // /**
    //  * Return a string describing the room's exits, for example
    //  * "Exits: north west".
    //  * @return Details of the room's exits.
    //  */
    // private String getExitString()
    // {
    //     String returnString = "Exits:";
    //     Set<String> keys = exits.keySet();
    //     for(String exit : keys) {
    //         returnString += " " + exit;
    //     }
    //     return returnString;
    // }

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

    public String listExits(){
        // return String.join(", ",exits.keySet());
        if (exits.isEmpty()){
            return "no exit";
        }
        ArrayList<String> exitList = new ArrayList<>(exits.keySet());
        String strOut = exitList.get(0);
        for(int i=1;i<exitList.size();i++){
            if (exitList.size() != 2){ //avoid adding comma for list of 2
                strOut += ", ";
            }
            if (i == exitList.size()-1){
                strOut += " or ";
            }
            strOut += exitList.get(i);
        }
        return strOut;
    }
       
    /**
     * Remove an item from the room, and return it.
     * 
     * If there is no item with the specified name, return null.
     * @param itemName the name of the item to retrieve
     * @return an item with the specified name
     */
    public Item retrieveItem(String itemName){
        return items.retrieve(itemName);    
    }

    public boolean canAcquire(Item item){
        return items.canAcquire(item); //no special rules
    }
    
    /**
     * Add an item to the room
     * @param item the item to add to the room
     * @return true if the item was successfully acquired
     */
    public boolean acquireItem(Item item){
        return items.acquire(item); //handles null, bad match
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

    public boolean spawnPlayer(Player newPlayer){
        boolean registered = theWorld.registerPlayer(newPlayer); //should always be true due to renaming tactic
        boolean spawned = addPlayer(newPlayer);
        return (registered && spawned);
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

    public boolean hasItem(Item item){
        return items.contains(item);
    }

    /**
     * To avoid rooms having the same name, we add a number to repeat names
     * 
     * E.g., adding a "bathroom" twice would make the second one "bathroom(1)"
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

    


}