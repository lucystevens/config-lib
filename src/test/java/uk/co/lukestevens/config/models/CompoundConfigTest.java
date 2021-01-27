package uk.co.lukestevens.config.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Set;

import uk.co.lukestevens.config.Config;

public class CompoundConfigTest {
	
	@Test
	public void testLoad_noConfigs() throws IOException {
		CompoundConfig config = new CompoundConfig();
		config.load();
		assertEquals(0, config.entrySet().size());
	}
	
	@Test
	public void testLoad_singleConfig() throws IOException {
		Config config1 = mock(Config.class);
		when(config1.getAsStringOrDefault("key", null)).thenReturn("value");
		Set<Entry<Object, Object>> entrySet = new HashSet<>();
		entrySet.add(new AbstractMap.SimpleEntry<Object, Object>("key", "value"));
		when(config1.entrySet()).thenReturn(entrySet);
		
		CompoundConfig compoundConfig = new CompoundConfig(config1);
		compoundConfig.load();
		
		verify(config1).load();
		assertEquals("value", compoundConfig.get("key"));
		assertEquals(1, compoundConfig.entrySet().size());
	}
	
	@Test
	public void testLoad_multipleConfigs() throws IOException {
		Config config1 = mock(Config.class);
		when(config1.getAsStringOrDefault("key", null)).thenReturn("value");
		
		Set<Entry<Object, Object>> entrySet1 = new HashSet<>();
		entrySet1.add(new AbstractMap.SimpleEntry<Object, Object>("key", "value"));
		when(config1.entrySet()).thenReturn(entrySet1);
		
		Config config2 = mock(Config.class);
		when(config2.getAsStringOrDefault("another", null)).thenReturn("property");
		when(config2.getAsStringOrDefault("and", null)).thenReturn("one more");
		
		Set<Entry<Object, Object>> entrySet2 = new HashSet<>();
		entrySet2.add(new AbstractMap.SimpleEntry<Object, Object>("another", "property"));
		entrySet2.add(new AbstractMap.SimpleEntry<Object, Object>("and", "one more"));
		when(config2.entrySet()).thenReturn(entrySet2);
		
		Config config3 = mock(Config.class);
		when(config3.getAsStringOrDefault("key", null)).thenReturn("other value");
		
		Set<Entry<Object, Object>> entrySet3 = new HashSet<>();
		entrySet3.add(new AbstractMap.SimpleEntry<Object, Object>("key", "other value"));
		when(config3.entrySet()).thenReturn(entrySet3);
		
		CompoundConfig compoundConfig = new CompoundConfig(config1, config2, config3);
		compoundConfig.load();
		
		verify(config1).load();
		verify(config2).load();
		verify(config3).load();
		
		assertEquals("value", compoundConfig.get("key"));
		assertEquals("property", compoundConfig.get("another"));
		assertEquals("one more", compoundConfig.get("and"));
		assertEquals(3, compoundConfig.entrySet().size());
		
	}

}
