import java.util.*;
/**
 * A character in the Zuul game. Can represent a player or an NPC.
 *
 * @Isaac
 * @November 6 2026
 */
public class Player extends Entity
{
    private Room currentRoom;
    private ArrayList<Room> roomHistory;
    private double capacity;
    private String likeness;
    
    /**
     * Create a player with a certain name.
     * @param name the name of the player
     * @param spawnpoint the room where the player starts
     */
    public Player (String name, Room spawnpoint){
        super(name);
        this.currentRoom = spawnpoint;
        roomHistory = new ArrayList<>();
        likeness = "";
        capacity = (double) 30;
        currentRoom.spawnPlayer(this); //spawn the player in the room
    }

    /**
     * Create a player with a certain name.
     * @param name the name of the player
     * @param description a description of the player
     * @param spawnpoint the room where the player starts
     */
    public Player (String name, String description, Room spawnpoint){
        super(name,description);
        this.currentRoom = spawnpoint;
        roomHistory = new ArrayList<>();
        likeness = "";
        capacity = (double) 30;
        currentRoom.spawnPlayer(this); //spawn the player in the room
    }


    /**
     * Create a player with a certain name.
     * @param name the name of the player
     */
    public Player (String name, double capacity, Room spawnpoint){
        super(name);
        this.currentRoom = spawnpoint;
        roomHistory = new ArrayList<>();
        likeness = "";
        this.capacity = capacity;
        currentRoom.spawnPlayer(this); //spawn the player in the room
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
        return getTotalWeight();
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
        currentRoom = newRoom; //update the fieldsta
        // String debugString = "";
        // debugString += vacated ? "vacated" : "";
        // debugString += entered ? " enetered" : "";
        // debugString += logged ? " logged" : "";
        // System.out.println(debugString);
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
        Room newRoom = currentRoom.takeExit(exitName);
        if(newRoom == null){
            return false;
        }
        return goRoom(newRoom);
    }
    
    /**
     * Attempt to unlock the named exit with a specified
     * key, which is in the inventory
     * @param exitName the name of the exit to try to unlock
     * @param keyName the name of the key to try to use
     * @return true if successful
     */
    public boolean unlock(String exitName, String keyName){
        Item key = retrieveItem(keyName); // may be null
        if (key == null){
            return false;
        } if (currentRoom.unlock(exitName,key)){
            return true;
        } // else actual item, but failed to unlock
        acquireItem(key); //put it back in inventory
        return false;
    }
    
   
    /**
     * Return to the previous room.
     * @return true if the backtrack is successful
     */
    public boolean backtrack(){
        if (roomHistory.isEmpty()){
            return false;
        }
        // remove self
        currentRoom.removePlayer(this);
        // update room
        currentRoom = roomHistory.removeLast();
        // enter (add self)
        currentRoom.addPlayer(this);
        return true;
        // return goRoom(roomHistory.removeLast()); // essentially resets to the previous state 
    }

    /**
     * Whether the player can add an item to inventory
     * 
     * False if items conflict
     * @param item the item to check
     * @return true if the player can acquire the item
     */
    @Override 
    public boolean canAcquire(Item item){ //could be item or null
        return (super.canAcquire(item) || currentRoom.canAcquire(item));
    } //        inventory can acquire  OR  room can acquire             if I can't acquire it, I can still drop it into the room

