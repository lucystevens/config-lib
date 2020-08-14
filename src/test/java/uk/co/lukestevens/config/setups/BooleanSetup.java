package uk.co.lukestevens.config.setups;

import uk.co.lukestevens.config.annotations.ConfigOption;

public class BooleanSetup extends ExtendedNumberSetup {

	@ConfigOption
	private boolean testboolean;

	@ConfigOption
	private Boolean testBoolean;

	public boolean isTestboolean() {
		return testboolean;
	}

	public Boolean getTestBoolean() {
		return testBoolean;
	}

}
