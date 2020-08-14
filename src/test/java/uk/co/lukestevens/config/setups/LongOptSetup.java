package uk.co.lukestevens.config.setups;

import uk.co.lukestevens.config.annotations.ConfigOption;

public abstract class LongOptSetup {

	@ConfigOption
	private int testint;

	@ConfigOption
	private Integer testInteger;

	public int getTestint() {
		return testint;
	}

	public Integer getTestInteger() {
		return testInteger;
	}

}
