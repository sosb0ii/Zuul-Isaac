import static org.junit.jupiter.api.Assertions.*;

// import java.beans.Transient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Test Player, Room, and Item
 */

public class PlRoItTest {

    private Player Chii;
    private Item laptop;
    private Item mouse1;
    private Item mouse2;
    private Item mouse3;
    private Room plasmaLab;
    private Room vanderHall;
    private Room mensRoom;

    @BeforeEach
    public void setUp(){
        Chii = new Player("Chii",12);
        laptop = new Item("laptop","sleek thang",8);
        mouse1 = new Item("mouse","goes squeak",2);
        mouse2 = new Item("mouse","goes click",1);
        mouse3 = new Item("mouse","goes squeak", 2);
        plasmaLab = new Room("Plasma_Lab","So much fun!");
        vanderHall = new Room("Vanderwerf_Hallway","straight and long");
        mensRoom = new Room("Mens_Room","For taking care of business");
    }

    @Test
    public void giveChiiLaptopAndMouse() {
        assertTrue(Chii.acquireItem(laptop));
        assertTrue(Chii.acquireItem(mouse2));
    }

    @Test
    public void sameNameMustMatch(){
        assertTrue(Chii.acquireItem(mouse1));
        assertFalse(Chii.acquireItem(mouse2));
        assertTrue(Chii.acquireItem(mouse3));
    }

    @Test
    public void squeakerIntoLab(){
        assertTrue(mensRoom.acquireItem(mouse1));
        assertTrue(plasmaLab.acquireItem(mouse2));
        assertTrue(vanderHall.acquireItem(mouse3));
        assertTrue(Chii.spawn(mensRoom));
    }

    @Test
    public void spawnChii(){
        assertTrue(Chii.spawn(mensRoom));
        assertNotNull(Chii.getCurrentRoom());
    }

    @AfterEach
    public void tearDown(){
        System.out.println(Chii.getInventoryList());
    }
}