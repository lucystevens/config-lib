package uk.co.lukestevens.config.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import uk.co.lukestevens.config.Property;
import uk.co.lukestevens.jdbc.Database;
import uk.co.lukestevens.jdbc.result.DatabaseResult;
import uk.co.lukestevens.utils.Dates;

/**
 * A property service implementation that fetches properties from a database
 * 
 * @author Luke Stevens
 */
public class DatabasePropertyService implements PropertyService {
	
	/**
	 * Gets a database property service for application configs
	 * @param db The database to use to fetch configs
	 * @param applicationName The application name
	 * @return A database property service for application configs
	 */
	public static DatabasePropertyService forApplication(Database db, String applicationName) {
		return new DatabasePropertyService("application_config", "application", applicationName, db);
	}
	
	/**
	 * Gets a database property service for site configs
	 * @param db The database to use to fetch configs
	 * @param siteName The site name
	 * @return A database property service for site configs
	 */
	public static DatabasePropertyService forSite(Database db, String siteName) {
		return new DatabasePropertyService("site_config", "site", siteName, db);
	}
	
	
	final String tableName;
	final String columnName;
	final String name;
	final Database db;

	DatabasePropertyService(String tableName, String columnName, String name, Database db) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.name = name;
		this.db = db;
	}

	@Override
	public List<Property> load() throws IOException{
		String baseSql = "SELECT key, value, refresh_rate FROM config.%s WHERE %s IN('*', ?) ORDER BY %s!=?;";
		String sql = String.format(baseSql, tableName, columnName, columnName);	
		try(DatabaseResult dbr = db.query(sql, name, "*")){
			return dbr.parseResultSet(this::parse);
		} catch (SQLException e) {
			throw new IOException(e);
		} 
	}

	@Override
	public Property get(String key) {
		String baseSql = "SELECT key, value, refresh_rate FROM config.%s WHERE %s IN('*', ?) AND key=? ORDER BY %s=?;";
		String sql = String.format(baseSql, tableName, columnName, columnName);	
		try(DatabaseResult dbr = db.query(sql, name, key, "*")){
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
