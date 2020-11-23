package uk.co.lukestevens.config.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class MavenConfigTest {
		
	@Test
	public void testLoad() throws IOException  {
		MavenConfig config = new MavenConfig();
		config.load();
		
		assertEquals(3, config.entrySet().size());
		assertEquals("config-lib-test", config.getApplicationName());
		assertEquals("2.0.0-test", config.getApplicationVersion());
		assertEquals("uk.co.lukestevens", config.getApplicationGroup());
	}

}
