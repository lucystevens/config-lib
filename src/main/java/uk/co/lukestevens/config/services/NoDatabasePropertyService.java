package uk.co.lukestevens.config.services;

import java.util.ArrayList;
import java.util.List;

import uk.co.lukestevens.config.Property;

public class NoDatabasePropertyService implements PropertyService {

	@Override
	public List<Property> load() {
		return new ArrayList<>();
	}

	@Override
	public Property get(String key) {
		return null;
	}

}
