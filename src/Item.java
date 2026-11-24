import java.util.*;
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
    private HashSet<String> attributes;
    // private String location;
    
    /**
     * Create the item by specifying its name, description, and weight
     * 
     * For example,
     * name = "moneybag"
     * description = "a pouch of coins"
     * weight = 3.2
     * startingPoint = "bank"
     * 
     * @param name the name of the item
     * @param description the description of the item
     * @param weight the weight of the item
     * @param startingPoint the player/room to have the item
     */
    public Item (String name, String description, double weight, Entity startingPoint){
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.attributes = new HashSet<>();
        boolean added = startingPoint.acquireItem(this);
        if (!added){ //if the item was not added to the player, it is not created
            System.out.println("Unable to add "+name+"to player: "+startingPoint.getName());
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
    public Item (String name, Entity startingpoint){
        this.name = name;
        this.description = "";
        this.weight = 0;
        this.attributes = new HashSet<>();
        boolean added = startingpoint.acquireItem(this);
        if (!added){
            System.out.println("Unable to add "+name+" to: "+startingpoint.getName());
        }
    }
    // //mainly for quick testing
    // /**
    //  * Create the item by specifying only its name.
    //  * Description is "" and weight is 0 by default
    //  * 
    //  * For example,
    //  * name = "moneybag"
    //  * description = ""
    //  * weight = 0
    //  * 
    //  * @param name the name of the item
    //  * @param location the room to have the item
    //  */
    // public Item (String name, Room location){
    //     this.name = name;
    //     this.description = "";
    //     this.weight = 0;
    //     boolean added = location.acquireItem(this);
    //     if (!added){
    //         System.out.println("Unable to add "+name+" to room: "+location.getName());
    //     }
    // }
    
    /**
     * Return the item's info
     * 
     * For example,
     * moneybag (3.2 lbs): A pouch of coins
     */
    public String getInfoString(){
        String strOut = name+" ("+Player.prettyDouble(weight)+" lbs): " +description;
        if (!attributes.isEmpty()){
            strOut += "\n\t[" + String.join(", ",attributes)+"]";
        }
        return strOut;
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
            // System.out.println("different name");
            return false;
        } else if (!description.equals(other.getDescription())){ //diff description
            // System.out.println("different description");
            return false;
        } else if (weight != other.getWeight()){// diff weight
            // System.out.println("different weight");
            return false;
        } else if (attributeCount() != other.attributeCount()){// diff number of attributes
            // System.out.println("different attribute count");
            return false;
        } else{ // still need to check individual attributes
            for(String attribute: attributes){ // check one by one
                if(!other.hasAttribute(attribute)){
                    // System.out.println("lacked attribute"+attribute);
                    return false; //fale if not
                }
            } // else all matched
        }
        // System.out.println("Both items match perfectly");
        return true;
    }
    
    public boolean hasAttribute(String attribute){
        return attributes.contains(attribute.toLowerCase());
    }

    public void addAttribute(String attribute){
        attributes.add(attribute.toLowerCase());
    }

    public boolean removeAttribute(String attribute){
        return attributes.remove(attribute.toLowerCase());
    }

    public int attributeCount(){
        return attributes.size();
    }

    /**
     * the worth of a currency item, such as a coin
     * @return its worth in gold
     */
    public double getWorth(){
        for(String attribute: attributes){
            if(attribute.startsWith("worth ") && attribute.endsWith(" gold")){
                return Double.parseDouble(attribute.substring(6,attribute.length()-5)) ;
            }
        }
        return 0.0;
    }

    /**
     * set the worth of a currency item, such as a coin
     * @param worth its worth in gold
     */
    public void setWorth(double worth){
        for(String attribute: attributes){ // first clear any worth already assigned
            if(attribute.startsWith("worth ") && attribute.endsWith(" gold")){
                attributes.remove(attribute);
            }
        }
        addAttribute("worth "+String.valueOf(worth)+" gold");
    }

    /**
     * the cost of an item, such as a product
     * @return its cost in gold
     */
    public double getCost(){
        for(String attribute: attributes){
            if(attribute.startsWith("costs ") && attribute.endsWith(" gold")){
                return Double.parseDouble(attribute.substring(6,attribute.length()-5)) ;
            }
        }
        return 0.0;
    }

    /**
     * set the cost of an item, like a product
     * @param cost its cost in gold
     */
    public void setCost(double cost){
        for(String attribute: attributes){ // first clear any worth already assigned
            if(attribute.startsWith("costs ") && attribute.endsWith(" gold")){
                attributes.remove(attribute);
            }
        }
        addAttribute("costs "+String.valueOf(cost)+" gold");
    }
    
}
