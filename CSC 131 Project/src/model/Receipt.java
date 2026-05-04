package model;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Receipt {
	private int receiptID;
	private User owner;
	private List<Item> items;
	private double total;
	private String date;
	private String name;
	
	public Receipt(User owner, List<Item> items, double total, String name, String date) {
		this.owner = owner;
		this.items = new ArrayList<>(items);
		this.total = total;
		this.name = name;
		this.date = date;
		this.receiptID = new Random().nextInt(900000) + 100000;
	}
	public void add(Item item) {
		this.items.add(item);
	}
	public void remove(Item item) {
		for(int i=0; i<this.items.size(); i++) {
			if(this.items.get(i) == item) {
				this.items.remove(i);
				break;
			}
		}
	}
	public int getReceiptID() {
		return receiptID;
	}
	// DatabaseService connection to the Receipt
	public void setReceiptID(int id) {
		this.receiptID = id;
	}
	public User getOwner() {
		return owner;
	}
	public List<Item> getItems(){
		return items;
	}
	public double getTotal() {
		return total;
	}
	public double itemSum() {
		double sum = 0;
		for(Item item: items) {
			sum+= item.getPrice() * item.getCount();
		}
		return sum;
	}

	public String getName() {
		return name;
	}

	public String getDate() {
		return date;
	}

}

