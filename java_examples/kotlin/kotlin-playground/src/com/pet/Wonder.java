package com.pet;

public class Wonder {

	public class X {}

	public X createX() {
		return new X();
	}

	public static X createX(Wonder wonder) {
		return wonder.new X();
	}
}
