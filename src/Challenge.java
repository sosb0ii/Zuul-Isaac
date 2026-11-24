import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * A quick challenge in the game.
 * Challenge
 */
public class Challenge {
    public static World cheeseWorld = new World();
    public static Room plasmaLab = new Room("Plasma_Lab","So much fun!", cheeseWorld);
    public static Room vanderHall = new Room("Vanderwerf_Hallway","straight and long", cheeseWorld);
    public static Room mensRoom = new Room("Mens_Room","A place of business", cheeseWorld);
    public static Room vanZ = new Room("VanZoeren","a veritable maze",cheeseWorld);
    public static Player Chii = new Player("Chii",12, mensRoom);
    public static Player Nik = new Player("Niklas", vanZ);
    public static Item laptop = new Item("laptop","sleek mac",8,Nik);
    public static Item laptop = new Item("laptop","sleek mac",8,Chii);
    public static Item mouse1 = new Item("mouse","goes squeak",2,mensRoom);
    public static Item mouse2 = new Item("mouse","goes click",1,plasmaLab);
    public static Item mouse3 = new Item("mouse","goes squeak", 2, vanderHall);

    public Challenge(){
        // Chii.spawn(mensRoom);
        // mensRoom.addExit("door", vanderHall);
        // vanderHall.addExit("push_door", mensRoom);
        // vanderHall.addExit("hall-door", plasmaLab);
        // plasmaLab.addExit("door", vanderHall);
        // System.out.println("Chii is in "+Chii.getCurrentRoom().getName());
        // mensRoom.acquireItem(mouse1);
        // plasmaLab.acquireItem(mouse2);
        // vanderHall.acquireItem(mouse3);
        // Chii.acquireItem(laptop);
        // System.out.println("Challenge: get a squeaker into the plasma lab");

    }

    @BeforeEach
    static public void setupChallenge(){
        // Chii.spawn(mensRoom);
        mensRoom.addExit("bath-exit", vanderHall);
        vanderHall.addExit("push_door", mensRoom);
        vanderHall.addExit("hall-door", plasmaLab);
        plasmaLab.addExit("lab-exit", vanderHall);
        System.out.println("Chii is in "+Chii.getCurrentRoom().getName());
        //mensRoom.acquireItem(mouse1);
        //plasmaLab.acquireItem(mouse2);
        //vanderHall.acquireItem(mouse3);
        //Chii.acquireItem(laptop);
        System.out.println("Challenge: get a squeaker into the plasma lab");
    }

    static public boolean didIWin(){
        return plasmaLab.hasItem(mouse1);
    }

    static public void whatsUp(){
        System.out.println("\nChii is in "+Chii.getCurrentRoom().getName());
        System.out.println(Chii.displayItems());
        System.out.println(Chii.getCurrentRoom().displayItems());
    }

    @Test
    public void testChallenge(){
        setupChallenge();
        assertTrue(cheeseWorld.stitchExits(vanderHall, "east-hall", vanZ, "west-hall"));
        assertFalse(didIWin());
        whatsUp();
        System.out.println(mensRoom.displayItems()+"\n"+mensRoom.listPlayers());
        assertTrue(Chii.pickUp("mouse"));
        assertFalse(Chii.takeExit("hall-door"));
        assertTrue(Chii.takeExit("bath-exit"));
        assertTrue(Chii.setDown("mouse"));
        assertTrue(Chii.takeExit("hall-door"));
        whatsUp();
        assertTrue(Chii.pickUp("mouse"));
        assertTrue(Chii.takeExit("lab-exit"));
        assertFalse(Chii.setDown("mouse")); //false because same name diff descr. as mouse already in hallway
        System.out.println(Chii.displayItems());
        whatsUp();
        assertFalse(Chii.pickUp("mouse")); //also false
        Chii.pickUp("mouse"); //also false
        System.out.println(Chii.displayItems());
        assertTrue(Chii.takeExit("push_door"));
        assertTrue(Chii.setDown("mouse")); //no mouse in here anymore
        assertTrue(Chii.takeExit("bath-exit"));
        assertTrue(Chii.pickUp("mouse")); //no conflicting mouse in inventory now
        assertFalse(didIWin());
        assertTrue(Chii.takeExit("hall-door"));
        assertFalse(didIWin());
        assertTrue(Chii.setDown("mouse"));
        assertTrue(didIWin());
    }
}
