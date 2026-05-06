package model;


/* Factory Design Pattern */

public class UserFactory {
	
	public static User createUser(int userID, String email, String password, String name) {
        return new User(userID, email, password, name);
    }

}
