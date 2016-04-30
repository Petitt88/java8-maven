package com.pet.king.car;

class Parent {
	public Object toObject(Number number) {
		return number.toString();
	}
}

class Child extends Parent {
	// covariance
	@Override
	public String toObject(Number number) {
		return number.toString();
	}

	public String toObject(Double number) {
		return number.toString();
	}
}
