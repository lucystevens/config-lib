package uk.co.lukestevens.config.models;

import java.util.Date;

import uk.co.lukestevens.utils.Dates;

/**
 * A simple object to define a Key-Value Property
 * 
 * @author Luke Stevens
 */
public class Property {
	
	private String key;
	private String value;
	private Date expiry;
	
	/**
	 * Creates a new Property with an expiry time
	 * @param key The property key
	 * @param value The property value
	 * @param expiry The Date at which this Property will expire
	 */
	public Property(String key, String value, Date expiry) {
		this.key = key;
		this.value = value;
		this.expiry = expiry;
	}
	
	/**
	 * Creates a new Property with no expiry
	 * @param key The property key
	 * @param value The property value
	 */
	public Property(String key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * @return The property key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * @return The property value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @return Whether this property has expired or not
	 */
	public boolean isExpired() {
		return expiry != null && expiry.before(Dates.now());
	}

}
