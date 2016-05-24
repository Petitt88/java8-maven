package com.pet.king.app;

import java.io.IOException;

@FunctionalInterface()
public interface InterfaceMindBlowUp {
	/** public static final implicitly */
	String CONSTANT_VALUE = "CONSTANT";

	/** public abstract implicitly */
	void performAction();

	// nested classes, interfaces or enumerations are implicitly public and static

	enum InnerEnum {
		E1, E2
	}

	class InnerClass {
	}

	interface InnerInterface {
		void performInnerAction();
	}

	default void performDefaulAction() {
		// Implementation here
	}

	static void createAction() {
		// Implementation here
	}
}

@FunctionalInterface
interface FileInterface {
	public String readLines() throws IOException;
}

// Functional interfaces:
// Supplier, Function,
// Consumer, Runnable