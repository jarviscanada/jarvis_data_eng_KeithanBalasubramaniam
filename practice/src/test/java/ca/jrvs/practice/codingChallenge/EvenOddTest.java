package ca.jrvs.practice.codingChallenge;

import junit.framework.TestCase;
import org.junit.Test;

public class EvenOddTest extends TestCase {

    @Test
    public void testEvenOddMod() {
        EvenOdd evenOdd = new EvenOdd();
        assertEquals("Even", evenOdd.evenOddMod(2));
        assertEquals("Odd", evenOdd.evenOddMod(5));
    }

    @Test
    public void testEvenOddBit() {
        EvenOdd evenOdd = new EvenOdd();
        assertEquals("Even", evenOdd.evenOddBit(2));
        assertEquals("Odd", evenOdd.evenOddBit(5));
    }
}