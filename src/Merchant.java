public class Merchant extends NPC
{

    public Merchant(String name, Room shop){
        super(name, shop);
    }

    public Merchant(String name, String description, Room shop, String monologue){
        super(name, description, shop, monologue);
    }

    public Merchant(String name, double capacity, Room shop){
        super(name, capacity, shop);
    }

}
