package ca.jrvs.apps.practice;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.*;
public class LambdaStreamExcImpTest{

    private LambdaStreamExcImp lambdaStreamExcImp;

    @Before
    public void setUp() throws Exception {
        lambdaStreamExcImp = new LambdaStreamExcImp();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void createStrStream() {
        System.out.println("Test case: test createStrStream method");
        Stream<String> testStream = lambdaStreamExcImp.createStrStream("a", "b", "c");
        String str = testStream.collect(Collectors.joining(""));
        assertEquals("The expected output is abc", "abc", str);
    }

    @Test
    public void toUpperCase() {
        System.out.println("Test case: test toUpperCase method");
        Stream<String> testStream = lambdaStreamExcImp.toUpperCase("a", "b", "c");
        String str = testStream.collect(Collectors.joining(""));
        assertEquals("The expected output is ABC", "ABC", str);
    }

    @Test
    public void filter() {
        System.out.println("Test case: test filter method");
        Stream<String> testStream = lambdaStreamExcImp.createStrStream("a", "ab", "c", "ca");
        String pattern = "a";
        String str = lambdaStreamExcImp.filter(testStream, pattern).collect(Collectors.joining(""));
        assertEquals("The expected outcome is c", "c", str);
    }

    @Test
    public void createIntStream() {
        System.out.println("Test case: test createIntStream method");
        int[] arr = {1, 2, 3};
        IntStream testStream = lambdaStreamExcImp.createIntStream(arr);
        assertEquals("The expected outcome will sum to 6", 6, testStream.sum());
    }

    @Test
    public void toList() {
        System.out.println("Test case: test toList method");
        Stream<String> testStream = lambdaStreamExcImp.createStrStream("a" ,"b", "c");
        List<String> testList = Arrays.asList("a", "b", "c");
        assertEquals("The expected outcome is abc", testList, lambdaStreamExcImp.toList(testStream));
    }

    @Test
    public void squareRootIntStream() {
        System.out.println("Test case: test squareRootIntStream method");
        int[] arr = {1, 4, 9};
        IntStream intStream = lambdaStreamExcImp.createIntStream(arr);
        DoubleStream doubleStream = lambdaStreamExcImp.squareRootIntStream(intStream);
        assertEquals("The expected outcome will sum to 6.0", 6.0, doubleStream.sum(), 0.0);
    }

    @Test
    public void getOdd() {
        System.out.println("Test case: test getOdd method");
        IntStream intStream = lambdaStreamExcImp.createIntStream(1, 5);
        assertEquals("The expected outcome will sum to 9", 9, lambdaStreamExcImp.getOdd(intStream).sum());
    }

    @Test
    public void getLambdaPrinter() {
        System.out.println("Test case: test getLambdaPrinter method");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Consumer<String> printer = lambdaStreamExcImp.getLambdaPrinter("1:", ":2");
        printer.accept("Hello");
        assertEquals("The expected outcome should be 1:Hello:2", "1:Hello:2\n", out.toString());
    }

    @Test
    public void printMessages() {
        System.out.println("Test case: test printMessages method");
        String[] testArr = {"1", "2", "3"};
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Consumer<String> printer = lambdaStreamExcImp.getLambdaPrinter("*", "*");
        lambdaStreamExcImp.printMessages(testArr, printer);
        assertEquals("The expected outcome is *1*\n*2*\n*3*\n", "*1*\n*2*\n*3*\n", out.toString());
    }

    @Test
    public void printOdd() {
        System.out.println("Test case: test printOdd method");
        int[] testArr = {1, 2, 3};
        IntStream testStream = lambdaStreamExcImp.createIntStream(testArr);
        Consumer<String> printer = lambdaStreamExcImp.getLambdaPrinter("*", "*");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        lambdaStreamExcImp.printOdd(testStream, printer);
        assertEquals("The expected outcome is *1*\n*3*\n", "*1*\n*3*\n", out.toString());

    }

    @Test
    public void flatNestedInt() {
        System.out.println("Test case: test flatNestedInt method");
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(4, 5, 6);
        Stream<List<Integer>> testStream = Arrays.asList(list1, list2).stream();
        int sum = lambdaStreamExcImp.flatNestedInt(testStream).reduce(0, Integer::sum);
        assertEquals("The expected outcome should sum to 91", 91, sum);
    }
}