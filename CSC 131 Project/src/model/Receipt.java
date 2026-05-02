package model;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Receipt {
	private int receiptID;
	private User owner;
	private List<Item> items;
	private double total;
	private Date date;
	
	public Receipt(User owner, List<Item> items, double total) {
		this.owner = owner;
		this.items = new ArrayList<>(items);
		this.total = total;
		this.date = new Date();
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
			sum+= item.getPrice();
		}
		return sum;
	}
}
