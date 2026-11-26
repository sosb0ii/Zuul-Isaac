import org.json.JSONObject;

/**
 * Exit
 * 
 * I PROMISE this will be a slim class. Pinky promise.
 * It's just a glorified hashMap. trust.
 */


public class Exit {
    private String exitName;
    private Room destination;
    private boolean locked;
    private String key;

    /**
     * Constructor for an unlocked exit
     * @param exitName The name of the exit
     * @param destination The Room the exit leads to
     */
    public Exit(String exitName, Room destination){
        this.exitName = exitName;
        this.destination = destination;
        this.locked = false;
    }

    /**
     * Constructor for a locked exit
     * @param exitName The name of the exit
     * @param destination The Room the exit leads to
     */
    public Exit(String exitName, Room destination, String key){
        this.exitName = exitName;
        this.destination = destination;
        this.locked = true;
        this.key = key;
    }

    public String getName(){
        return exitName;
    }

    /**
     * Get the destination of the exit.
     * 
     * Only allowed if the door is unlocked!
     * @return the Room destination
     */
    public Room getRoom(){
        if (!locked){
            return destination;
        }
        return null;
    }

    public boolean unlock(Item item){
        if(!locked || item == null){
            return false; //already unlocked, or item null
        }
        if(item.hasAttribute(key)){
            locked = false;
            return true;
        } else {
            return false;
        }
    }

    public String toString(){
        return exitName;
    }

    public boolean isLocked(){
        return locked;
    }

    public JSONObject toJSON(){
        JSONObject jsonOut = new JSONObject();
        jsonOut.put("name",exitName);
        // Constructor from the JSON must know World in order to retrieve Rooms via their names
        jsonOut.put("destination", destination.getName());
        jsonOut.put("locked",locked);
        jsonOut.put("key",key);
        return jsonOut;
    }
}
