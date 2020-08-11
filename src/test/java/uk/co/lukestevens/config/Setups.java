package uk.co.lukestevens.config;

import uk.co.lukestevens.config.annotations.ConfigOption;

public class Setups {
	
	public static class DummySetup {
		
	}
	
	public static class SimpleSetup {
		
		@ConfigOption(envVar = "", opt = "s")
		String stringField;
		
		@ConfigOption(envVar = "", opt = "b")
		boolean booleanField;
		
		@ConfigOption(envVar = "", opt = "l", longOpt = "longopt")
		int longOptField;
		
		double nonAnnotatedField;
		
	}

}
