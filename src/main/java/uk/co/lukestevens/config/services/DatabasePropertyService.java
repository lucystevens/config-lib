package uk.co.lukestevens.config.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.config.models.Property;
import uk.co.lukestevens.db.Database;
import uk.co.lukestevens.db.DatabaseResult;
import uk.co.lukestevens.utils.Dates;

/**
 * A property service implementation that fetches properties from a database
 * 
 * @author Luke Stevens
 */
public class DatabasePropertyService implements PropertyService {
	
	final Database db;
	final ApplicationProperties applicationProperties;

	@Inject
	public DatabasePropertyService(Database db, ApplicationProperties applicationProperties) {
		this.db = db;
		this.applicationProperties = applicationProperties;
	}

	@Override
	public List<Property> load() throws IOException{
		String sql = "SELECT key, value, refresh_rate FROM core.config WHERE application_name IN('*', ?) ORDER BY application_name!='*';";
		try(DatabaseResult dbr = db.query(sql, this.applicationProperties.getApplicationName())){
			return dbr.parseResultSet(this::parse);
		} catch (SQLException e) {
			throw new IOException(e);
		} 
	}

	@Override
	public Property get(String key) {
		String sql = "SELECT key, value, refresh_rate FROM core.config WHERE application_name IN('*', ?) AND key=? ORDER BY application_name!='*';";
		try(DatabaseResult dbr = db.query(sql, this.applicationProperties.getApplicationName(), key)){
			List<Property> props = dbr.parseResultSet(this::parse);
			return props.isEmpty()? null: props.get(0);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			return null;
		} 
	}
	

	/*
	 * Parse a result set row into a Property object
	 */
	Property parse(ResultSet rs) throws SQLException {
		return new Property(
				rs.getString("key"),
				rs.getString("value"),
				new Date(Dates.millis() + rs.getLong("refresh_rate"))
		);
	}

}
