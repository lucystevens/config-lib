package uk.co.lukestevens.config.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.lukestevens.config.Property;
import uk.co.lukestevens.jdbc.Database;
import uk.co.lukestevens.jdbc.result.DatabaseResult;

public class ApplicationPropertyService implements PropertyService {
	
	private final Database db;
	private final String applicationName;
	
	public ApplicationPropertyService(Database db, String applicationName) {
		this.db = db;
		this.applicationName = applicationName;
	}

	@Override
	public List<Property> load() {
		String sql = "SELECT key, value, ttl FROM application_config WHERE application IN('*', ?);";
		try(DatabaseResult dbr = db.query(sql, applicationName)){
			return dbr.parseResultSet(this::parse);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		} 
	}

	@Override
	public Property get(String key) {
		String sql = "SELECT key, value, ttl FROM application_config WHERE application IN('*', ?) AND key=?;";
		try(DatabaseResult dbr = db.query(sql, applicationName, key)){
			List<Property> props = dbr.parseResultSet(this::parse);
			return props.isEmpty()? null: props.get(0);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	Property parse(ResultSet rs) throws SQLException {
		return new Property(
				rs.getString("key"),
				rs.getString("value"),
				new Date(System.currentTimeMillis() + rs.getLong("ttl"))
		);
	}

}
