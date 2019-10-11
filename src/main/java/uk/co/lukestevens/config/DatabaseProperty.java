package uk.co.lukestevens.config;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import uk.co.lukestevens.models.IEntity;

@Entity
@Table(name = "applications")
public class DatabaseProperty extends IEntity {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Integer id;
	
	@Column(name = "site", unique=true)
	private String site;
	
	@Column(name = "key")
	private String key;
	
	@Column(name = "value")
	private String value;
	
	@Column(name = "ttl")
	private long timeToLive;

	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * @return The site name this property is valid for. * indicates all sites
	 */
	public String getSite() {
		return site;
	}

	/**
	 * Sets the site this property is valid for. * indicates all sites
	 * @param site
	 */
	public void setSite(String site) {
		this.site = site;
	}

	/**
	 * @return The key for this property e.g. 'api.key'
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the key for this property
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return Gets the current value for this property
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the current value for this property
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return The length of time (in seconds) that this property should
	 * be cached before being updated from the database. Defaults to 5 minutes
	 */
	public long getTimeToLive() {
		return timeToLive;
	}

	/**
	 * Sets the length of time in seconds this property should be cached
	 * for before being updated from the database
	 * @param timeToLive
	 */
	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

}
