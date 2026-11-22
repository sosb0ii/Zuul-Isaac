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

    /**
     * Connect two rooms via exits.
     * They may have different names on 
     * different sides
     * @param room1 Room 1
     * @param name1 The name of the exit from room 1's side
     * @param room2 Room 2
     * @param name2 The name of the exit from room 2's side 
     * @return true if successful
     */
    public boolean stitchExits(Room room1, String name1, Room room2, String name2){
        if (!rooms.containsValue(room1) || !rooms.containsValue(room2) || room1 == null || room2 == null){ // rooms not in world, or null
            System.out.println("room DNE");
            return false;
        } else if (name1 == null || name2 == null || name1.isEmpty() || name2.isEmpty()){ // invalid exit names
            System.out.println("bad exit name");
            return false;
        } else if (room1.hasExit(name1) || room2.hasExit(name2)){ // exits already exist
            System.out.println("exit name taken");
            return false;
        } // everything good
        room1.addExit(name1, room2);
        room2.addExit(name2, room1);
        return true;
    }

    /**
     * Connect two rooms via exits.
     * They may have different names on 
     * different sides.
     * Room 1's side is locked with a key.
     * 
     * @param room1 Room 1
     * @param name1 The name of the exit from room 1's side
     * @param key the attribute needed to unlock from room 1's side
     * @param room2 Room 2
     * @param name2 The name of the exit from room 2's side 
     * @return true if successful
     */
    public boolean stitchExits(Room room1, String name1, String key, Room room2, String name2){
        if (!rooms.containsValue(room1) || !rooms.containsValue(room2) || room1 == null || room2 == null){ // rooms not in world, or null
            System.out.println("room DNE");
            return false;
        } else if (name1 == null || name2 == null || name1.isEmpty() || name2.isEmpty()){ // invalid exit names
            System.out.println("bad exit name");
            return false;
        } else if (room1.hasExit(name1) || room2.hasExit(name2)){ // exits already exist
            System.out.println("exit name taken");
            return false;
        } // everything good
        room1.addExit(name1, room2, key);
        room2.addExit(name2, room1);
        return true;
    }

    /**
     * Connect two rooms via exits.
     * They may have different names on 
     * different sides.
     * Both sides are locked with a key.
     * 
     * @param room1 Room 1
     * @param name1 The name of the exit from room 1's side
     * @param key the attribute needed to unlock from room 1's side
     * @param room2 Room 2
     * @param name2 The name of the exit from room 2's side 
     * @return true if successful
     */
    public boolean stitchExits(Room room1, String name1, Room room2, String name2, String key){
        if (!rooms.containsValue(room1) || !rooms.containsValue(room2) || room1 == null || room2 == null){ // rooms not in world, or null
            System.out.println("room DNE");
            return false;
        } else if (name1 == null || name2 == null || name1.isEmpty() || name2.isEmpty()){ // invalid exit names
            System.out.println("bad exit name");
            return false;
        } else if (room1.hasExit(name1) || room2.hasExit(name2)){ // exits already exist
            System.out.println("exit name taken");
            return false;
        } // everything good
        room1.addExit(name1, room2, key);
        room2.addExit(name2, room1, key);
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