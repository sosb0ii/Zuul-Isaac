import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.commons.annotation.Testable;

/**
 * A quick challenge in the game.
 * Challenge
 */
public class Challenge {
    public static World cheeseWorld = new World("Chii's_World");
    public static Room plasmaLab = new Room("Plasma_Lab","So much fun!", cheeseWorld);
    public static Room vanderHall = new Room("Vanderwerf_Hallway","straight and long", cheeseWorld);
    public static Room mensRoom = new Room("Mens_Room","A place of business", cheeseWorld);
    public static Room vanZ = new Room("VanZoeren","a veritable maze",cheeseWorld);
    public static Player Chii = new Player("Chii",12, mensRoom);
    public static Player Nik = new Player("Niklas", vanZ);
    public static Item laptop = new Item("laptop","sleek mac",8,Nik);
    public static Item mouse1 = new Item("mouse","goes squeak",2,mensRoom);
    public static Item mouse2 = new Item("mouse","goes click",1,plasmaLab);
    public static Item mouse3 = new Item("mouse","goes squeak", 2, vanderHall);

    public static World scamWorld = new World("Huckster's_Paradise");
    public static Room attic = new Room("attic", "stuffy and dusty", scamWorld);
    public static Room crawlspace = new Room("crawlspace", "a dark, cramped hole of spiderwebs", scamWorld);
    public static Room bedroom = new Room("bedroom", "no sure whose", scamWorld);
    public static Room closet = new Room("closet", "full of clothes", scamWorld);
    public static Room kitchen = new Room("kitchen", "aromatic and cramped",scamWorld);
    public static Room bathroom = new Room("bathroom", "stinky and cramped", scamWorld);
    public static Room street = new Room("street", "cobbled and puddly", scamWorld);
    public static Room market = new Room("market", "for the buying and for the selling", scamWorld);
    public static Player merchant = new Player("merchant", "fat and rich", market);
    public static Player p1 = new Player("Charlie", "the Charlatan", attic);
    public static Item atticKey = new Item("key", "a rusty key",.1, crawlspace);
    public static Item chestKey = new Item("key", "a tarnished silver skeleton key", .1,attic);
    public static Item gateKey = new Item("key", "a hefty steel key", .1, p1);
    public static Item goldCoin = new Item("coin",merchant);
    public static Item brassCoin = new Item("coin",closet);
    public static Item cookie = new Item("cookie",merchant);
    
    @BeforeEach
    public void setupScam(){
        scamWorld.stitchExits(attic,"floor-door","tetanus, probably",bedroom,"ceiling-stairs");
        atticKey.addAttribute("tetanus, probably");
        scamWorld.stitchExits(attic, "dark-hole", crawlspace, "the-light");
        scamWorld.stitchExits(bedroom, "narrow-oak-doors", closet, "back");
        scamWorld.stitchExits(bedroom, "curtain", bathroom, "curtain");
        scamWorld.stitchExits(bedroom, "doorway", kitchen, "doorway");
        scamWorld.stitchExits(kitchen, "front-door", street, "front-door");
        scamWorld.stitchExits(street, "pavement", market, "pavement");
        cookie.addAttribute("100 coin");
        cookie.addAttribute("edible");
        brassCoin.addAttribute("0.1 coin");
        goldCoin.addAttribute("1 coin");
    }

    @Test
    public void workScam(){


        
    }
    



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

    // @BeforeEach
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
