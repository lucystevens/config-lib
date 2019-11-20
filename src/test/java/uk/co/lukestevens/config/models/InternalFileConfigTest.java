package uk.co.lukestevens.config.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.co.lukestevens.config.models.InternalFileConfig;
import uk.co.lukestevens.encryption.EncryptionService;
import uk.co.lukestevens.mocks.MockEncryptionService;

public class InternalFileConfigTest {
	
	InternalFileConfig config;
	
	@BeforeEach
	public void setup() {
		EncryptionService encryption = new MockEncryptionService();
		this.config = new InternalFileConfig("/conf/InternalFileConfigTest.conf", encryption);
	}
	
	@Test
	public void testGetPropertyBeforeLoad() {
		Object value = config.get("string.property");
		assertNull(value);
	}
	
	@Test
	public void testGetExistingProperty() throws IOException {
		config.load();
		
		String string = config.getAsString("string.property");
		assertEquals("some string", string);
		
		boolean bool = config.getAsBoolean("boolean.property");
		assertTrue(bool);
		
		double doubl = config.getAsDouble("double.property");
		assertEquals(12.5, doubl);
		
		int integer = config.getAsInt("integer.property");
		assertEquals(3, integer);
	}
	
	@Test
	public void testGetEncryptedProperty() throws IOException {
		config.load();
		
		String string = config.getEncrypted("encrypted.property");
		assertEquals("secretkey", string);
	}
	
	@Test
	public void testGetMissingProperty() throws IOException {
		config.load();
		
		Object value = config.get("missing.property");
		assertNull(value);
	}

	@Test
	public void testEntrySet() throws IOException {
		config.load();
		
		List<Entry<Object, Object>> entries = new ArrayList<>(config.entrySet());
		Collections.sort(entries, (e1, e2) -> e1.getKey().toString().compareTo(e2.getKey().toString()));
		
		{
			Entry<Object, Object> entry = entries.get(0);
			assertEquals("boolean.property", entry.getKey());
			assertEquals("true", entry.getValue());
		}
		
		{
			Entry<Object, Object> entry = entries.get(1);
			assertEquals("double.property", entry.getKey());
			assertEquals("12.5", entry.getValue());
		}
		
		{
			Entry<Object, Object> entry = entries.get(2);
			assertEquals("encrypted.property", entry.getKey());
			assertEquals("secretkey (encrypted)", entry.getValue());
		}
		
		{
			Entry<Object, Object> entry = entries.get(3);
			assertEquals("integer.property", entry.getKey());
			assertEquals("3", entry.getValue());
		}
		
		{
			Entry<Object, Object> entry = entries.get(4);
			assertEquals("string.property", entry.getKey());
			assertEquals("some string", entry.getValue());
		}
	}
}
