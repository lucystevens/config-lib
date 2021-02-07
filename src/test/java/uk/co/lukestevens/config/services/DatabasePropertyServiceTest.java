package uk.co.lukestevens.config.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.config.models.Property;
import uk.co.lukestevens.config.services.DatabasePropertyService;
import uk.co.lukestevens.db.Database;
import uk.co.lukestevens.db.DatabaseResult;
import uk.co.lukestevens.testing.mocks.DateMocker;

public class DatabasePropertyServiceTest {
	
	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	ApplicationProperties applicationProperties = mock(ApplicationProperties.class);
	Database database = mock(Database.class);
	
	DatabasePropertyService service = 
		spy(new DatabasePropertyService(database, applicationProperties));
	
	@Test
	public void testLoad() throws IOException, SQLException {
		when(applicationProperties.getApplicationName()).thenReturn("test");
		
		DatabaseResult dbr = mock(DatabaseResult.class);
		when(database.query(DatabasePropertyService.LOAD_PROPERTIES_SQL, "test"))
			.thenReturn(dbr);
		
		when(dbr.parseResultSet(service::parse)).thenReturn(new ArrayList<>());
		
		List<Property> props = service.load();
		assertTrue(props.isEmpty());
	}
	
	@Test
	public void testGetWhenNoPropertiesFound() throws SQLException, IOException {
		when(applicationProperties.getApplicationName()).thenReturn("test");
		
		DatabaseResult dbr = mock(DatabaseResult.class);
		when(database.query(DatabasePropertyService.GET_PROPERTY_SQL, "test", "test.config"))
			.thenReturn(dbr);
		
		when(dbr.parseResultSet(any())).thenReturn(new ArrayList<>());
		
		Property prop = service.get("test.config");
		assertNull(prop);
	}
	
	@Test
	public void testGetWhenPropertyFound() throws SQLException, IOException {
		when(applicationProperties.getApplicationName()).thenReturn("test");
		
		DatabaseResult dbr = mock(DatabaseResult.class);
		when(database.query(DatabasePropertyService.GET_PROPERTY_SQL, "test", "test.config"))
			.thenReturn(dbr);
		
		Property expected = new Property("test.config", "some-value");
		when(dbr.parseResultSet(any())).thenReturn(Arrays.asList(expected));
		
		Property actual = service.get("test.config");
		assertEquals("test.config", actual.getKey());
		assertEquals("some-value", actual.getValue());
	}
	
	@Test
	public void testParse() throws SQLException, IOException, ParseException {
		ResultSet rs = mock(ResultSet.class);
		when(rs.getString("key")).thenReturn("test.config");
		when(rs.getString("value")).thenReturn("some-value");
		when(rs.getLong("refresh_rate")).thenReturn(1000L*73L);// 73 seconds
		
		DateMocker.setCurrentDate(df.parse("2021-02-07 21:59:34"));
		Property actual = service.parse(rs);
		assertEquals("test.config", actual.getKey());
		assertEquals("some-value", actual.getValue());
		assertEquals(df.parse("2021-02-07 22:00:47"), actual.getExpiry());
	}
	
}
