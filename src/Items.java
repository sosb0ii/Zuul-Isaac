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


    /**
     * Remove the item with the specified name from the collection (there should only be one!), and return it.
     * If there is no item with the specified name, return null.
     * @param itemName the name of the item to retrieve
     * @return an item with the specified name
     */
    public Item retrieve(String itemName){ //should GUARANTEE the item removed is the item returned
        if (itemName == null || itemName.equals("")){ //invalid names
            return null;
        }
        int m = getCount(itemName); // known the multiplicity of the item
        if(m>0){ //there's something to retrieve
            for(Item item:contents.keySet()){
                if(item.isNamed(itemName)){
                    if (contents.get(item) != m){ //something is wrong
                        throw new IllegalStateException("Item multiplicity is inconsistent, there is a bug");
                    }
                    contents.put(item, m-1); //decrease the multiplicity by 1
                    dropZeroKeys(); //if now 0, remove it
                    return item; //successfully return item 
                }
            } // the for loop should not finish!
            throw new IllegalStateException("Positive multiplicity but no item found, there is a bug");
        }
        return null; //m=0, return nothing
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
            int m = contents.getOrDefault(item,0); // gets value or 0 if key DNE
            contents.put(item, m+1);
            return true;
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
                strOut += "x"+m+"\t"+item.getName()+" ("+item.getWeight()*m;
                strOut += " lbs): "+item.getDescription()+"\n";
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
}