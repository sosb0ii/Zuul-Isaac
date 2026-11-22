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
    private HashMap<Item, Integer> contents;
    
    /**
     * Create a collection of Items.
     */
    public Items(){
        contents = new HashMap<>();
    }
    
    /**
     * Whether there are no items in the collection
     * @return true if there are no items in the collection
     */
    public boolean isEmpty(){
        return contents.isEmpty();
    }

    /**
     * How many items are in the collection
     * @return the number of items in the collection
     */
    public int size(){
        return contents.size();
    }
    
    /**
     * Whether the collection contains an item with the specified name
     * @param itemName the name of the item to search for
     * @return true if an item with that name is found in the collection
     */
    public boolean contains(String itemName){
        for (Item item: contents.keySet()){
            if (item.getName().equals(itemName)){
                return true;
            }
        }
        return false;
    }

    /**
     * Whether the collection contains the item
     * @param item the item to search for
     * @return true if that item is found in the collection
     */
    public boolean contains(Item item){
        return contents.containsKey(item);
    }

    /**
     * How many of the specified item are in the collection
     * @param itemName the name of the item to search for
     * @return the number of the named item which are in the collection
     */
    private int getCount(String itemName){
        if (itemName == null){
            return 0;
        }
        for (Item item: contents.keySet()){
            if (item.isNamed(itemName)){
                return contents.get(item);
            }
        }
        return 0;
    }

    public Item retrieve(String itemName){
        // check null or invalid
        if (itemName == null || itemName.equals("")){ //invalid names
            return null;
        } else if (!contains(itemName)){ // no item with that name here
            return null;
        } else { // we have an item with that name
            Item item = getItem(itemName); //use the hidden private method
            subtractOne(item); // subtract 1 from the item count
            return item;
        }

    }

    public boolean canAcquire(Item item){
        if (item == null || item.getName() == null || item.isNamed("")){ //illegal conditions
            return false;
        }
        for (Item already: contents.keySet()){
            if (already.equals(item)){ //same item, can acquire
                return true;
            } else if (already.isNamed(item.getName())){ //different item, same name, cannot acquire (bad match)
                return false;
            }
        }
        return true; //if there's no match whatsoever then we're good to go
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
        if (!canAcquire(item)){ //check for null or bad match
            return false;
        } else { //can acquire!
            return addOne(item);
        }
    }

    public String list(){
        String strOut = "";
        Item item;
        int m;
        if (contents.isEmpty()){
            return "\t nothing :(";
        }
        for (Map.Entry<Item, Integer> eqItem: contents.entrySet()){
            item = eqItem.getKey();
            m = eqItem.getValue();
            if (m>0){ //otherwise don't print
                strOut += "x"+m+"\t"+item.getInfoString()+"\n";
                // strOut += "x"+m+"\t"+item.getName()+" ("+item.getWeight()*m;
                // strOut += " lbs): "+item.getDescription()+"\n";
            }
        } // e.g. x12   coins(1 lbs): Shiny gold guau
        return strOut.trim(); //rid trailing \n
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
        for (Item item:contents.keySet()){
            if (item.getName().equals(itemName)){
                return item.getWeight();
            }
        }
        return -1;
    }
    
    // This method is ulitmately to track players' carried weight
    public double getTotalWeight(){
        double totalWeight = (double) 0;      
        for (Map.Entry<Item, Integer> eqItem: contents.entrySet()){
            totalWeight += eqItem.getKey().getWeight()*eqItem.getValue(); //weight*num items
        }
        return totalWeight;
    }

    /**
     * Remove all elements with a value of 0
     */
    private void dropZeroKeys(){
        Iterator<Integer> counts = contents.values().iterator();
        while(counts.hasNext()){
            if (counts.next() == 0){
                counts.remove();
            }
        }
    }

    /**
     * Whether any items in the collection have the specified attribute
     * @param attribute the attribute to search for
     * @return true if such an item is found
     */
    public boolean hasItemWithAttribute(String attribute){
        for(Item item: contents.keySet()){
            if(item.hasAttribute(attribute)){
                return true;
            }
        }
        return false;
    }

    public String toString(){
        ArrayList<Item> itemArray = new ArrayList<>(contents.keySet());
        String strOut = itemArray.get(0).getName();
        for(int i=1;i<itemArray.size();i++){
            if (itemArray.size() != 2){ //avoid adding comma for list of 2
                strOut += ", ";
            }
            if (i == itemArray.size()-1){
                strOut += " and ";
            }
            strOut += itemArray.get(i).getName();
            strOut += "["+contents.get(itemArray.get(i))+"]";
        }
        return strOut;
    }

    /**
     * How many of the specified item are in the collection
     * @param item the item to count
     * @return the number of counts found (0 or positive int)
     */
    public int getCount(Item item){
        for(Item already: contents.keySet()){
            if (already.equals(item)){ // uses .equals() instead of hash
                return contents.get(already);
            }
        }
        return 0;
    }

    private boolean addOne(Item item){
        Item aItem; // items already in collection that we're iterating through
        int aCount; //counts of those items
        for(Map.Entry<Item,Integer> already: contents.entrySet()){
            aItem = already.getKey();
            aCount = already.getValue();
            if (aItem.getName().equals(item.getName())){ //names match
                if (aItem.equals(item)){ // uses .equals() instead of hash
                    contents.put(aItem,aCount+1); // forces it to use the existing key
                    return true;
                } else { // names match but not everything else, it's a bad match
                    return false;
                }
            } // this item did not match
        } // no equivalent items present
        contents.put(item,1); // make new key
        return true;
    }

    /**
     * Remove 1 from the count of the given item
     * @param item
     * @return
     */
    private boolean subtractOne(Item item){
        Item aItem;
        int aCount;
        for(Map.Entry<Item,Integer> already: contents.entrySet()){
            aItem = already.getKey();
            aCount = already.getValue();
            if (aItem.equals(item)){ // this is the one we're looking for
                contents.put(aItem,aCount-1);
                if(contents.get(aItem) < 1){ //there are a gazillion ways to do these two lines, this was the clearest to me
                    contents.remove(aItem);
                } // else item remains in hashmap
                return true;
            } // else keep searching
        } // else found no item to subtract
        return false;
    }

    /**
     * Get a reference to the item from its name.
     * @param itemName
     * @return
     */
    private Item getItem(String itemName){
        for (Item item:contents.keySet()){
            if (item.isNamed(itemName)){
                return item;
            }
        }
        return null;
    }
}