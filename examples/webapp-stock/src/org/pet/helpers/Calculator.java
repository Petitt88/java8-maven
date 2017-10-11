package org.pet.helpers;

import hu.overall.lib.HeavyComputer;

public class Calculator {

	public String calc() {
		HeavyComputer computer = new HeavyComputer();
		return computer.computeFromNumbers(1, 2, 3);
	}
}