    /**
     * Attempt to acquire item
     * 
     * If item is light enough and not a bad match, acquired normally.
     * 
     * If item is too heavy, drop it into room.
     * 
     * If item has a bad match, drop that match to room.
     * 
     * If room won't take it, transaction fails
     * 
     * 
     * @param item the item to attempt to acquire
     * @return true if a transaction is made
     */
    @Override
    public boolean acquireItem(Item item){
        if (item == null)return false;
        else if (item.getWeight() > canCarry()){ // too heavy! drop it into the room
            currentRoom.acquireItem(item);
            say(getName()+" dropped "+item.getName()+" because it was too heavy");
            say(item.getDescription());
            return false;
        } else if (super.acquireItem(item)){ // not too heavy, no bad match with inventory
            // THIS part is regular, successful acquisition!
            // say(getName()+" picks up "+item.getName()); //gets called for wide variety of things
            return true;
        }  // we know that item is a bad match with something in the inventory. First test to see if we can drop that bad match into room
        Item dropOut = retrieveBadMatch(item); // this item has been dropped out of the player's inventory.
        // before proceeding, we ensure it's safe
        if(dropOut == null || currentRoom.hasBadMatch(dropOut)){ //if there's going to be a problem actually dropping it into the room,
            // super.acququitireItem(dropOut); // put the dropOut back in
            return false; //didn't work
        } //now that we know it's safe, drop dropOut into the room
        currentRoom.acquireItem(dropOut);
        say(getName()+" takes "+item.getName());
        say("\tand drops "+dropOut.getName()+" on accident");
        // say(dropOut.getDescription());
        // now that we know it's safe, we keep doing so until all bad matches are dropped into room
        while(hasBadMatch(item)){ // for each badly matched item in inventory
            dropOut = retrieveBadMatch(item);
            currentRoom.acquireItem(dropOut);
        } // and now that we no longer have a bad match
        super.acquireItem(item);
        return true;
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
        }else if (acquireItem(item)){ //successfully acquired item
            return true;
        } else {   //was unsuccessful, must reabsorb into source
            currentRoom.acquireItem(item); //drop it back into the room
            return false;
        }
    }

    /**
     * This will call the other pickUp a bunch of times
     * @param itemName the name of the item you want to pick up
     * @param count the nubmer of items you want to pick up
     * @return the number of items you successfully picked up
     */
    public int pickUpCount(String itemName, int count){
        boolean moved;
        int moveCount = 0;
        for (int i = 0; i < count; i++){
            moved = pickUp(itemName);
            if (moved){
                moveCount ++;
            } else {
                break;
            }
        }
        return moveCount;
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
        } else if (currentRoom.acquireItem(item)){ // if successful, put it in room
            return true;
        } else { // otherwise,
            acquireItem(item); //pick it back up 
            return false;
        }
    }

    /**
     * setDown a certain number of items
     * @param itemName the name of the item you want to set down
     * @param count the nubmer of items you want to set down
     * @return the number of items you successfully set down
     */
    public int setDownCount(String itemName, int count){
        boolean moved;
        int moveCount = 0;
        for (int i = 0; i < count; i++){
            moved = setDown(itemName);
            if (moved){
                moveCount ++;
            } else {
                break;
            }
        }
        return moveCount;
    }



    /**
     * The items in the players inventory
     * @return a display string of item info
     */
    public String displayItems(){
        return getName()+"'s Inventory:\t\t ("+prettyDouble(getInventoryWeight())+"/"+prettyDouble(capacity)+" lbs)\n"+getItemsList();
    }
   
    /**
     * Give an item to another player
     * @param playerName the player to give the item to
     * @param itemName the name of item to give to the other player
     * @return true if successful
     */
    public boolean givePlayerItem(String playerName, String itemName){
        Player targetPlayer = currentRoom.getPlayer(playerName);
        if (targetPlayer == null){
            return false;
        }
        Item item = retrieveItem(itemName);
        if (item == null){
            return false;
        } else if (targetPlayer.acquireItem(item)){
            return true; // on success
        } else { //it failed
            acquireItem(item); //take it back
            return false;
        }
    }

    /**
     * give a player a number of items
     * @param playerName the name of the player you're giving items to
     * @param itemName the name of the item you want to give the player
     * @param count the nubmer of items you want to give the player
     * @return the number of items you successfully picked up
     */
    public int givePlayerItemCount(String playerName, String itemName, int count){
        boolean moved;
        int moveCount = 0;
        for (int i = 0; i < count; i++){
            moved = givePlayerItem(playerName, itemName);
            if (moved){
                moveCount ++;
            } else {
                break;
            }
        }
        return moveCount;
    }


    public boolean eat(String foodName){
        Item food;
        if (hasItem(foodName)){
            food = retrieveItem(foodName);
            if (food.hasAttribute("edible")){
                setCapacity(999);
                return true;
            } else {
                acquireItem(food); //back in inventory
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Remove trailing 0's and take out decimal from double
     * @param num the double to make pretty
     * @return a pretty string version of the double
     */
    public static String prettyDouble(double num){
        String strOut = String.valueOf(num);
        while(strOut.endsWith("0")){
            strOut = strOut.substring(0,strOut.length()-1); //remove last character
        } if (strOut.endsWith(".")){
            strOut = strOut.substring(0,strOut.length()-1);
        }
        return strOut;
    }

    public String talkTo(String playerName){
        Player targetPlayer = currentRoom.getPlayer(playerName);
        if (targetPlayer instanceof NPC npc) { // check type and store as type!
            return npc.getMonologue();
        }
        return null;
        }

    public boolean buy(String productName, String currencyName, int amount){
        Merchant merchant = currentRoom.getMerchant();
        if (merchant == null ){
            say("there is no merchant here");
            return false; // no such player, or not Merchant
        }
        System.out.println("found merchant");
        if (productName.equals("change")){ //player is just asking for change!
            merchant.requestProduct(productName);
            return true;
        }
        System.out.println("not looking for change");
        //having verified the merchant, we now verify the currency item
        Item currency;
        boolean transferred = true;
        System.out.println("trying to pay "+amount+" "+currencyName);
        int count = 1;
        for(int i=0; i < amount && transferred; i++){ //while loop checks the last transfer was successful
            currency = retrieveItem(currencyName); //take out the currency
            System.out.println("retrieved "+currencyName+count);
            if (currency == null) return false;
            transferred = merchant.acquireCurrency(currency); // add to transaction balance
            System.out.println("gave money to merchant");
            if (!transferred){
                acquireItem(currency); //pick it back up if transfer failed
                System.out.println("giving money to merchant actually failed");
                return false;
            } 
        }
        System.out.println("about to request product");
        Item product = merchant.requestProduct(productName); // request product. merchant handles the rest
        System.out.println("product request done");
        return acquireItem(product);
    }

    public void say(String message){
        currentRoom.tellTheWorld(message);
    }

    public void sayI(String message){
        currentRoom.tellTheWorld(getName()+": "+message);
    }
}