import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

/**
 * @author Alexander Tsupko (tsupko.alexander@yandex.ru)
 *         Copyright (c) 2016. All rights reserved.
 */
public class MainClassTest {
    /**
     * The test checks the correct answer computed manually is the same as computed by the program.
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void main_test() throws Exception {
        MainClass.main(null);
        Assert.assertEquals(69785181432L, MainClass.getCurrentTotal());
    }
}