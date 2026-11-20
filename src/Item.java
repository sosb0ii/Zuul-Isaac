
/**
 * A simple class of the items in the game.
 * An item can only be in one place at a certain time,
 * but more than one item can have the same name
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Item
{
    private String name;
    private String description;
    private double weight;
    // private String location;
    
    /**
     * Create the item by specifying its name, description, and weight
     * 
     * For example,
     * name = "moneybag"
     * description = "a pouch of coins"
     * weight = 3.2
     * 
     * @param name the name of the item
     * @param description the description of the item
     * @param weight the weight of the item
     * @param owner the player to have the item
     */
    public Item (String name, String description, double weight, Player owner){
        this.name = name;
        this.description = description;
        this.weight = weight;
        boolean added = owner.acquireItem(this);
        if (!added){ //if the item was not added to the player, it is not created
            System.out.println("Unable to add "+name+"to player: "+owner.getName());
        }

    }
    /**
     * Create the item by specifying its name, description, and weight
     * 
     * For example,
     * name = "moneybag"
     * description = "a pouch of coins"
     * weight = 3.2
     * 
     * @param name the name of the item
     * @param description the description of the item
     * @param weight the weight of the item
     * @param location the room to have the item
     */
    public Item (String name, String description, double weight, Room location){
        this.name = name;
        this.description = description;
        this.weight = weight;
        boolean added = location.acquireItem(this);
        if (!added){ //if the item was not added to the player, it is not created
            System.out.println("Unable to add "+name+"to room: "+location.getName());
        }

    }

    //mainly for quick testing
    /**
     * Create the item by specifying only its name.
     * Description is "" and weight is 0 by default
     * 
     * For example,
     * name = "moneybag"
     * description = ""
     * weight = 0
     * 
     * @param name the name of the item
     * @param owner the player to have the item
     */
    public Item (String name, Player owner){
        this.name = name;
        this.description = "";
        this.weight = 0;
        boolean added = owner.acquireItem(this);
        if (!added){
            System.out.println("Unable to add "+name+" to player: "+owner.getName());
        }
    }
    //mainly for quick testing
    /**
     * Create the item by specifying only its name.
     * Description is "" and weight is 0 by default
     * 
     * For example,
     * name = "moneybag"
     * description = ""
     * weight = 0
     * 
     * @param name the name of the item
     * @param location the room to have the item
     */
    public Item (String name, Room location){
        this.name = name;
        this.description = "";
        this.weight = 0;
        boolean added = location.acquireItem(this);
        if (!added){
            System.out.println("Unable to add "+name+" to room: "+location.getName());
        }
    }
    
    /**
     * Return the item's info
     * 
     * For example,
     * moneybag (3.2 lbs): A pouch of coins
     */
    public String getInfoString(){
        return name+" ("+weight+" lbs): " +description.toUpperCase();
    }
    
    /**
     * @return the name of the item
     */
    public String getName(){
        return name;
    }

    /**
     * @return the name of the item
     */
    public boolean isNamed(String nameQuery){
        return name.equals(nameQuery);
    }
    
    /**
     * Return the description of the item
     */
    public String getDescription(){
        return description;
    }
    
    /**
     * Return the weight of the item
     */
    public double getWeight(){
        return weight;
    }
    
    /**
     * Whether another item is equal
     * 
     * (items are used as keys, so equivalent fields means equivalent items)
     * @param other the item to comapare to
     * @returns true if the name, description, and weight are the same
     * @returns false if the name or description or weight differ
     */
    public boolean equals(Item other){
        if (!name.equals(other.getName())){ //diff name
            return false;
        } else if (!description.equals(other.getDescription())){ //diff description
            return false;
        } else if (weight != other.getWeight()){// diff weight
            return false;
        } else{ // all same
            return true;
        }
    }
    
    
}