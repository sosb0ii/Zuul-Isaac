import java.util.*;
/**
 * A wrapper class that represents a collection of Item objects.
 * The Items contained herein are individual and identifiable,
 * in the sense that they are objects residing in different memory locations.
 * However, they may be identical in the sense that every
 * field is the same (in which case we don't care to distinguish them, just count them).
 * @Isaac
 * @November 9
 */
public class Items
{
    private HashSet<Item> contents;
    
    /**
     * Create a collection of Items.
     */
    public Items(){
        contents = new HashSet<>();
    }
    
    /**
     * Whether there are no items in the collection
     * @return true if there are no items in the collection
     */
    public boolean isEmpty(){
        return contents.isEmpty();
    }
    
    /**
     * Whether the collection contains an item with the specified name
     * @param itemName the name of the item to search for
     * @return true if an item with that name is found in the collection
     */
    public boolean contains(String itemName){
        for (Item item: contents){
            if (item.getName().equals(itemName)){
                return true;
            }
        }
        return false;
    }

    /**
     * Remove an item with the specified name from the collection, and return it.
     * If there is more than one item with the specified name,
     * it will just remove/return one of them, and no particular order is guaranteed.
     * If there is no item with the              name, return null.
     * @param itemName the name of the item to retrieve
     * @return an item with the specified name
     */
    public Item retrieve(String itemName){ //should GUARANTEE the item removed is the item returned
        for (Item item: contents){
            if (item.getName().equals(itemName)){
                if(!contents.remove(item)){ //removes item. if false somehow, returns null
                    return null;
                }
                return item;
            }
        }
        return null;
    }
    
    /**
     * Add an item to the collection
     * 
     * If an item in the collection already has that name
     * but a different weight or description, this fails.
     * @param item the item to add to the collection
     * @returns true if the item was successfully acquired
     */
    public boolean acquire(Item item){
        for (Item already:contents){
            if(already.getName().equals(item.getName()) && !already.matches(item)){
                return false;
            }
        }
        return contents.add(item);
    }
    
    /**
     * List items contained herein.
     * Include the number of each item,
     * the name, the weight, and the description.
     * @returns a text list describing the items
     */
    public String list(){ // a bit of a doozy, innit
        String strOut = "";
        HashSet<Item> clone = (HashSet<Item>) contents.clone(); //had to cast it back into a HashSet
        // this allows us to identify where there are multiple matching items
        // by removing elements from the set as we iterate through
        Item thisItem; // this will be an item in clone;
        //all remaining items will be compared to it
        int count; // this will be how many items match thisItem (at least 1)
        while(!clone.isEmpty()){ //this runs once per set of matching items
            Iterator<Item> itemIter = clone.iterator(); //itemIter will get elements to check
            // matching and remove elements that have been counted from clone
            thisItem = itemIter.next(); // retrieve the first item
            // guaranteed to exist bc clone is not empty
            count = 1; // set the count to 1
            itemIter.remove(); //removes thisItem from clone
            while(itemIter.hasNext()){ //on the last one, this will be skipped
                if(thisItem.matches(itemIter.next())){ //found a match!
                    count ++;
                    itemIter.remove(); //never check it again. removed from clone
                }
            } //we now have the count of what matches thisItem
            strOut += "x"+count+"\t"+thisItem.getName()+" ("+thisItem.getWeight();
            strOut += "lbs): "+thisItem.getDescription()+"\n";
        }
        return strOut.trim(); //get rid of trailing \n
    }
    
    // this method is ultimately for the take command, to allow checking weight capacity
    // before Player actually sees that item
    /**
     * The weight of an item.
     * 
     * If multiple items share this name, use the first one found.
     * If no item has this weight, return -1.
     * 
     * @param itemName the name of the item to search
     * @return the weight of the item searched
     */
    public double getItemWeight(String itemName){
        for (Item item:contents){
            if (item.getName().equals(itemName)){
                return item.getWeight();
            }
        }
        return -1;
    }
    
    // This method is ulitmately to track players' carried weight
    public double getTotalWeight(){
        double totalWeight = (double) 0;      
        for (Item item:contents){
            totalWeight += item.getWeight();
        }
        return totalWeight;
    }
}