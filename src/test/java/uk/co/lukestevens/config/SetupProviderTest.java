package uk.co.lukestevens.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.co.lukestevens.config.Setups.DummySetup;
import uk.co.lukestevens.config.Setups.SimpleSetup;
import uk.co.lukestevens.config.annotations.ConfigOption;
import uk.co.lukestevens.config.parsers.ArgumentParserProvider;
import uk.co.lukestevens.utils.ReflectionUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class SetupProviderTest {
	
	ReflectionUtils reflection;
	ArgumentParserProvider parserProvider;
	
	@BeforeEach
	public void setup() {
		this.reflection = new ReflectionUtils();
		this.parserProvider = ArgumentParserProvider.getDefault();
	}
	
	private <T> SetupProvider<T> getSetupProvider(Class<T> c){
		return new SetupProvider<>(parserProvider, reflection, c);
	}
	
	
	public void testGetArgumentParserProvider() {
		
	}
	
	public void testParseCommandLine_valid() {
		
	}
	
	public void testParseCommandLine_noArgs() {
		
	}
	
	public void testParseCommandLine_setupIncomplete() {
		
	}
	
	public void testSetupConfigProviders_withConfigFile() {
		
	}
	
	public void testSetupConfigProviders_withoutConfigFile() {
		
	}

	public void testSetupFromField_valid() {
		
	}
	
	public void testSetupFromField_noDefaultConstructor() {
		
	}
	
	public void testSetupConfigValuesFromProvider(){
		
	}
	
	// checkSetupComplete
	
	@Test
	public void testCheckSetupComplete_complete() throws IllegalArgumentException, IllegalAccessException {
		SetupProvider<SimpleSetup> setupProvider = getSetupProvider(SimpleSetup.class);
		
		SimpleSetup setup = new SimpleSetup();
		setup.booleanField = true;
		setup.stringField = "something";
		setup.longOptField = 42;
		
		setupProvider.checkSetupComplete(setup);
	}
	
	@Test
	public void testCheckSetupComplete_incomplete() {
		SetupProvider<SimpleSetup> setupProvider = getSetupProvider(SimpleSetup.class);
		
		SimpleSetup setup = new SimpleSetup();
		setup.booleanField = true;
		setup.longOptField = 42;
		
		assertThrows(ConfigException.class,  () -> setupProvider.checkSetupComplete(setup), "Cannot find value for property: SimpleSetup.stringField.");
		
	}
	
	// createCommandLineOptions
	
	@Test
	public void testCreateCommandLineOptions() {
		SetupProvider<SimpleSetup> setupProvider = getSetupProvider(SimpleSetup.class);
		Options options = setupProvider.createCommandLineOptions();
		
		List<Option> optionsList = new ArrayList<>(options.getOptions());
		assertEquals(4, optionsList.size());
		
		Option configOption = optionsList.stream().filter(o -> o.getOpt().equals("c")).findFirst().orElse(null);
		assertEquals("c", configOption.getOpt());
		assertTrue(configOption.hasArg());
		assertTrue(configOption.hasLongOpt());
		assertEquals("config", configOption.getLongOpt());
	}
	
	// addFieldToOptions
	
	@Test
	public void testAddFieldToOptions_isString() throws NoSuchFieldException, SecurityException {
		SetupProvider<SimpleSetup> setupProvider = getSetupProvider(SimpleSetup.class);
		Options options = new Options();
		
		Field field = SimpleSetup.class.getDeclaredField("stringField");
		
		setupProvider.addFieldToOptions(options, field);
		
		List<Option> optionsList = new ArrayList<>(options.getOptions());
		assertEquals(1, optionsList.size());
		
		Option option = optionsList.get(0);
		assertEquals("s", option.getOpt());
		assertTrue(option.hasArg());
		assertFalse(option.hasLongOpt());
	}
	
	@Test
	public void testAddFieldToOptions_isBoolean() throws NoSuchFieldException, SecurityException {
		SetupProvider<SimpleSetup> setupProvider = getSetupProvider(SimpleSetup.class);
		Options options = new Options();
		
		Field field = SimpleSetup.class.getDeclaredField("booleanField");
		
		setupProvider.addFieldToOptions(options, field);
		
		List<Option> optionsList = new ArrayList<>(options.getOptions());
		assertEquals(1, optionsList.size());
		
		Option option = optionsList.get(0);
		assertEquals("b", option.getOpt());
		assertFalse(option.hasArg());
		assertFalse(option.hasLongOpt());
	}
	
	@Test
	public void testAddFieldToOptions_hasLongOpt() throws NoSuchFieldException, SecurityException {
		SetupProvider<SimpleSetup> setupProvider = getSetupProvider(SimpleSetup.class);
		Options options = new Options();
		
		Field field = SimpleSetup.class.getDeclaredField("longOptField");
		
		setupProvider.addFieldToOptions(options, field);
		
		List<Option> optionsList = new ArrayList<>(options.getOptions());
		assertEquals(1, optionsList.size());
		
		Option option = optionsList.get(0);
		assertEquals("l", option.getOpt());
		assertTrue(option.hasArg());
		assertTrue(option.hasLongOpt());
		assertEquals("longopt", option.getLongOpt());
	}
	
	// hasLongOpt
	
	@Test
	public void testHasLongOpt_true() {
		SetupProvider<DummySetup> setupProvider = getSetupProvider(DummySetup.class);
		ConfigOption mockConfigOption = mock(ConfigOption.class);
		when(mockConfigOption.longOpt()).thenReturn("longopt");
		assertTrue(setupProvider.hasLongOpt(mockConfigOption));
	}
	
	@Test
	public void testHasLongOpt_false() {
		SetupProvider<DummySetup> setupProvider = getSetupProvider(DummySetup.class);
		ConfigOption mockConfigOption = mock(ConfigOption.class);
		when(mockConfigOption.longOpt()).thenReturn(ConfigOption.LONG_OPT_DEFAULT);
		assertFalse(setupProvider.hasLongOpt(mockConfigOption));
	}
}
