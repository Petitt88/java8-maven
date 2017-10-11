package webshop.common;

import java.util.HashMap;
import java.util.Map;

public class Session<T> {

	private final Map<String, T> _items = new HashMap<>();

	public void store(String key, T item) {
		_items.put(key, item);
	}

	public T get(String key) {
		return _items.get(key);
	}

	public void delete(T key) {
		_items.remove(key);
	}

	public int size() {
		return _items.size();
	}
}
