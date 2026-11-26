import java.util.*;
/**
 * This class is used to build the world and keep world state information
 * 
 *
 * @Isaac Evans
 * @November 15
 */

import org.json.JSONArray;
import org.json.JSONObject;
public class World
{
    // Static field
    private static final String[] presetWorlds = {"assignment"};
    // fields
    private int numPlayers = 1;
    private Player mainCharacter;
    private HashMap<String, Room> rooms;
    private HashMap<String, Player> players;
    private String name;
    private ArrayList<String> messages;
    private String introduction;
    
    /**
     * Create an empty world
     */
    public World(String name){
        this.name = name;
        rooms = new HashMap<>();
        players = new HashMap<>();
        messages = new ArrayList<>();
    }

    // Static Methods

    public static String listPresetWorlds(){
        return " - "+String.join("\n + ",presetWorlds);
    }

    /**
     * Get a preset world
     * @return a World object, fully built with rooms, players, etc.
     */
    public static World fromPreset(String worldName){
        if(!hasPreset(worldName)) return null;
        else if("assignment".equals(worldName)){
            World scamWorld = new World("assignment");
            //Rooms
            Room attic = new Room("attic", "stuffy and dusty", scamWorld);
            Room crawlspace = new Room("crawlspace", "a dark, cramped hole of spiderwebs", scamWorld);
            Room bedroom = new Room("bedroom", "not sure whose", scamWorld);
            Room closet = new Room("closet", "full of clothes", scamWorld);
            Room kitchen = new Room("kitchen", "aromatic and cramped",scamWorld);
            Room bathroom = new Room("bathroom", "stinky and cramped", scamWorld);
            Room street = new Room("street", "cobbled and puddly", scamWorld);
            Room market = new Room("market", "for the buying and for the selling", scamWorld);
            // Players
            Player p1 = new Player("Larson", "the Larcenist", attic);
            Merchant merchant = new Merchant("Gilmore", "fat and rich", market,"");
            merchant.setCapacity(500); //so he can hold money, which is ofc 1 lb per coin
            // Set main character
            scamWorld.setMainCharacter(p1);
            // Add keys, not all useful
            Item atticKey = new Item("key", "a rusty key",1, crawlspace);
            atticKey.addAttribute("tetanus");
            Item chestKey = new Item("key", "a tarnished silver skeleton key", 1,attic);
            chestKey.addAttribute("cursed");
            Item gateKey = new Item("key", "a hefty steel key", 3, p1);
            // Add coins
            Item goldCoin = new Item("coin","made of gold",1,merchant);
            goldCoin.setWorth(1.);
            Item brassCoin = new Item("coin","made of brass",.2,closet);
            brassCoin.setWorth(0.1);
            // Add items to merchant
            Item cookie = new Item("magic-cookie","makes you buff",1.2,merchant);
            cookie.addAttribute("capacity -> 999");
            cookie.addAttribute("edible");
            cookie.setCost(12);
            merchant.createNitems(goldCoin, 299); //stuff his pockets
            merchant.createMerchandise(cookie);
            merchant.addValidCurrency(goldCoin);
            // distribute coinage
            closet.createNitems(brassCoin,12);
            scamWorld.stitchExits(attic,"floor-door","tetanus",bedroom,"ceiling-stairs"); // set the rusty key (from crawlspace) as the one that opens the first door
            scamWorld.stitchExits(attic, "dark-hole", crawlspace, "the-light");
            scamWorld.stitchExits(bedroom, "narrow-oak-doors", closet, "back");
            scamWorld.stitchExits(bedroom, "curtain", bathroom, "curtain");
            scamWorld.stitchExits(bedroom, "doorway", kitchen, "doorway");
            scamWorld.stitchExits(kitchen, "front-door", street, "front-door");
            scamWorld.stitchExits(street, "pavement", market, "pavement");
            return scamWorld;
        }

        else{
            throw new UnsupportedOperationException("Need to add code to build "+worldName+"!");
        }
    }

    public boolean winReached (Player Player){
         if(getName().equals("assignment")){
            return mainCharacter.getNetWorth()>= 100;
        }
        
        else {
            throw new UnsupportedOperationException("Need to add win condition for "+getName()+"! (temporary false is ok)");
        }
    }


    public static boolean hasPreset(String worldName){
        for(int i=0;i<presetWorlds.length;i++){
            if(presetWorlds[i].equals(worldName))return true;
        }
        return false;
    }

    public String getName(){
        return name;
    }

    public void setIntroduction(String introduction){
        this.introduction = introduction;
    }

    public String getIntroduction(){
        return (introduction == null) ? "" : introduction;
    }

    public int getNumPlayables(){
	int pCount = 0;
	for (Player player: players.values()){
	    if (!(player instanceof NPC)) pCount ++;
    }
	return pCount;
    }

    public void setMainCharacter (Player player){
	    mainCharacter = player;
    }

    public Player getMainCharacter () {
	    return mainCharacter;
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
     * The exits on either side have the same name
     * @param name the name of the exit
     * @param room1 Room 1
     * @param room2 Room 2
     * @return true if successful
     */
    public boolean stitchExits(String name, Room room1, Room room2){
        if (!rooms.containsValue(room1) || !rooms.containsValue(room2) || room1 == null || room2 == null){ // rooms not in world, or null
            System.out.println("room DNE");
            return false;
        } else if (name == null || name.isBlank()){ // invalid exit names
            System.out.println("bad exit name");
            return false;
        } else if (room1.hasExit(name) || room2.hasExit(name)){ // exits already exist
            System.out.println("exit name taken");
            return false;
        } // everything good
        room1.addExit(name, room2);
        room2.addExit(name, room1);
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
     * @param room2 Room 2
     * @param name2 The name of the exit from room 2's side 
     * @param key the attribute needed to unlock from either side
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
    

    public void queueMessage(String message){
        messages.add(message);
    }

    public String getMessages(){
        return messages.isEmpty() ? "" : String.join("\n",messages);
    }

    public void clearMessages(){
        messages.clear();
    }

    public boolean hasMessages(){
        return !messages.isEmpty();
    }

    public JSONObject toJSON(){
        JSONObject jsonOut = new JSONObject()
            .put("name",name)
            .put("introduction",introduction)
            .put("numPlayers",numPlayers)
            .put("mainCharacter",mainCharacter.getName());
        // skip players, instead generate them from Rooms
        JSONArray roomsJSON = new JSONArray();
        for(Room room:rooms.values()){
            roomsJSON.put(room.toJSON());
        }
        return jsonOut;
    }

}