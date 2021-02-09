package uk.co.lukestevens.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import uk.co.lukestevens.utils.SubClassB.InnerSubClassC;

public class ReflectionUtilsTest {
	
	@Test
	public void testGetAllFields() {
		List<Field> fields = ReflectionUtils.getAllFields(InnerSubClassC.class);
		assertEquals(6, fields.size());
		
		Collections.sort(fields, Comparator.comparing(Field::getName));
		assertEquals("annotatedFieldA", fields.get(0).getName());
		assertEquals("annotatedFieldB", fields.get(1).getName());
		assertEquals("annotatedFieldC", fields.get(2).getName());
		assertEquals("fieldA", fields.get(3).getName());
		assertEquals("fieldB", fields.get(4).getName());
		assertEquals("fieldC", fields.get(5).getName());
	}
	
	@Test
	public void testGetAllFieldsWithAnnotation() {
		List<Field> fields = ReflectionUtils.getAllFieldsWithAnnotation(InnerSubClassC.class, Inject.class);
		assertEquals(3, fields.size());
		
		Collections.sort(fields, Comparator.comparing(Field::getName));
		assertEquals("annotatedFieldA", fields.get(0).getName());
		assertEquals("annotatedFieldB", fields.get(1).getName());
		assertEquals("annotatedFieldC", fields.get(2).getName());
	}
}
