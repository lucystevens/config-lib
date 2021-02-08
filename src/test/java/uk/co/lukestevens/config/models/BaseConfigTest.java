package uk.co.lukestevens.config.models;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import uk.co.lukestevens.config.exceptions.ConfigException;

public class BaseConfigTest {
	
	BaseConfig config = mock(BaseConfig.class, CALLS_REAL_METHODS);
	
	@Test
	public void testGetAsString_valueExists() {
		when(config.get("key")).thenReturn("value");
		assertEquals("value", config.getAsString("key"));
	}
	
	@Test
	public void testGetAsString_valueMissing() {
		when(config.get("key")).thenReturn("value");
		ConfigException e = assertThrows(ConfigException.class, () -> {
			config.getAsString("nokey");
		});
		assertEquals("Cannot find value for property: nokey.", e.getMessage());
	}
	
	@Test
	public void testGetAsStringOrDefault_valueExists() {
		when(config.get("key")).thenReturn("value");
		assertEquals("value", config.getAsStringOrDefault("key", "default"));
	}
	
	@Test
	public void testGetAsStringOrDefault_valueMissing() {
		when(config.get("key")).thenReturn(null);
		assertEquals("default", config.getAsStringOrDefault("key", "default"));
	}
	
	@Test
	public void testGetParsedValue_valueParsesCorrectly() {
		when(config.get("key")).thenReturn("value");
		assertEquals("value+", config.getParsedValue("key", s -> s+"+"));
	}
	
	@Test
	public void testGetParsedValue_valueParsingThrowsException() {
		when(config.get("key")).thenReturn("value");
		ConfigException e = assertThrows(ConfigException.class, () -> {
			config.getParsedValue("key", s -> { throw new Exception("e"); });
		});
		assertEquals("Could not load key: key. Root cause: e", e.getMessage());
	}
	
	@Test
	public void testGetParsedValueOrDefault_valueMissing() {
		when(config.get("key")).thenReturn(null);
		assertEquals("default", config.getParsedValueOrDefault("key", s -> s+"+", "default"));
	}
	
	@Test
	public void testGetParsedValueOrDefault_valueParsingThrowsException() {
		when(config.get("key")).thenReturn("value");
		ConfigException e = assertThrows(ConfigException.class, () -> {
			config.getParsedValueOrDefault("key", s -> { throw new Exception("e"); }, "default");
		});
		assertEquals("Could not load key: key. Root cause: e", e.getMessage());
	}
	
	@Test
	public void testBooleanParser() throws Exception {
		assertTrue(BaseConfig.BOOLEAN_PARSER.parse("true"));
		assertTrue(BaseConfig.BOOLEAN_PARSER.parse("TrUe"));
		assertTrue(BaseConfig.BOOLEAN_PARSER.parse("TRUE"));
		
		assertFalse(BaseConfig.BOOLEAN_PARSER.parse("false"));
		assertFalse(BaseConfig.BOOLEAN_PARSER.parse("FALSE"));
		assertFalse(BaseConfig.BOOLEAN_PARSER.parse("yes"));
		assertFalse(BaseConfig.BOOLEAN_PARSER.parse("t"));
		assertFalse(BaseConfig.BOOLEAN_PARSER.parse("somethingelse"));
	}
	
	@Test
	public void testListParser() throws Exception {
		assertEquals(Arrays.asList("1", "3", "4"), BaseConfig.LIST_PARSER.parse("1,3,4"));
		assertEquals(Arrays.asList("1"), BaseConfig.LIST_PARSER.parse("1"));
		assertEquals(Arrays.asList("1", "3", "6"), BaseConfig.LIST_PARSER.parse("1, 3, 6 "));
		assertEquals(Arrays.asList(""), BaseConfig.LIST_PARSER.parse(""));
		assertEquals(Arrays.asList("2 4", "6 8", "motorway"), BaseConfig.LIST_PARSER.parse("2 4, 6 8, motorway"));
	}
	

}
