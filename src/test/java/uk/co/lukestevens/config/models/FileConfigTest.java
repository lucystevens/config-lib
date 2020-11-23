package uk.co.lukestevens.config.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileConfigTest {

	
	@Test
	public void testLoad_fileExists() throws IOException {
		FileConfig config = new FileConfig(new File("src/test/resources/test.properties"));
		config.load();
		
		assertEquals(5, config.entrySet().size());
		assertEquals("some string", config.getAsString("string.property"));
		assertTrue(config.getAsBoolean("boolean.property"));
		assertEquals(12.5, config.getAsDouble("double.property"));
		assertEquals(3, config.getAsInt("integer.property"));
		assertEquals("secretkey (encrypted)", config.getAsString("encrypted.property"));
	}
	
	@Test
	public void testLoad_fileNotExists() throws IOException {
		FileConfig config = new FileConfig(new File("src/test/resources/missing.properties"));
		assertThrows(FileNotFoundException.class, () -> config.load());
	}
}
