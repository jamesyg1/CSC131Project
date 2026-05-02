package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import model.Item;
import model.Receipt;
import model.User;

class ReceiptTest {

    @Test
    void testReceiptCreation() {
        User user = new User(1, "test@email.com", "pass123", "John");
        Receipt receipt = new Receipt(user, new ArrayList<Item>(), 0.0, "Test", "5/2/2026");

        assertNotNull(receipt.getReceiptID());
        assertEquals(user, receipt.getOwner());
        assertNotNull(receipt.getItems());
        assertEquals(0, receipt.getItems().size());
        assertEquals(0.0, receipt.getTotal());
        assertEquals("Test", receipt.getName());
        assertEquals("5/2/2026", receipt.getDate());
    }

    @Test
    void testAddItemAndItemSum() {
        User user = new User(1, "test@email.com", "pass123", "John");
        Receipt receipt = new Receipt(user, new ArrayList<Item>(), 0.0, "School Supplies", "5/3/2026");

        receipt.add(new Item("Pen", 2, 1.5));
        receipt.add(new Item("Notebook", 1, 5.0));

        assertEquals(2, receipt.getItems().size());
        assertEquals(8.0, receipt.itemSum());
    }
    
    @Test
    void testRemoveItemAndItemSum() {
        User user = new User(1, "test@email.com", "pass123", "John");
        Receipt receipt = new Receipt(user, new ArrayList<Item>(), 0.0, "School Supplies", "5/1/2026");
        Item notebook = new Item("Notebook", 1, 5.0);
        Item chair = new Item("Chair", 1, 50.0);
        
        receipt.add(notebook);
        receipt.add(new Item("Pen", 2, 1.5));
        receipt.remove(notebook);
        receipt.remove(chair);

        assertEquals(1, receipt.getItems().size());
        assertEquals(3.0, receipt.itemSum());
    }
}