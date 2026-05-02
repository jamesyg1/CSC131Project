package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import model.Item;


class ItemTest {

	@Test
	void testItemCreation() {		
		String input = "Hotdog" + System.lineSeparator() 
					 + 3 + System.lineSeparator()
					 + 3.99;
		
	    InputStream in = new ByteArrayInputStream(input.getBytes());
	    System.setIn(in);

		Item item = Item.inputItem();

		assertEquals("Hotdog", item.getName());
        assertEquals(3, item.getCount());
        assertEquals(3.99, item.getPrice());
	}

}
