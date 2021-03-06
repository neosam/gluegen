/**
 * Created on Sunday, March 28 2010 21:01
 */
package com.jogamp.common.util;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.System.*;

/**
 *
 * @author Michael Bien
 */
public class IntIntHashMapTest {

    private static int iterations;
    private static int[] rndKeys;
    private static int[] rndValues;

    @BeforeClass
    public static void init() {

        iterations = 20000;
        final int keySeed = 42;
        final int valueSeed = 23;

        Random keyRnd = new Random(/*keySeed*/);
        Random valueRnd = new Random(/*valueSeed*/);

        rndKeys = new int[iterations];
        rndValues = new int[iterations];
        for (int i = 0; i < iterations; i++) {
            rndValues[i] = valueRnd.nextInt();
            rndKeys[i] = keyRnd.nextInt();
        }

    }
    /**
     * Test of put method, of class IntIntHashMap.
     */
    @Test
    public void testPutRemove() {

        final IntIntHashMap intmap = new IntIntHashMap();
        final HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        // put
        for (int i = 0; i < iterations; i++) {
            intmap.put(rndKeys[i], rndValues[i]);

            assertTrue(intmap.containsValue(rndValues[i]));
            assertTrue(intmap.containsKey(rndKeys[i]));
        }

        for (int i = 0; i < iterations; i++) {
            map.put(rndKeys[i], rndValues[i]);
        }

        assertEquals(map.size(), intmap.size());

        for (Entry<Integer, Integer> entry : map.entrySet()) {
            assertTrue(intmap.containsKey(entry.getKey()));
            assertTrue(intmap.containsValue(entry.getValue()));
        }

        int i = 0;
        for (Entry<Integer, Integer> entry : map.entrySet()) {
            assertEquals((int)entry.getValue(), intmap.remove(entry.getKey()));
            assertEquals(map.size() - i - 1, intmap.size());
            i++;
        }

    }

    @Test
    public void benchmark() {

        // simple benchmark
        final IntIntHashMap intmap          = new IntIntHashMap(1024);
        final HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(1024);

        out.println("put");
        long time = currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            intmap.put(rndKeys[i], rndValues[i]);
        }
        long intmapTime = (currentTimeMillis() - time);
        out.println("   iimap: " + intmapTime+"ms");


        time = currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            map.put(rndKeys[i], rndValues[i]);
        }
        long mapTime = (currentTimeMillis() - time);
        out.println("   map:   " + mapTime+"ms");

        assertTrue(intmapTime <= mapTime);


        System.out.println();
        System.out.println("get");
        intmapTime = (currentTimeMillis() - time);
        out.println("   iimap: " + intmapTime+"ms");
        for (int i = 0; i < iterations; i++) {
            intmap.get(rndValues[i]);
        }
        
        mapTime = (currentTimeMillis() - time);
        out.println("   map:   " + mapTime+"ms");
        for (int i = 0; i < iterations; i++) {
            map.get(rndValues[i]);
        }
        assertTrue(intmapTime <= mapTime);


        out.println();
        out.println("remove");
        intmapTime = (currentTimeMillis() - time);
        out.println("   iimap: " + intmapTime+"ms");
        for (int i = 0; i < iterations; i++) {
            intmap.remove(rndValues[i]);
        }

        mapTime = (currentTimeMillis() - time);
        out.println("   map:   " + mapTime+"ms");
        for (int i = 0; i < iterations; i++) {
            map.remove(rndValues[i]);
        }

        assertTrue(intmapTime <= mapTime);
    }


}
