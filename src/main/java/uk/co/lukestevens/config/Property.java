package uk.co.lukestevens.config;

import java.util.Date;

public class Property {
	
	private String key;
	private String value;
	private Date expiry;
	
	public Property(String key, String value, Date expiry) {
		this.key = key;
		this.value = value;
		this.expiry = expiry;
	}
	
	public Property(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	public boolean isExpired() {
		return expiry != null && expiry.before(new Date());
	}

}
