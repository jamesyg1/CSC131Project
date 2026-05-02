package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import service.ReceiptService;

import model.Item;
import model.User;

class ReceiptServiceTest {

    @Test
    void testNullReceipt() {
        User user = new User(1, "test@email.com", "pass123", "John");
        ReceiptService list = new ReceiptService();
        
        list.createReceipt(user, new ArrayList<>(), "Test", "5/4/2026");
        
        assertEquals(0.0, list.getReceiptTotal(null));
    }
	
    @Test
    void testReceiptListCreation() {
        User user = new User(1, "test@email.com", "pass123", "John");
        ReceiptService list = new ReceiptService();
        
        list.createReceipt(user, new ArrayList<>(), "Test1", "5/4/2026");
        list.createReceipt(user, new ArrayList<>(), "Test2", "5/5/2026");

        assertEquals(2, list.getAllReceipts().size());
    }
    
    @Test
    void testAddItemToReceiptInListAndSum() {
        User user = new User(1, "test@email.com", "pass123", "John");
        ReceiptService list = new ReceiptService();
        
        list.addItemToReceipt(list.createReceipt(user, new ArrayList<>(), "Test1", "5/4/2026"), new Item("Pen", 2, 1.5));
        list.addItemToReceipt(list.getAllReceipts().get(0), new Item("Notebook", 1, 5.0));

        list.addItemToReceipt(null, new Item("Hotdog", 3, 3.99));
        list.addItemToReceipt(list.getAllReceipts().get(0), null);

        assertEquals(1, list.getAllReceipts().size());
        assertEquals(2, list.getAllReceipts().get(0).getItems().size());
        assertEquals(8, list.getReceiptTotal(list.getAllReceipts().get(0)));
    }
    
    @Test
    void testRemoveItemFromReceiptInListAndSum() {
        User user = new User(1, "test@email.com", "pass123", "John");
        ReceiptService list = new ReceiptService();
        
        list.addItemToReceipt(list.createReceipt(user, new ArrayList<>(), "Food", "5/4/2026"), new Item("Hotdog", 3, 3.99));
        list.addItemToReceipt(list.createReceipt(user, new ArrayList<>(), "School Supplies", "5/4/2026"), new Item("Pen", 2, 1.5));
        list.addItemToReceipt(list.getAllReceipts().get(1), new Item("Notebook", 1, 5.0));
        list.removeItemFromReceipt(list.getAllReceipts().get(1), "Pen");
        
        assertEquals(false, list.removeItemFromReceipt(list.getAllReceipts().get(1), "HotDog"));
        assertEquals(false, list.removeItemFromReceipt(null, "Notebook"));
        assertEquals(false, list.removeItemFromReceipt(list.getAllReceipts().get(0), null));
        
        assertEquals(2, list.getAllReceipts().size());
        assertEquals(1, list.getAllReceipts().get(0).getItems().size());
        assertEquals(5, list.getReceiptTotal(list.getAllReceipts().get(1)));
    }    

}