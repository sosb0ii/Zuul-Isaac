// import java.util.*;

/**
 * This is a parent class for Room and Player, since they share 
 * surprisingly many functionalities
 * 
 * @author  Isaac
 * @version 1
 */

public abstract class Entity 
{
    private String name;
    private String description;
    private Items items;        // the items in this entity.
    
    /**
     * Create an entity named "name"
     * Description remains empty until specified.
     * @param name the entity's name
     * @param world the world this entity is in
     */
    public Entity(String name){
        this.name = name;
        this.description = "";
        this.items = new Items();
    }
    
    /**
     * Create an entity named "name" in `World` with description "description".
     * @param name the entity's name
     * @param description the entity's description
     * @param world the world this entity is in
     */
    public Entity(String name, String description){
        this.name = name;
        this.description = description;
        this.items = new Items();
    }

    /**
     * Create `n` items like modelItem in entity
     * @param modelItem the item for all created items to match
     * @param n the number of items to add to the entity
     */
    public void createNitems(Item modelItem, int n){
        for (int i=0; i<n; i++){
            acquireItem(modelItem);
        }
    }

    /**
     * @return the name of the entity
     */
    public String getName(){
        return name;
    }

    
    /**
     * @return the description of the entity
     */
    public String getDescription(){
        return description;
    }

    /**
     * @param the description of the entity
     */
    public void setDescription(String description){
        this.description = description;
    }
    
    /**
     * Whether the entity has an item with the specified name
     * @param itemName: the name of the item
     * @return true if the entity has an item with the specified name
     */
    public boolean hasItem(String itemName){
        return items.contains(itemName);
    }

    /**
     * Whether the internal collection of Items contains one with the specified attribute
     * @param attribute the attribute to search for 
     * @return true if an item with the attribute is found
     */
    public boolean hasItemWithAttribute(String attribute){
        return items.hasItemWithAttribute(attribute);
    }
       
    public abstract String displayItems(); //abstract so that the header is customized

    public String getItemsList(){
        return items.list();
    }

    /**
     * Remove an item from the entity, and return it.
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
     * Add an item to the entity
     * @param item the item to add to the entity
     * @return true if the item was successfully acquired
     */
    public boolean acquireItem(Item item){
        return items.acquire(item); //handles null, bad match
    }

    /**
     * Get the weight of a specified item
     * 
     */
    public double getItemWeight(String itemName){
        return items.getItemWeight(itemName);
    }

    /**
     * How many items are in the entity
     * @return the number of items in the entity
     */
    public int getNumItems(){
        return items.size();
    }

    public boolean hasItem(Item item){
        return items.contains(item);
    }

    /**
     * To avoid entities having the same name, we add a number to repeated names
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

    /**
     * How total weight of items held by the entity
     * @return the total weight of all the items in the entity's collection
     */
    public double getTotalWeight(){
        return items.getTotalWeight();
    }

    /**
     * Retrieves an item that matches badly (same name, diff contents)
     * with item.
     * @param item the item to check against
     * @return the badly matched item
     */
    public Item retrieveBadMatch(Item item){
        return items.retrieveBadMatch(item);
    }

        /**
     * Whether an item has a bad match within the collection
     * (same name, different contents)
     * @param item the item to check against
     * @return true if such an item is found
     */
    public boolean hasBadMatch(Item item){
        return items.hasBadMatch(item);
    }

    public double getNetWorth(){
        return items.getNetWorth();
    }


    public String toString(){
        return getName();
    }

}