package uk.co.lukestevens.config.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.co.lukestevens.config.Property;
import uk.co.lukestevens.mocks.DateMocker;
import uk.co.lukestevens.test.db.TestDatabase;

public class DatabasePropertyServiceTest {
	
	TestDatabase db;
	
	@BeforeEach
	public void setup() throws IOException, SQLException {
		this.db = new TestDatabase();
		this.db.executeFile("setup");
		this.db.executeFile("addSiteConfigs");
	}
	

	@Test
	public void testLoad() throws IOException {
		Date currentDate = new Date();
		DateMocker.setCurrentDate(currentDate);
		
		DatabasePropertyService service = DatabasePropertyService.forSite(db, "test");
		List<Property> props = service.load();
		
		// Before we sort, check that first property is global one
		{
			Property prop = props.get(0);
			assertEquals("global.test.key", prop.getKey());
			assertEquals("globalkey", prop.getValue());
		}
		
		props.sort(Comparator.comparing(Property::getKey));
		
		assertEquals(4, props.size());
		
		Date futureDate = new Date(currentDate.getTime() + 25);
		DateMocker.setCurrentDate(futureDate);
		
		{
			Property prop = props.get(0);
			assertEquals("boolean.test.key", prop.getKey());
			assertEquals("true", prop.getValue());
			assertTrue(prop.isExpired());
		}
		{
			Property prop = props.get(1);
			assertEquals("global.test.key", prop.getKey());
			assertEquals("globalkey", prop.getValue());
			assertFalse(prop.isExpired());
		}
		{
			Property prop = props.get(2);
			assertEquals("int.test.key", prop.getKey());
			assertEquals("33", prop.getValue());
			assertTrue(prop.isExpired());
		}
		{
			Property prop = props.get(3);
			assertEquals("string.test.key", prop.getKey());
			assertEquals("iamakey", prop.getValue());
			assertFalse(prop.isExpired());
		}
	}
	
	@Test
	public void testLoadNoProperties() throws IOException, SQLException {
		this.db.executeFile("setup");
		
		DatabasePropertyService service = DatabasePropertyService.forSite(db, "missing");
		List<Property> props = service.load();
		assertEquals(0, props.size());
	}
	
	@Test
	public void testGetProperty() {
		Date currentDate = new Date();
		DateMocker.setCurrentDate(currentDate);
		
		DatabasePropertyService service = DatabasePropertyService.forSite(db, "test");
		Property prop = service.get("string.test.key");
		
		Date futureDate = new Date(currentDate.getTime() + 31);
		DateMocker.setCurrentDate(futureDate);
		
		assertEquals("string.test.key", prop.getKey());
		assertEquals("iamakey", prop.getValue());
		assertTrue(prop.isExpired());
	}
	
	@Test
	public void testGetOverridenProperty1() throws SQLException {
		this.db.update("INSERT INTO site_config VALUES(default, ?, ?, ?, ?);", "global.test.key", "overiddenkey", "test", 12);
		Date currentDate = new Date();
		DateMocker.setCurrentDate(currentDate);
		
		DatabasePropertyService service = DatabasePropertyService.forSite(db, "test");
		Property prop = service.get("global.test.key");
		
		Date futureDate = new Date(currentDate.getTime() + 31);
		DateMocker.setCurrentDate(futureDate);
		
		assertEquals("global.test.key", prop.getKey());
		assertEquals("overiddenkey", prop.getValue());
		assertTrue(prop.isExpired());
	}
	@Test
	public void testGetOverridenProperty2() throws SQLException {
		this.db.update("INSERT INTO site_config VALUES(default, ?, ?, ?, ?);", "string.test.key", "globalkey", "*", 100);
		Date currentDate = new Date();
		DateMocker.setCurrentDate(currentDate);
		
		DatabasePropertyService service = DatabasePropertyService.forSite(db, "test");
		Property prop = service.get("string.test.key");
		
		Date futureDate = new Date(currentDate.getTime() + 31);
		DateMocker.setCurrentDate(futureDate);
		
		assertEquals("string.test.key", prop.getKey());
		assertEquals("iamakey", prop.getValue());
		assertTrue(prop.isExpired());
	}
	
	@Test
	public void testGetGlobalProperty() {
		Date currentDate = new Date();
		DateMocker.setCurrentDate(currentDate);
		
		DatabasePropertyService service = DatabasePropertyService.forSite(db, "test");
		Property prop = service.get("global.test.key");
		
		Date futureDate = new Date(currentDate.getTime() + 31);
		DateMocker.setCurrentDate(futureDate);
		
		assertEquals("global.test.key", prop.getKey());
		assertEquals("globalkey", prop.getValue());
		assertFalse(prop.isExpired());
	}
	
	@Test
	public void testGetMissingProperty() {
		Date currentDate = new Date();
		DateMocker.setCurrentDate(currentDate);
		
		DatabasePropertyService service = DatabasePropertyService.forSite(db, "test");
		Property prop = service.get("not.test.key");
		
		assertNull(prop);
	}

}
