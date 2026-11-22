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

public class Room extends Entity
{
    private HashMap<String, Room> exits;        // stores exits of this room.
    private ArrayList<Player> players; // uniqueness will be enforced by World
    private final World theWorld;
    
    /**
     * Create a room named "name" in `World`.
     * Description remains empty until specified.
     * @param name the room's name
     * @param world the world this room is in
     */
    public Room(String name, World world){
        super(name);
        this.exits = new HashMap<>();
        this.players = new ArrayList<>();
        this.theWorld = world;
    }
    
    /**
     * Create a room named "name" in `World` with description "description".
     * @param name the room's name
     * @param description the room's description
     * @param world the world this room is in
     */
    public Room(String name, String description, World world){
        super(name,description);
        this.exits = new HashMap<>();
        this.players = new ArrayList<>();
        this.theWorld = world;
    }

    /**
     * @return the world this entity is in
     */
    public World getWorld(){
        return theWorld;
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
    public String displayItems(){
        return "Items in "+getName()+":\n"+getItemsList();
    }
    
    /**
     * What other players are in the room
     * @param activePlayer the player to exclude from the list
     * @return a string of the other players present in the room
     */
    public String listOtherPlayers(Player activePlayer){
        ArrayList<Player> otherPlayers = new ArrayList<>(players);
        otherPlayers.remove(activePlayer); // no error if activePlayer not present
        if (otherPlayers.isEmpty()){
            return "nobody";
        }
        String strOut = otherPlayers.get(0).getName();
        for(int i=1;i<otherPlayers.size();i++){
            if (otherPlayers.size() != 2){ //avoid adding comma for list of 2
                strOut += ", ";
            }
            if (i == otherPlayers.size()-1){
                strOut += " and ";
            }
            strOut += otherPlayers.get(i).getName();
        }
        return strOut;
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

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
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
}