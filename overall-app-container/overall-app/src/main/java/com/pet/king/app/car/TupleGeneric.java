package com.pet.king.app.car;

public class TupleGeneric<T1, T2> {
	private T1 age;
	private T2 name;

	public TupleGeneric(T1 age, T2 name) {
		this.age = age;
		this.name = name;
	}

	public T1 getAge() {
		return age;
	}

	public void setAge(T1 age) {
		this.age = age;
	}

	public T2 getName() {
		return name;
	}

	public void setName(T2 name) {
		this.name = name;
	}
}