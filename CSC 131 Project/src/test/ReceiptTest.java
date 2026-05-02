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
        Receipt receipt = new Receipt(user, new ArrayList<Item>(), 0.0);

        assertEquals(user, receipt.getOwner());
        assertNotNull(receipt.getItems());
        assertEquals(0, receipt.getItems().size());
        assertEquals(0.0, receipt.getTotal());
    }

    @Test
    void testAddItemAndItemSum() {
        User user = new User(1, "test@email.com", "pass123", "John");
        Receipt receipt = new Receipt(user, new ArrayList<Item>(), 0.0);

        receipt.add(new Item("Pen", 2, 1.5));
        receipt.add(new Item("Notebook", 1, 5.0));

        assertEquals(2, receipt.getItems().size());
        assertEquals(8.0, receipt.itemSum());
    }
}