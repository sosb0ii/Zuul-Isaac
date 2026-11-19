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
    
    // Replaced with register
    // /**
    //  * Add a player to the world, unassigned to any room
    //  * 
    //  * Recommended to specify the room.
    //  * 
    //  * @param player the player to add to the game
    //  * @return true if successful
    //  */
    // public boolean addPlayer(Player player){
    //     if (players.containsKey(player.getName())){
    //         System.out.println("A player named \""+player.getName()+"\" already exists");
    //         return false;
    //     }
    //     players.put(player.getName(),player);
    //     return true;
    // }
    
    // Not base methods, may be shortcut methods in the future
    // /**
    //  * Add a player to a specified room
    //  * @param player the player to add to the game
    //  * @param roomName the name of the room the player starts in
    //  * @return true if successful
    //  */
    // public boolean addPlayer(Player player, String roomName){
    //     if (!rooms.containsKey(roomName)){ //ensure room exists
    //         System.out.println("World does not contain a room named \""+roomName+"\"");
    //         return false; //quit if not
    //     } else if (!addPlayer(player)){ //add player to world and check unique name
    //         return false; //quit early if failed
    //     } // both checks pass: room exists and player in world
    //     return player.spawn(rooms.get(roomName)); // false if failed
    // }
    
    // /**
    //  * Add a player to a specified room
    //  * @param player the player to add to the game
    //  * @param room the room the player starts in
    //  * @return true if successful
    //  */
    // public boolean addPlayer(Player player, Room room){
    //     if (!addPlayer(player)){ //add player to world and check unique name
    //         return false; //quit early if failed
    //     } // both checks pass: room exists and player in world
    //     return player.spawn(room); // false if failed
    // }
    
    // public static World makeDefault(){
    //     // rooms in this world
    //     Room studyRooom = new Room("DEWITT-STUDY","player starts in this room");
    //     Room skylight = new Room("DEWITT-SKYLIGHT","carpeted space under a skylight");
    //     Room eastQuad = new Room("QUAD-EAST","open space with paths and flagpole");
    //     Room Scott = new Room("SCOTT_HALL","middle dorm on the South side");
    //     Room briansRoom = new Room("BRIAN'S_ROOM","it's the room Brian lives in");
    //     // players in this world
    //     Player Brian = new Player("Brian",35);
    //     Player Kelly = new Player("Kelly",12);
    //     // items in this world
    //     Item keyboard = new Item("keyboard","it goes to a computer",0.3);
    //     Item candy = new Item("candy","just for snax",0.1);
    //     // create the world object to return
    //     World thisWorld = new World();
    //     // put player in room
    //     thisWorld.addPlayer(Brian, "DEWITT-STUDY");
    //     thisWorld.addPlayer(Kelly, skylight);
    //     // link rooms
        
        
        
        
        
    //     return thisWorld;
    // }
    
    // public static boolean 
    
}