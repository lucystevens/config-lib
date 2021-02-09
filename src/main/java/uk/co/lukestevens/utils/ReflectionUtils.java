package uk.co.lukestevens.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtils {

	/**
	 * Recursively get all fields from a class and it's superclasses
	 * @param <T> The class type
	 * @param c The class to get fields from
	 * @return A list of all fields on the class and it's superclasses
	 */
	public static <T> List<Field> getAllFields(Class<T> c){
		List<Field> fields = new ArrayList<>();
		Class<?> currentClass = c;
		while(currentClass != null && !currentClass.isPrimitive() && !currentClass.equals(Object.class)) {
			for(Field field : currentClass.getDeclaredFields()) {
				fields.add(field);
			}
			currentClass = currentClass.getSuperclass();
		}
		return fields;
	}
	
	/**
	 * Get all fields on a class (and it's superclasses) with a given annotation
	 * @param <T> The class type
	 * @param <A> The annotation type
	 * @param c The class to get fields from
	 * @param annotationClass The annotation to check against fields
	 * @return A list of all fields on a class with a given annotation
	 */
	public static <T, A extends Annotation> List<Field> getAllFieldsWithAnnotation(Class<T> c, Class<A> annotationClass){
		return getAllFields(c)
				.stream()
				.filter(f -> f.getDeclaredAnnotation(annotationClass) != null)
				.collect(Collectors.toList());
	}
	
}
