
package com.pet.king.app;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

import com.pet.king.app.car.Car;
import com.pet.king.app.car.GenericTester;

// import com.pet.king.util.SuperCalculator;

import hu.overall.lib.HeavyComputer;

@Trait(name = "Peter", names = { "Noni", "Fl�ri" })
@TraitDefault(27)
public class MyClass {

	public static void main(String[] args) {
		MyClass mc = new MyClass();
		mc.nestedClasses();
		mc.enumTest();
		try {
			mc.interfaceTest();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mc.optionalTest();
		mc.threadTest();
		try {
			mc.executorTest();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		mc.futureTest();
		mc.genericTest(new ArrayList<String>());
		mc.reflectionTest("K.P.");

		mc.callLib();
	}

	private void callLib() {
		String calculated = new HeavyComputer().computeFromNumbers(1, 2, 3, 4, 5, 6);
		System.out.println(String.format("Calculated from external lib: %s", calculated));

		boolean empty = StringUtils.isEmpty("alma");

		// SuperCalculator calc = new SuperCalculator();
		// Number result = calc.calc(1, 20);
		// System.out.println(String.format("Calculated from lib dependency: %s", result));
	}

	private void nestedClasses() {
		Person person = new Person();

		Person.Pocket pocket = person.new Pocket();
		Person.PocketStatic pocketStatic = new Person.PocketStatic();

		PersonExtended personExt = new PersonExtended();
		personExt.simpleProt();
		personExt.boss();
		personExt.bossProt();
	}

	void enumTest() {
		EnumTester tester = new EnumTester();
		tester.test();
	}

	protected void interfaceTest() throws IOException {
		InterfaceMindBlowUp ifOldFashioned = new InterfaceMindBlowUp() {
			@Override
			public void performAction() {
				System.out.println("Anonymous class implemets interface.");
			}
		};
		ifOldFashioned.performDefaulAction();

		InterfaceMindBlowUp ifFunctionalOne = () -> System.out.println("Lamba to interface implicit conversion by Java compiler");
		ifFunctionalOne.performAction();

		FileInterface fi = () -> {
			List<String> lines = Files.readAllLines(new File("").toPath());
			return String.join(", ", lines);
		};
		// fi.readLines();

		// Objects.
		// Arrays.asList(a)
		// Collections.emptySet()
		// Collections.unmodifiableList(list)
		// IntStream.of(values)

		boolean isEquals = InterfaceMindBlowUp.CONSTANT_VALUE.equals("alma");

		InterfaceMindBlowUp.InnerClass ifInnerClassObj = new InterfaceMindBlowUp.InnerClass();
		ifInnerClassObj.toString();
	}

	public void optionalTest() {
		Car car = new Car();
		// must check for null
		Integer res = car.parse("3");
		if (res != null) {

		}

		Optional<Integer> res2 = car.parseOptional("333");
		if (res2.isPresent()) {
			System.out.println(String.format("Optional value is: %s", res2.get().intValue()));
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		String[] sArr = /* new String[] */ { "apple", "android" };
	}

	private void threadTest() {
		int alma = 2;
		Thread thread = new Thread(() -> {
			System.out.println(String.format("Alma variable is currently now: \"%s\"", alma));
		});
		thread.setDaemon(false);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void executorTest() throws InterruptedException, ExecutionException {
		Person p = new Person();
		p.setAge(21);

		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(() -> {
			// System.out.println(num2);
			System.out.println("Age before: " + p.getAge());
			p.setAge(30);
			System.out.println("Age after: " + p.getAge());
			System.out.println("Parallelism: " + ForkJoinPool.getCommonPoolParallelism());
		});
		Future<Integer> future = executor.submit(() -> 2);
		System.out.println(future.get());
		// executor.invokeAll(tasks)
		// executor.invokeAny(tasks)

		// IntStream.range(startInclusive, endExclusive)
		// IntStream.of(values)
		try {
			System.out.println("attempt to shutdown executor");
			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.err.println("tasks interrupted");
		} finally {
			if (!executor.isTerminated()) {
				System.err.println("cancel non-finished tasks");
			}
			executor.shutdownNow();
			System.out.println("shutdown finished");
		}
	}

	private void futureTest() {
		CompletableFuture<Void> cfNoResult = CompletableFuture.runAsync(() -> {
			// Thread.sleep(10000);
			System.out.println("I'm async - start");
			// try {
			// TimeUnit.SECONDS.sleep(2);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			System.out.println("I'm async - end " + Thread.currentThread().getName());
		});
		cfNoResult.thenRunAsync(() -> System.out.println("Done Consumer, thread: " + Thread.currentThread().getName()));
		// cf.complete(null);

		CompletableFuture<Integer> cfWithResult = CompletableFuture.supplyAsync(() -> new Integer(50)).thenApplyAsync(res -> res + 50);
		cfWithResult.thenAccept(res -> System.out.println(String.format("Accumulated result: %s, thread: %s", res, Thread.currentThread().getName())));

		// this::functionName
		// Person::new
		// Person::staticFunctionName
		// Person::instanceFunction
		// person::instanceFunction

		IntStream.range(20, 30).mapToObj(i -> new Person(i)).map(Person::getAge).forEach(p -> System.out.print(" " + p));
		System.out.println();

		// Collections.unmodifiableList(list)
		// Collections.synchronizedList(list) // prefer to use the concurrent
		// package over this
		// Collections.emptyList()
	}

	private void genericTest(List<? extends String> list) {
		// int length = list.get(0).length();
		Type clazz = list.getClass().getGenericSuperclass();
		System.out.println("Generic super class: " + clazz.toString());

		GenericTester genTest = new GenericTester();
		genTest.LogGenericInfo("Peter", String.class);
		genTest.LogGenericInfo(27, Integer.class);
	}

	private void reflectionTest(String parameter1) {
		try {
			final Method method = MyClass.class.getDeclaredMethod("reflectionTest", String.class);
			// this will print out "parameter1" only if the java compiler is invoked with the "-parameters" argument
			Arrays.stream(method.getParameters()).map(p -> "ParameterName: " + p.getName()).forEach(System.out::println);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		System.err.println("ERROR - just kidding -:), only for demonstration purposes.");
	}

	// try-with-resource: those classes can be used that implement the AutoCloseable interface
}