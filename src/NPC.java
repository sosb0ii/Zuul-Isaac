public class NPC extends Player
{

    private String monologue;

    public NPC(String name, Room fixedRoom){
        super(name, fixedRoom);
        this.monologue = "";
    }

    public NPC(String name, String description, Room fixedRoom, String monologue){
        super(name, description, fixedRoom);
        this.monologue = monologue;
    }

    public NPC(String name, double capacity, Room fixedRoom){
        super(name, capacity, fixedRoom);
        this.monologue = "";
    }

    public String getMonologue(){
        return monologue;
    }

    public void setMonologue(String text){
        monologue = text;
    }

}
