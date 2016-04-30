package main.java.com.pet.king.car;

public class GenericTester {

	public <T> void LogGenericInfo(T param, Class<T> info) {
		System.out.println("Generic type is: " + info.getCanonicalName());
	}
}
