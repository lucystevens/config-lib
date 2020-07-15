package uk.co.lukestevens.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.co.lukestevens.encryption.EncryptionService;
import uk.co.lukestevens.testing.mocks.MockEncryptionService;

public class ConfigManagerTest {
	
	ConfigManager manager;
	
	@BeforeEach
	public void setup() {
		File configFile = new File("src/test/resources/conf/test.conf");
		EncryptionService encryption = new MockEncryptionService();
		this.manager = new ConfigManager(configFile, encryption);
	}
	
	@Test
	public void testGetApplicationProperties() {
		Config config = this.manager.getAppConfig();
		assertEquals("some string", config.getAsString("string.property"));
		assertTrue(config.getAsBoolean("boolean.property"));
		assertEquals("secretkey", config.getEncrypted("encrypted.property"));
		assertEquals(12.5, config.getAsDouble("double.property"));
		assertEquals(3, config.getAsInt("integer.property"));
		assertNull(config.getAsStringOrDefault("missing.property", null));
		
		assertEquals("config-lib-test", config.getApplicationName());
		assertEquals("1.0.0-beta", config.getApplicationVersion());
	}

}
