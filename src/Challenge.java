import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * A quick challenge in the game.
 * Challenge
 */
public class Challenge {
    public static Player Chii = new Player("Chii",12);
    public static Item laptop = new Item("laptop","sleek mac",8);
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

    @BeforeEach
    static public void setupChallenge(){
        Chii.spawn(mensRoom);
        mensRoom.addExit("bath-exit", vanderHall);
        vanderHall.addExit("push_door", mensRoom);
        vanderHall.addExit("hall-door", plasmaLab);
        plasmaLab.addExit("lab-exit", vanderHall);
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

    @Test
    public void testChallenge(){
        setupChallenge();
        assertFalse(didIWin());
        assertTrue(Chii.pickUp("mouse"));
        assertFalse(Chii.takeExit("hall-door"));
        assertTrue(Chii.takeExit("bath-exit"));
        assertTrue(Chii.setDown("mouse"));
        assertTrue(Chii.takeExit("hall-door"));
        assertTrue(Chii.pickUp("mouse"));
        assertTrue(Chii.takeExit("lab-exit"));
        assertFalse(Chii.setDown("mouse")); //false because same name diff descr. as mouse already in hallway
        System.out.println(Chii.getInventoryList());
        assertFalse(Chii.pickUp("mouse")); //also false
        Chii.pickUp("mouse"); //also false
        System.out.println(Chii.getInventoryList());
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
