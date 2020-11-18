package uk.co.lukestevens.config.models;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class EnvironmentConfigTest {
	
	@Test
	public void testNormalise() {
		EnvironmentConfig config = new EnvironmentConfig();
		assertEquals("app.home", config.normalise("APP_HOME"));
		assertEquals("core.db.url", config.normalise("core.db.url"));
		assertEquals("hibernate.driver.class", config.normalise("hibernate.driver_class"));
	}
	
	@Test
	public void testInit() {
		Map<String, String> envVariables = new HashMap<>();
		envVariables.put("DATABASE_URL", "jdbc:url");
		envVariables.put("OVERRIDE_PORT", "10000");
		envVariables.put("properties.somethingElse", "true");
		
		EnvironmentConfig config = new EnvironmentConfig();
		config.init(envVariables);
		
		assertEquals("jdbc:url", config.get("database.url"));
		assertEquals(10000, config.getAsInt("override.port"));
		assertTrue(config.getAsBoolean("properties.somethingelse"));
		assertEquals(3, config.entrySet().size());
	}
	
	@Test
	public void testLoad() throws IOException {
		EnvironmentConfig config = new EnvironmentConfig();
		config.load();
		
		// This will throw an exception if not exists
		config.get("java.home");
	}

}
