package uk.co.lukestevens.config.setups;

import java.io.File;

import uk.co.lukestevens.config.annotations.ConfigOption;

public class AdvancedSetup extends BooleanSetup {

	@ConfigOption
	private Object testObject;

	@ConfigOption
	private String testString;

	@ConfigOption
	private File testFile;

	public Object getTestObject() {
		return testObject;
	}

	public String getTestString() {
		return testString;
	}

	public File getTestFile() {
		return testFile;
	}

}
