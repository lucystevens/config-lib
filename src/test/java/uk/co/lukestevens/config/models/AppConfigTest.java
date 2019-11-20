package uk.co.lukestevens.config.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.co.lukestevens.encryption.EncryptionService;
import uk.co.lukestevens.mocks.MockConfigSource;
import uk.co.lukestevens.mocks.MockEncryptionService;

public class AppConfigTest {
	
	AppConfig config;
	
	@BeforeEach
	public void setup() {
		List<ConfigSource> sources = new ArrayList<>();
		
		Properties props1 = new Properties();
		props1.setProperty("string.property", "a value");
		props1.setProperty("boolean.property", "true");
		sources.add(new MockConfigSource(props1));
		
		Properties props2 = new Properties();
		props2.setProperty("string.property", "backup value");
		props2.setProperty("encrypted.property", "secretkey (encrypted)");
		sources.add(new MockConfigSource(props2));
		
		Properties props3 = new Properties();
		props3.setProperty("string.property", "final chance value");
		props3.setProperty("encrypted.property", "publickey (encrypted)");
		props3.setProperty("double.property", "27.45");
		props3.setProperty("boolean.property", "false");
		sources.add(new MockConfigSource(props3));

		EncryptionService encryption = new MockEncryptionService();
		this.config = new AppConfig(encryption, sources);
	}
	
	@Test
	public void testGetProperty() {
		assertEquals("a value", config.getAsString("string.property"));
		assertTrue(config.getAsBoolean("boolean.property"));
		assertEquals("secretkey", config.getEncrypted("encrypted.property"));
		assertEquals(27.45, config.getAsDouble("double.property"));
		assertNull(config.get("missing.property"));
	}
	
	@Test
	public void testEntrySet() {
		Set<Entry<Object,Object>> entryset = config.entrySet();
		assertEquals(4, entryset.size());
		
		List<Entry<Object,Object>> entryList = new ArrayList<>(entryset);
		Collections.sort(entryList, (e1, e2) -> e1.getKey().toString().compareTo(e2.getKey().toString()));
		
		assertEquals("boolean.property", entryList.get(0).getKey());
		assertEquals("true", entryList.get(0).getValue());
		
		assertEquals("double.property", entryList.get(1).getKey());
		assertEquals("27.45", entryList.get(1).getValue());
		
		assertEquals("encrypted.property", entryList.get(2).getKey());
		assertEquals("secretkey (encrypted)", entryList.get(2).getValue());
		
		assertEquals("string.property", entryList.get(3).getKey());
		assertEquals("a value", entryList.get(3).getValue());
	}

}
