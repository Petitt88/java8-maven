package com.pet;

import org.jetbrains.annotations.NotNull;

public class TestJava {

	private Object obj;

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public void tester(Object obj) {
	}

	public void testerNotNull(@NotNull Object obj) {
	}
}
