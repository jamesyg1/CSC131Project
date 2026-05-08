package test;

import static org.junit.jupiter.api.Assertions.*; 

import org.junit.jupiter.api.Test;
import model.User;
import model.UserFactory;

class UserFactoryTest {

	@Test
	void testUserFactory() {
	    new UserFactory();
	    User user = UserFactory.createUser(1, "test@email.com", "pass123", "John");

	    assertEquals(1, user.getUserID());
	    assertEquals("test@email.com", user.getEmail());
	    assertEquals("pass123", user.getPassword());
	    assertEquals("John", user.getName());
	}

}
