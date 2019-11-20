package uk.co.lukestevens.config.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.co.lukestevens.mocks.DateMocker;
import uk.co.lukestevens.utils.Dates;

public class ExternalFileConfigTest {
	
	ExternalFileConfig config;
	
	@BeforeEach
	public void setup() throws IOException {
		// Copy config file to user directory
		File targetFile = new File("ExternalFileConfigTest.conf");
		try(InputStream initialStream = ExternalFileConfigTest.class.getResourceAsStream("/conf/InternalFileConfigTest.conf")){
		    byte[] buffer = new byte[initialStream.available()];
		    initialStream.read(buffer);
		 
		    try(OutputStream outStream = new FileOutputStream(targetFile)){
		    	outStream.write(buffer);
		    }
		}
	    
	    DateMocker.setCurrentDate(new Date());
		this.config = new ExternalFileConfig(targetFile, 10L);
	}
	
	@AfterEach
	public void tearDown() throws IOException {
		// If the temp config exists then delete it
		File targetFile = new File("ExternalFileConfigTest.conf");
		if(targetFile.exists()) {
			Files.delete(targetFile.toPath());
		}
	}
	
	@Test
	public void testGetPropertyBeforeLoad() {
		Object value = config.get("string.property");
		assertNull(value);
	}
	
	@Test
	public void testGetExistingProperty() throws IOException {
		config.load();
		
		Object string = config.get("string.property");
		assertEquals("some string", string);
		
		Object bool = config.get("boolean.property");
		assertEquals("true", bool);
		
		Object doubl = config.get("double.property");
		assertEquals("12.5", doubl);
		
		Object integer = config.get("integer.property");
		assertEquals("3", integer);
		
		Object encrypted = config.get("encrypted.property");
		assertEquals("secretkey (encrypted)", encrypted);
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
		assertEquals(5, entries.size());
		
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
	
	@Test
	public void testLoadExpiredProperty() throws IOException {
		config.load();
		
		{
			Object string = config.get("string.property");
			assertEquals("some string", string);
		}
		
		// Switch out config for updated version
		this.tearDown();
		File targetFile = new File("ExternalFileConfigTest.conf");
		try(InputStream initialStream = ExternalFileConfigTest.class.getResourceAsStream("/conf/UpdatedExternalFileConfigTest.conf")){
		    byte[] buffer = new byte[initialStream.available()];
		    initialStream.read(buffer);
		 
		    try(OutputStream outStream = new FileOutputStream(targetFile)){
		    	outStream.write(buffer);
		    }
		}
		
		DateMocker.setCurrentDate(new Date(Dates.millis() + 100));
		
		Object updated = config.get("string.property");
		assertEquals("updated", updated);
		
		Object neu = config.get("new.property");
		assertEquals("newproperty", neu);
	}
	
	@Test
	public void testLoadExpiredEntryset() throws IOException {
		config.load();
		
		assertEquals(5, config.entrySet().size());
		DateMocker.setCurrentDate(new Date(Dates.millis() + 100));
		assertEquals(5, config.entrySet().size());
	}
}
