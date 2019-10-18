package uk.co.lukestevens.config.services;

import java.util.List;

import uk.co.lukestevens.config.Property;

public interface PropertyService {
	
	List<Property> load();
	
	Property get(String key);

}
