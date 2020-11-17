package uk.co.lukestevens.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtils {

	/*
	 * Recursively gets all fields from this class and super classes
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
	
	public static <T, A extends Annotation> List<Field> getAllFieldsWithAnnotation(Class<T> c, Class<A> annotationClass){
		return getAllFields(c)
				.stream()
				.filter(f -> f.getDeclaredAnnotation(annotationClass) != null)
				.collect(Collectors.toList());
	}
	
}
