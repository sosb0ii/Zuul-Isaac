import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ItemTest {

    @Test
    public void testCreateItem() {
        Item bag = new Item("moneybag", "a pouch of coins", 3.2);
        assertNotNull(bag);
    }
}