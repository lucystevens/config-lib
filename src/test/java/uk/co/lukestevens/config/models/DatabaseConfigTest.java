package uk.co.lukestevens.config.models;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.Test;

import uk.co.lukestevens.config.services.PropertyService;
import uk.co.lukestevens.testing.mocks.DateMocker;

public class DatabaseConfigTest {
	
	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	PropertyService propService = mock(PropertyService.class);
	
	DatabaseConfig config = spy(new DatabaseConfig(propService));
	
	void addProperty(Property prop) {
		config.cache.put(prop.getKey(), prop);
	}
	
	@Test
	public void testEntryset() throws ParseException {
		DateMocker.setCurrentDate(df.parse("2021-02-07"));
		addProperty(new Property("no-expiry-key", "value1"));
		addProperty(new Property("expired-key", "value2", df.parse("2021-01-01")));
		addProperty(new Property("valid-key", "value3", df.parse("2021-02-08")));
		
		Set<Entry<Object, Object>> entryset = config.entrySet();
		assertEquals(2, entryset.size());
		assertTrue(entryset.contains(
				new AbstractMap.SimpleEntry<>("no-expiry-key", "value1")));
		assertTrue(entryset.contains(
				new AbstractMap.SimpleEntry<>("valid-key", "value3")));
	}
	
	@Test
	public void testGetWhenPropertyNotInCache() throws ParseException {
		when(propService.get("key1")).thenReturn(new Property("key1", "value1"));
		String value = config.get("key1");
		
		assertEquals("value1", value);
		assertTrue(config.cache.containsKey("key1"));
	}
	
	@Test
	public void testGetWhenPropertyInCacheExpired() throws ParseException {
		DateMocker.setCurrentDate(df.parse("2021-02-07"));
		addProperty(new Property("expired-key", "value2", df.parse("2021-01-01")));
		
		when(propService.get("expired-key")).thenReturn(new Property("expired-key", "value3"));
		String value = config.get("expired-key");
		
		assertEquals("value3", value);
		assertEquals("value3", config.cache.get("expired-key").getValue());
	}
	
	@Test
	public void testGetWhenPropertyNotInCacheOrService() {
		String value = config.get("missing-key");
		assertNull(value);
	}
	
	@Test
	public void testGetWhenPropertyIsInCache() {
		addProperty(new Property("no-expiry-key", "value1"));
		String value = config.get("no-expiry-key");
		assertEquals("value1", value);
	}
	
	@Test
	public void testLoad() throws IOException {
		when(propService.load()).thenReturn(Arrays.asList(
				new Property("key1", "value1"),
				new Property("key2", "value2"),
				new Property("key3", "value3")
			));
		
		config.load();
		assertEquals(3, config.cache.size());
		assertTrue(config.cache.containsKey("key1"));
		assertTrue(config.cache.containsKey("key2"));
		assertTrue(config.cache.containsKey("key3"));
	}
}
