package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Item;
import model.Receipt;
import model.User;

public class ReceiptService {

    private List<Receipt> receipts;
    private Random random;

    public ReceiptService() {
        receipts = new ArrayList<>();
        random = new Random();
    }

    private int generateReceiptID() {
        return 100000 + random.nextInt(900000);
    }

    public Receipt createReceipt(User owner, List<Item> items, String name, String date) {
        Receipt receipt = new Receipt(owner, items, 0.0, name, date);
        receipts.add(receipt);
        return receipt;
    }

    public void addItemToReceipt(Receipt receipt, Item item) {
        if (receipt != null && item != null) {
            receipt.add(item);
        }
    }

    public boolean removeItemFromReceipt(Receipt receipt, String itemName) {
        if (receipt == null || itemName == null) {
            return false;
        }

        for (Item item : receipt.getItems()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                receipt.remove(item);
                return true;
            }
        }
        return false;
    }

    public double getReceiptTotal(Receipt receipt) {
        if (receipt == null) {
            return 0.0;
        }
        return receipt.itemSum();
    }

    public List<Receipt> getAllReceipts() {
        return receipts;
    }
} 
