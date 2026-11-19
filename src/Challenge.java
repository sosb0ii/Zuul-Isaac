
/**
 * A quick challenge in the game.
 * Challenge
 */
public class Challenge {
    public static Player Chii = new Player("Chii",12);
    public static Item laptop = new Item("laptop","sleek thang",8);
    public static Item mouse1 = new Item("mouse","goes squeak",2);
    public static Item mouse2 = new Item("mouse","goes click",1);
    public static Item mouse3 = new Item("mouse","goes squeak", 2);
    public static Room plasmaLab = new Room("Plasma_Lab","So much fun!");
    public static Room vanderHall = new Room("Vanderwerf_Hallway","straight and long");
    public static Room mensRoom = new Room("Mens_Room","For taking care of business");

    public Challenge(){
        Chii.spawn(mensRoom);
        mensRoom.addExit("door", vanderHall);
        vanderHall.addExit("push_door", mensRoom);
        vanderHall.addExit("hall-door", plasmaLab);
        plasmaLab.addExit("door", vanderHall);
        System.out.println("Chii is in "+Chii.getCurrentRoom().getName());
        mensRoom.acquireItem(mouse1);
        plasmaLab.acquireItem(mouse2);
        vanderHall.acquireItem(mouse3);
        Chii.acquireItem(laptop);
        System.out.println("Challenge: get a squeaker into the plasma lab");

    }

    static public boolean didIWin(){
        return plasmaLab.hasMatchingItem(mouse1);
    }













}
