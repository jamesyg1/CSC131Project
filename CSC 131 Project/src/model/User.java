package model;

public class User {
	
	private int userID;
	private String email;
	private String password;
	private String name;
	
	/* Constructor */
	
	public User(int userID, String email, String password, String name) {
		
		this.userID = userID;
		this.email = email;
		this.password = password;
		this.name = name;
	}
	
	/* Getter Methods */
	
	public int getUserID() {
		return userID;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getName() {
		return name;
	}

}
