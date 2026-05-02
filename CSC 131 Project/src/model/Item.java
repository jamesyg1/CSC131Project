package model;
import java.util.Scanner;

public class Item {
	/*  Attributes  */
	private String name;
	private int count;
	private double price;
	
	
	/*  Constructor  */
	public Item(String name, int count, double price) {
		this.name = name;
		this.count = count;
		this.price = price;
	}
	
	public static Item inputItem() {
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter item name: ");
        String name = scanner.nextLine();

        System.out.print("Enter quantity: ");
        int count = scanner.nextInt();

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();

        return new Item(name, count, price);
	}
	
	/*  Getter Methods */
	public String getName() {
		return name;
	}
	public int getCount() {
		return count;
	}
	public double getPrice() {
		return price;
	}
	
}
