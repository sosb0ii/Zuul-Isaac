import java.util.*;
/**
 * This class is used to build the world and keep world state information
 * 
 *
 * @Isaac Evans
 * @November 15
 */
public class World
{
    private HashMap<String, Room> rooms;
    private HashMap<String, Player> players;
    
    /**
     * Create an empty world
     */
    public World(){
        rooms = new HashMap<>();
        players = new HashMap<>();
    }
    
    /**
     * Add a room to the world. Happens at room construction.
     * 
     * Return false if the room is registered
     * Rename room if it matches an existing name
     * @param room the room to add to the game
     * @return true if successful
     */
    public boolean addRoom(Room room){
        System.out.println("World.addRoom(Room room) is deprecated. Use World.addEntity(Room room) instead");
        if (rooms.containsValue(room)){ //room already in world. should never happen of course
            return false;
        }
        // isn't a pre-existing room, but could have the same name
        while(rooms.containsKey(room.getName())){
            room.rename();
        }// name is now unique
        rooms.put(room.getName(),room);
        return true;
    }

    /**
     * Register an unregistered player. Ultimately this happens at player creation.
     * 
     * If the player is registered, return false.
     * If the player has a matching name, rename the player and then register them
     * @param player the player to register
     * @return true if registration is successful
     */
    public boolean registerPlayer(Player player){
        if (players.containsValue(player)){ //already registered
            return false;
        }
        //isn't a registered player, but could have the same name
        while(players.containsKey(player.getName())){
            player.rename();
        }// name is now unique
        players.put(player.getName(),player);
        return true;
    }

    public boolean stitchExits(Room room1, String name1, Room room2, String name2){
        if (!rooms.containsValue(room1) || !rooms.containsValue(room2) || room1 == null || room2 == null){ // rooms not in world, or null
            return false;
        } else if (name1 == null || name2 == null || name1.isEmpty() || name2.isEmpty()){ // invalid exit names
            return false;
        } else if (room1.hasExit(name1) || room2.hasExit(name2)){ // exits already exist
            return false;
        } // everything good
        room1.addExit(name1, room2);
        room2.addExit(name2, room1);
        return true;
    }

    /**
     * Whether the named player is in the world
     * @param playerName the name of the player to search
     * @return true if the player is in the world
     */
    public boolean hasPlayer(String playerName){
        return players.containsKey(playerName);
    }
    
}