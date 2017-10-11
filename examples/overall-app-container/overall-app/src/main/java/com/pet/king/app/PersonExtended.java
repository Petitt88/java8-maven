package com.pet.king.app;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonExtended extends Person {

	public <T extends String> void simpleGeneric(T param) {
		List<Integer> a = new ArrayList<>();
		a.stream().collect(Collectors.toList());
	}

	protected void simpleProt() {
		Pocket p = new Pocket();
		PocketStatic pStatic = new PocketStatic();

		super.boss();
	}
}
