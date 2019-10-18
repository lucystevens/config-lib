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

public class SitePropertyService implements PropertyService {
	
	private final Database db;
	private final String siteName;
	
	public SitePropertyService(Database db, String siteName) {
		this.db = db;
		this.siteName = siteName;
	}

	@Override
	public List<Property> load() {
		String sql = "SELECT key, value, ttl FROM site_config WHERE site IN('*', ?);";
		try(DatabaseResult dbr = db.query(sql, siteName)){
			return dbr.parseResultSet(this::parse);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		} 
	}

	@Override
	public Property get(String key) {
		String sql = "SELECT key, value, ttl FROM site_config WHERE site IN('*', ?) AND key=?;";
		try(DatabaseResult dbr = db.query(sql, siteName, key)){
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
