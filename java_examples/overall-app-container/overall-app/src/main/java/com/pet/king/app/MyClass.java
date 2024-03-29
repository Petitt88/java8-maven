
package com.pet.king.app;

import com.pet.king.app.car.Car;
import com.pet.king.app.car.GenericTester;
import com.pet.king.app.car.Tuple;
import com.pet.king.app.car.TupleGeneric;
import com.pet.king.jaxrs.JerseyTest;
import com.pet.king.util.SuperCalculator;
import hu.overall.lib.HeavyComputer;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Trait(name = "Pet", names = {"Non", "Flor"})
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
		mc.genericTest(new ArrayList<>());
		mc.reflectionTest("K.P.");
		mc.streamAndCurryingTest();
		mc.instanceOfTest();

		mc.callLib();

		new XmlExample().execute();
		new JerseyTest().execute();
	}

	private void callLib() {
		String calculated = new HeavyComputer().computeFromNumbers(1, 2, 3, 4, 5, 6);
		System.out.println(String.format("Calculated from external lib: %s", calculated));

		boolean empty = StringUtils.isEmpty("alma");

		SuperCalculator calc = new SuperCalculator();
		Number result = calc.calc(1, 20);
		System.out.println(String.format("Calculated from lib dependency (through parent POM): %s", result));
	}

	private void nestedClasses() {
		Person person = new Person();

		Person.Pocket pocket = person.new Pocket();
		Person.PocketStatic pocketStatic = new Person.PocketStatic();
	}

	void enumTest() {
		System.out.println("-----------------enumTest-----------------");
		EnumTester tester = new EnumTester();
		tester.test();
	}

	void visibilityTest() {
		System.out.println("-----------------visibilityTest-----------------");
		PersonExtended personExt = new PersonExtended();
		personExt.simpleProt();
		personExt.boss();
		// can invoke package-private and protected members (from the same package)
		personExt.bossProt();
	}

	void interfaceTest() throws IOException {
		System.out.println("-----------------interfaceTest-----------------");
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
		// Collectors.toSet();
		// Collectors.toList();

		boolean isEquals = InterfaceMindBlowUp.CONSTANT_VALUE.equals("alma");

		InterfaceMindBlowUp.InnerClass ifInnerClassObj = new InterfaceMindBlowUp.InnerClass();
		ifInnerClassObj.toString();
	}

	void optionalTest() {
		System.out.println("-----------------optionalTest-----------------");
		Car car = new Car();
		// must check for null
		Integer res = car.parse("3");

		Optional<Integer> optNoGo = car.parseOptional("333");
		optNoGo.ifPresent(integer -> System.out.println(String.format("Optional value is: %s", integer)));

		Integer optGo = car.parseOptional(null).orElse(666);
		System.out.println(String.format("orElse optional value is: %s", optGo));

		Optional.of(4)
				.filter(a -> a == 4)
				.flatMap(a -> Optional.of(String.format("Monad(%s) - Optional", a)))
				.ifPresent(System.out::println);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		String[] sArr = /* new String[] */ {"apple", "android"};
	}

	void threadTest() {
		System.out.println("-----------------threadTest-----------------");
		int alma = 2;
		Thread thread = new Thread(() -> System.out.println(String.format("Alma variable is currently now: \"%s\"", alma)));
		thread.setDaemon(false);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	void executorTest() throws InterruptedException, ExecutionException {
		System.out.println("-----------------streamAndCurryingTest-----------------");
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

		// Stream.of(future).map(a -> a.get()).forEach(System.out::println);

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

	private IntStream calculate(IntStream stream, int a, int b) {
		// x is b,
		// y is a
		// z is the iterator variable (always the last lambda's parameter)
		return stream.map(((IntFunction<IntFunction<IntUnaryOperator>>) x -> y -> z -> x + y * z).apply(b).apply(a));
	}

	void streamAndCurryingTest() {
		System.out.println("-----------------streamAndCurryingTest-----------------");
		// IntStream.range(startInclusive, endExclusive)
		// IntStream.of(values)

		// IntStream alma = IntStream.of(1, 2, 4);
		// alma.boxed().collect(Collectors.toSet());

		IntStream stream = IntStream.of(1, 2, 3, 4, 5);
		IntStream newStream = calculate(stream, 3, 4);
		System.out.println(newStream.boxed().collect(Collectors.toList()));

		// iterate: 1st element is "1", next elements are the "previous + 1"
		// limit: take only the first 4 elements
		// reduce: applying an operation to each element of the list, resulting in the combination of this element and the
		// result of the same operation applied to the previous element operation applied to the previous element
		Integer res = Stream
				.iterate(1, x -> x + 3)
				// to demonstrate how streams are evaluated - elements are only iterated once!
				.peek(System.out::println)
				.limit(4)
				.reduce((prev, i) -> {
					System.out.println(String.format("%s %s", prev, i));
					return prev + i;
				})
				.orElse(2);
		System.out.println(String.format("Integer stream magic result is: %s", res));

		/*
		 * Streams should be used with high caution when processing intensive computation tasks. In particular,
		 * by default, all streams will use the SAME! ForkJoinPool (which happens to be ForkJoinPool.commonPool()),
		 * configured to use as many threads as there are cores in the computer on which the program is running.
		 * 
		 * If evaluation of one parallel stream results in a very long running task, this may be split into as many long
		 * running sub-tasks that will be distributed to each thread in the pool. From there, no other parallel stream can
		 * be processed because all threads will be occupied. So, for computation intensive stream evaluation, one
		 * should always use a specific ForkJoinPool in order not to block other streams.
		 * 
		 * This way, other parallel streams (using their own ForkJoinPool) will not be blocked by this one.
		 * In other words, we would need a pool of ForkJoinPool in order to avoid this problem.
		 * 
		 * If a program is to be run inside a container, one must be very careful when using parallel streams.
		 * Never use the default pool in such a situation unless you know for sure that the container can handle it.
		 * In a Java EE container, do not use parallel streams.
		 */
		try {
			List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
			Stream<Integer> listStream = list.parallelStream().map(/* this::veryLongProcessing */ a -> a);
			Callable<List<Integer>> task = () -> listStream.collect(Collectors.toList());
			ForkJoinPool forkJoinPool = new ForkJoinPool(4);
			List<Integer> newList = forkJoinPool.submit(task).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	void instanceOfTest() {
		System.out.println("-----------------testInstanceOf-----------------");

		Tuple tuple = new Tuple(15, "Peter");
		System.out.println(tuple instanceof Tuple);

		TupleGeneric<Integer, String> tupleGen1 = new TupleGeneric<>(15, "Peter");
		System.out.println(tupleGen1 instanceof TupleGeneric);
	}

	void futureTest() {
		System.out.println("-----------------futureTest-----------------");
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

		CompletableFuture<Integer> cfWithResult = CompletableFuture.supplyAsync(() -> (50)).thenApplyAsync(res -> res + 50);
		cfWithResult.thenAccept(res -> System.out.println(String.format("Accumulated result: %s, thread: %s", res, Thread.currentThread().getName())));

		// this::functionName
		// Person::new
		// Person::staticFunctionName
		// Person::instanceFunction
		// person::instanceFunction

		IntStream.range(20, 30).mapToObj(Person::new).map(Person::getAge).forEach(p -> System.out.print(" " + p));
		System.out.println();

		// Collections.unmodifiableList(list)
		// Collections.synchronizedList(list) // prefer to use the concurrent
		// package over this
		// Collections.emptyList()
		// Files.readAllLines(path)
	}

	void genericTest(List<? extends String> list) {
		System.out.println("-----------------genericTest-----------------");
		// int length = list.get(0).length();
		Type clazz = list.getClass().getGenericSuperclass();
		System.out.println("Generic super class: " + clazz.toString());

		GenericTester genTest = new GenericTester();
		genTest.LogGenericInfo("Peter", String.class);
		genTest.LogGenericInfo(27, Integer.class);
	}

	void reflectionTest(String parameter1) {
		System.out.println("-----------------reflectionTest-----------------");
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