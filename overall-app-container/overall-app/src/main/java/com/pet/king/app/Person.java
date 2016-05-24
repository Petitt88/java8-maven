package com.pet.king.app;

public class Person {

	public Person() {
		this(0);
		Pocket p = new Pocket();
		PocketStatic ps = new PocketStatic();
	}

	public Person(int age) {
		this.age = age;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	private int age;

	protected class Pocket {
		Pocket() {
			age = 2;
		}
	}

	protected static class PocketStatic {
		PocketStatic() {

		}
	}

	void boss() {

	}

	protected void bossProt() {

	}
}