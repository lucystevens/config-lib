package uk.co.lukestevens.config.setups;

import uk.co.lukestevens.config.annotations.ConfigOption;

public class ExtendedNumberSetup extends LongOptSetup {

	@ConfigOption
	private float testfloat;

	@ConfigOption
	private Float testFloat;

	@ConfigOption
	private double testdouble;

	@ConfigOption
	private Double testDouble;
	
	private String missingField;

	public float getTestfloat() {
		return testfloat;
	}

	public Float getTestFloat() {
		return testFloat;
	}

	public double getTestdouble() {
		return testdouble;
	}

	public Double getTestDouble() {
		return testDouble;
	}

	public String getMissingField() {
		return missingField;
	}
	
	

}
