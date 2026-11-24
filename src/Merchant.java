/**
 * 
 * Merchant is an NPC that sells you stuff.
 * Some of the implementation (requestProduct) is clunky,
 * because it would fit better in a Shop subclass of Room.
 * But this works alright.
 */

public class Merchant extends NPC
{

    private Items validCurrencies;
    private Items forSale; // will have attribute that states cost
    private Items transaction;

    public Merchant(String name, Room shop){
        super(name, shop);
        validCurrencies = new Items();
        forSale = new Items();
        transaction = new Items();
    }

    public Merchant(String name, String description, Room shop, String monologue){
        super(name, description, shop, monologue);
        validCurrencies = new Items();
        forSale = new Items();
        transaction = new Items();
    }

    public Merchant(String name, double capacity, Room shop){
        super(name, capacity, shop);
        validCurrencies = new Items();
        forSale = new Items();
        transaction = new Items();
    }

    public void addValidCurrency(Item newCurrency){ //useful for defining a merchant
        if (validCurrencies.getCount(newCurrency) == 0){ //make sure it's nto already there
            validCurrencies.acquire(newCurrency);
        }
    }

    public void removeValidCurrency(Item newCurrency){
        validCurrencies.retrieve(newCurrency.getName());
    }

    public double getTransactionBalance(){
        return transaction.getNetWorth();
    }

    public boolean acquireCurrency(Item currency){
        if (!validCurrencies.contains(currency)){// not a valid currency
            sayI("that's not real money!");
            say("accepted currencies are:\n"+validCurrencies.list()); //shows x1 but that's fine with me for now
            sayI("however, trinkets are always accepted, hehe");
            return false;
        }
        // now that it is a valid currency, add it to the transaction
        return transaction.acquire(currency);
    }

    public Item requestProduct(String productName){
        if (productName.equals("change")){ // player is asking for change!
            clearTheCounter();
            say(getName()+" sweeps everything on the counter onto the floor");
            return null;
        }
        if (!forSale.contains(productName)){
            sayI("we don't sell \""+productName+"\" here!\n\twe have the following for sale:\n"+forSale.list());
            return null;
        } //now it must be a valid product
        Item product = forSale.retrieve(productName);
        if (transaction.getNetWorth() < product.getCost()){
            sayI("that's not enough, bud\n\t"+productName+" costs "+product.getCost());
            forSale.acquire(product); // take the product back
            return null;
        } else { //there's enough money on the counter, start picking it up. note the merchant may take too much charge
            say("there is now "+transaction.getNetWorth()+" gold worth of currency on the counter");
            say(productName+" costs "+product.getCost());
            double gained = 0;
            Item charge;
            int attempts = 0; // in case something goes wrong with what the merchant can't pick up (obscure free money glitch??)
            System.out.println("about to loop pickups");
            while(gained < product.getCost() && attempts < 10000){ //keep picking up money (at random) until satisfied
                charge = transaction.retrieveAny(); // grab something from the counter
                if(!acquireItem(charge)){ //pocket the item. if you can't,
                    transaction.acquire(charge); //put it back
                    // System.out.println("could not get item "+charge.getName()+charge.getDescription()+" from transaction");
                } else {
                    gained = charge.getWorth();
                }
                attempts ++;
            } //enough money given & merchant has had their attempt to pick it up (99.9% successful, just edge case fails)
            System.out.println("transaction all but done");
            if (gained < product.getCost()){ // merchant can't pick up all the money for some reason
                sayI("You sly dog, you tricked me!"); // If you manage this then I'll let you take the product anyway
            } else {
                say(getName()+" gives you "+productName);
            }
            return product;
        }
    }

    public void createMerchandise(Item product){
        forSale.acquire(product);
    }
    public void createMerchandise(Item product, int N){
        for(int i=1; i<N; i++){
            forSale.acquire(product);
        }
    }

    @Override 
    public String getMonologue(){
        return "Hey there! What're you looking to buy?\nI've got\n"+forSale.list();
    }

    public void clearTheCounter(){ //counter is the "transaction"
        Item change;
        while(!transaction.isEmpty()){ //for every item on the counter
            change = transaction.retrieveAny(); // remove the item
            if(!getCurrentRoom().acquireItem(change)){ //drop the item. if you can't,
                transaction.acquire(change); //take it back
            }
        }
    }
    @Override 
    public String getNPCid(){
        return "[NPC-merchant]";
    }


} // in future, can have Shop extends Room where transaction collection belongs to Shop -- essentially what's "on the counter."
