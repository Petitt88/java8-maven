package com.pet.king.util;

public class SuperCalculator {
	public <T extends Number> T calc(T param1, T param2) {
		Integer integer = new Integer(param1.intValue() + param2.intValue());
		return (T) integer;
	}
}
