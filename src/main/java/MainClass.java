import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import org.apache.log4j.Logger;

/**
 * Вариант 3 / Variant 3
 *
 * Необходимо разработать программу, которая получает на вход список ресурсов,
 * содержащих набор чисел, и считает сумму всех положительных четных.
 * Каждый ресурс должен быть обработан в отдельном потоке,
 * набор должен содержать лишь числа, унарный оператор "-" и пробелы.
 * Общая сумма должна отображаться на экране и изменяться в режиме реального времени.
 * Все ошибки должны быть корректно обработаны, все API покрыто модульными тестами.
 *
 * The task is to develop a program which receives a list of resources
 * containing a set of numbers, and computes the sum of all positive even ones.
 * Every resource has to be handled in a separate thread,
 * the set must contain just the numbers, unary operator "-" and spaces.
 * The total sum should be displayed at the screen and change in real time.
 * All the errors have to be correctly handled, all APIs should have module tests.
 *
 * @author Alexander Tsupko (tsupko.alexander@yandex.ru)
 *         Copyright (c) 2016. All rights reserved.
 */
public class MainClass {
    private static final Logger LOGGER = Logger.getLogger(MainClass.class);             // logging instance variable
    private static final File PATH = new File(String.format("%s/src/main/resources", System.getProperty("user.dir")));
                                                                                        // location of resources
    private static final int NUMBER = PATH.list(((dir, name) -> name.endsWith(".txt"))).length; // number of resources
    private static List<Future<Void>> list = new ArrayList<>();                         // list of results
    private static AtomicLong currentTotal = new AtomicLong(0L);                        // current result

    /**
     * The main method creates executor service of a fixed thread pool with number of threads equal to that of resources.
     * Using the new Java 8 Stream API allows quite efficiently articulate the solution to the problem at hand.
     * To make the access to the running total synchronized, we use {@code AtomicLong} variable.
     * Future developments will revolve about passing the paths to the resources as the command-line arguments.
     *
     * @param args for the moment being, the program does not require any command-line arguments
     */
    public static void main(String[] args) {
//        if (args.length == 0) {
//            throw new RuntimeException("Program requires at least one command-line argument");
//        }
//
//        for (String arg : args) {
//            long current = Long.parseLong(arg);
//            if (current > 0 && current % 2 == 0) {
//                currentTotal += current;
//                System.out.printf("Current Total: %d%n", currentTotal);
//            }
//        }
        ExecutorService executorService = null;
        try {
            executorService = Executors.newFixedThreadPool(NUMBER); // create thread pool according to resources quantity
            LOGGER.debug("New fixed thread pool created");
            for (int i = 1; i <= NUMBER; i++) {
                int finalI = i; // using the changing loop variable in a lambda requires it to be effectively final
                Future<Void> submit = executorService.submit(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        LOGGER.info("New task submitted");
                        try (BufferedReader bufferedReader = new BufferedReader(
                                new FileReader(String.format("%s/resource%d.txt", PATH, finalI)));
                             Stream<String> streamString = bufferedReader.lines()) {
                            LOGGER.info("New stream created and processed");
                            currentTotal.getAndAccumulate((streamString // creating stream of strings from the resource
                                    .parallel() // using parallel processing of the stream
                                    .mapToLong(Long::parseLong) // parsing strings to long primitive type
                                    .filter(e -> e > 0 && e % 2 == 0) // filtering the required values
                                    .reduce((left, right) -> left + right)) // adding filtered values together
                                    .getAsLong(), (left, right) -> left + right); // adding results to current total
                            return null;
                        }
                    }
                });
                list.add(submit); // adding tasks to the list enables waiting for their completion
                LOGGER.info("The task is added to the list");
            }
            for (Future<Void> submit : list) {
                if (!submit.isDone()) { // if the current task is not yet complete
                    try {
                        submit.get(); // wait for it to finish
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            if (executorService != null) executorService.shutdownNow(); // shutdown the executor service
            LOGGER.debug("Executor service is shutdown");
        }
        System.out.println(currentTotal.get()); // printing out the final result
    }

    /**
     * Getter method for the result of computation, converted to long primitive type.
     *
     * @return the result of computation
     */
    public static long getCurrentTotal() {
        return currentTotal.get();
    }
}
