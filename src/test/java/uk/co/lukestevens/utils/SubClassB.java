package uk.co.lukestevens.utils;

import javax.inject.Inject;

@SuppressWarnings("unused")
public class SubClassB extends ClassA {
	

	String fieldB;
	

	@Inject
	private String annotatedFieldB;
	
	
	static class InnerSubClassC extends SubClassB {
		String fieldC;
		
		@Inject
		private String annotatedFieldC;
	}

}
