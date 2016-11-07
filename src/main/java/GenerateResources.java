import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Random;

/**
 * The class serves for generating the specified number of resources (currently, text files)
 * containing the specified (currently, the same as number of resources) number of integers per resource,
 * randomly distributed in the range from {@code Integer.MIN_VALUE + 2} to {@code Integer.MAX_VALUE - 1}.
 *
 * @author Alexander Tsupko (tsupko.alexander@yandex.ru)
 *         Copyright (c) 2016. All rights reserved.
 */
public class GenerateResources {
    private static int number = 0;                              // current number of resources
    private static int size = Integer.MAX_VALUE >> 27; // number of integers per resource (15)

    private static final Random RANDOM = new Random();      // variable for generating random values
    private static final File PATH = new File(System.getProperty("user.dir") + "/src/main/resources"); // location
                                                                                                // of the resources

    /**
     * The main method either takes a single parameter which signifies the desirable numbers of resources
     * and integers per resource, or takes none, in which case the default value mentioned above is used.

     * @param args if present, specifies the number of resources and
     *             the number of integers generated per resource;
     *             otherwise, default value is used.
     *             The recommended values range between 1 and about 31.
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            size = Integer.parseInt(args[0]);
        } else if (args.length > 1) return;
        while (size > number++) {
            try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(PATH + "/resource" + number + ".txt"))) {
                for (int i = 0; i < size; i++) {
                    printWriter.println(
                            (int)(Math.random() * Integer.MAX_VALUE) * (RANDOM.nextBoolean() ? 1 : -1)
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}