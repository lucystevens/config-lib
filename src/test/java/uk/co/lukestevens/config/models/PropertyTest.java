package uk.co.lukestevens.config.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.co.lukestevens.testing.mocks.DateMocker;

public class PropertyTest {
	
	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@BeforeEach
	public void setNow() throws ParseException {
		DateMocker.setCurrentDate(df.parse("2021-02-07 21:45:11"));
	}
	
	@Test
	public void testisExpiredWhenExpiryIsNull() {
		Property prop = new Property("test.config", "some-value");
		assertNull(prop.getExpiry());
		assertFalse(prop.isExpired());
	}
	
	@Test
	public void testisExpiredWhenExpiryBeforeNow() throws ParseException {
		Property prop = new Property("test.config", "some-value",
				df.parse("2021-02-07 21:45:00"));
		assertTrue(prop.isExpired());
	}
	
	@Test
	public void testisExpiredWhenExpiryIsNow() throws ParseException {
		Property prop = new Property("test.config", "some-value",
				df.parse("2021-02-07 21:45:11"));
		assertFalse(prop.isExpired());
	}
	
	@Test
	public void testisExpiredWhenExpiryAfterNow() throws ParseException {
		Property prop = new Property("test.config", "some-value",
				df.parse("2021-02-07 21:45:40"));
		assertFalse(prop.isExpired());
	}

}
