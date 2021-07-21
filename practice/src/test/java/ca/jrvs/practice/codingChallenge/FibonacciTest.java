package ca.jrvs.practice.codingChallenge;

import junit.framework.TestCase;
import org.junit.Test;

public class FibonacciTest extends TestCase {

    @Test
    public void testFibonacciRecursion() {
        Fibonacci fib = new Fibonacci();
        assertEquals(3, fib.fibonacciRecursion(4));
    }

    public void testFibonacciDynamic() {
        Fibonacci fib = new Fibonacci();
        assertEquals(3, fib.fibonacciDynamic(4));
        assertEquals(34, fib.fibonacciDynamic(9));
    }

}