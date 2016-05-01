package main.java.com.pet.king.car;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GenericTester {

	private final List<String> list = new ArrayList<>();

	public GenericTester() {
		try {
			ParameterizedType type = (ParameterizedType) this.getClass().getDeclaredField("list").getGenericType();
			Type[] argumentType = type.getActualTypeArguments();
			System.out.println(String.format("The inner field's generic type is: %s", type.toString()));
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}

	public <T> void LogGenericInfo(T param, Class<T> info) {
		System.out.println("Generic type is: " + info.getCanonicalName());
	}
}
