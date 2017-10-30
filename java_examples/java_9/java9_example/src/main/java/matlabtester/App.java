package matlabtester;

import com.mathworks.toolbox.javabuilder.MWException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class App {

	public static void main(String[] args) {
		try {
			ExecutorService executor = Executors.newFixedThreadPool(100);
			CompletableFuture.supplyAsync(() -> 3).thenAcceptAsync(res -> {

			}, executor);

			Supplier<Number> sup = () -> 28;
			Function<Number, String> func = num -> String.format("%s-%s", num, 5);
			Consumer<String> consumer = System.out::print;
			Runnable run = () -> {};

			//System.gc();

			SuperFunctions superFunctions = new SuperFunctions() {
				// converiant return type: base is Number --> this is Integer
				@Override public Integer invokeTest() {
					return 28;
				}
			};

			superFunctions.invokeTest();

			{
				int bax2 = 1232;
				String bananana;
			}

			// ImageIO

			throw new MWException();
		} catch (MWException e) {
			e.printStackTrace();
		}
	}
}

class ConsumerSuper {
	private final App app;
	private final SuperFunctions superFunctions;

	public ConsumerSuper(App app, SuperFunctions superFunctions) {
		this.app = app;
		this.superFunctions = superFunctions;
	}
}


@FunctionalInterface
interface SuperFunctions {
	Number invokeTest();

	default void doTest() {
		this.invokeTest();
	}
}

class Outer {

	private int age;

	class Inner {
		public Inner() {
			System.out.println(age + Outer.this.age);
		}
	}
}

@FunctionalInterface
interface TestFunc {

	void superMagic();

	default void magic() {
		System.out.println("Magic.");
	}

	default void megaMagic() {
		System.out.println("Mega Magic.");
		System.out.println("Mega Magic Again.");
	}

	static void invokeMagics() {
		TestFunc tf = () -> {};
		tf.magic();
		tf.megaMagic();
		tf.superMagic();
	}
}