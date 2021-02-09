package uk.co.lukestevens.config.application;

import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.config.Config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

public class ConfiguredApplicationPropertiesTest {
	
	Config config = mock(Config.class);
	
	ApplicationProperties appProps = new ConfiguredApplicationProperties(config);
	
	@Test
	public void testGetApplicationName() {
		when(config.getAsString("application.name")).thenReturn("test-name");
		String actual = appProps.getApplicationName();
		assertEquals("test-name", actual);
	}
	
	@Test
	public void testGetApplicationVersion() {
		when(config.getAsString("application.version")).thenReturn("test-version");
		String actual = appProps.getApplicationVersion();
		assertEquals("test-version", actual);
	}
	
	@Test
	public void testGetApplicationGroup() {
		when(config.getAsString("application.group")).thenReturn("test-group");
		String actual = appProps.getApplicationGroup();
		assertEquals("test-group", actual);
	}
}
