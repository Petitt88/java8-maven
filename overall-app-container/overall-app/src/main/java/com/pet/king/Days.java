package main.java.com.pet.king;

import java.util.EnumSet;
import java.util.Set;

interface DayOfWeek {
	boolean isWeekend();
}

// enums are special classes, they can implment interfaces
// but cannot extends classes. That is because every enum implicitly inherits
// from Enum<T> (java compiler generates this)
// a restriction is that enums can have only private ctors
public enum Days implements DayOfWeek {
	MONDAY(false), TUESDAY(true), SUNDAY(true);

	private boolean _isWeekend;

	private Days(boolean isWeekend) {
		this._isWeekend = isWeekend;
	}

	@Override
	public boolean isWeekend() {
		return this._isWeekend;
	}

	class DayClass {

	}

	public static class DayClassStatic {

	}

}

enum DaysOfTheWeek {
	MONDAY(), TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

	// DaysOfTheWeek(){
	//
	// }
}

enum FlagEnum {
	NONE(0), WEAK(1), STRONG(2), MEGA(4);

	public int id;

	FlagEnum(int id) {
		this.id = id;
	}
}

class EnumTester {
	protected EnumTester() {
	}

	void test() {
		DayOfWeek day = Enum.valueOf(Days.class, Days.MONDAY.name());
		System.out.println(String.format("Name of Monday is: %s", day.toString()));

		final Set<Days> dayAll = EnumSet.allOf(Days.class);

		System.out.println("FlagEnum test, (value should be 6): " + (FlagEnum.STRONG.id | FlagEnum.MEGA.id));
	}
}