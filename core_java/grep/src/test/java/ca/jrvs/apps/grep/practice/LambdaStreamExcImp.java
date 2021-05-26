package ca.jrvs.apps.grep.practice;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExcImp implements LambdaStreamExc {
    /**
     * Create a String stream from array
     * <p>
     * note: arbitrary number of value will be stored in an array
     *
     * @param strings
     * @return
     */
    @Override
    public Stream<String> createStrStream(String... strings) {
        Stream<String> strStream = Stream.of(strings);
        return strStream;
    }

    /**
     * Convert all strings to uppercase
     * please use createStrStream
     *
     * @param strings
     * @return
     */
    @Override
    public Stream<String> toUpperCase(String... strings) {
        Stream<String> toUpperStream = createStrStream(strings).map(element  -> element.toUpperCase());
        return toUpperStream;
    }

    /**
     * filter strings that contains the pattern
     * e.g.
     * filter(stringStream, "a") will return another stream which no element contains a
     *
     * @param stringStream
     * @param pattern
     * @return
     */
    @Override
    public Stream<String> filter(Stream<String> stringStream, String pattern) {
        Stream<String> fltStream = stringStream.filter(str -> str.contains(pattern));
        return fltStream;
    }

    /**
     * Create a intStream from a arr[]
     *
     * @param arr
     * @return
     */
    @Override
    public IntStream createIntStream(int[] arr) {
        IntStream intStr = Arrays.stream(arr);
        return intStr;
    }

    /**
     * Convert a stream to list
     *
     * @param stream
     * @return
     */
    @Override
    public <E> List<E> toList(Stream<E> stream) {
        List list = stream.collect(Collectors.toList());
        return list;
    }

    /**
     * Convert a intStream to list
     *
     * @param intStream
     * @return
     */
    @Override
    public List<Integer> toList(IntStream intStream) {
        List<Integer> intList = intStream.boxed().collect(Collectors.toList());
        return intList;
    }

    /**
     * Create a IntStream range from start to end inclusive
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public IntStream createIntStream(int start, int end) {
        IntStream intStr = IntStream.range(start, end+1);
        return intStr;
    }

    /**
     * Convert a intStream to a doubleStream
     * and compute square root of each element
     *
     * @param intStream
     * @return
     */
    @Override
    public DoubleStream squareRootIntStream(IntStream intStream) {
        DoubleStream dbStream = intStream.asDoubleStream().map(num -> Math.sqrt(num));
        return dbStream;
    }

    /**
     * filter all even number and return odd numbers from a intStream
     *
     * @param intStream
     * @return
     */
    @Override
    public IntStream getOdd(IntStream intStream) {
        IntStream oddStream = intStream.filter(num -> num%2 == 1);
        return oddStream;
    }

    /**
     * Return a lambda function that print a message with a prefix and suffix
     * This lambda can be useful to format logs
     * <p>
     * You will learn:
     * - functional interface http://bit.ly/2pTXRwM & http://bit.ly/33onFig
     * - lambda syntax
     * <p>
     * e.g.
     * LambdaStreamExc lse = new LambdaStreamImp();
     * Consumer<String> printer = lse.getLambdaPrinter("start>", "<end");
     * printer.accept("Message body");
     * <p>
     * sout:
     * start>Message body<end
     *
     * @param prefix prefix str
     * @param suffix suffix str
     * @return
     */
    @Override
    public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
        Consumer<String> strConsumer = (str) -> System.out.println(prefix + str + suffix);
        return strConsumer;
    }

    /**
     * Print each message with a given printer
     * Please use `getLambdaPrinter` method
     * <p>
     * e.g.
     * String[] messages = {"a","b", "c"};
     * lse.printMessages(messages, lse.getLambdaPrinter("msg:", "!") );
     * <p>
     * sout:
     * msg:a!
     * msg:b!
     * msg:c!
     *
     * @param messages
     * @param printer
     */
    @Override
    public void printMessages(String[] messages, Consumer<String> printer) {
        Stream.of(messages).forEach(printer);
    }

    /**
     * Print all odd number from a intStream.
     * Please use `createIntStream` and `getLambdaPrinter` methods
     * <p>
     * e.g.
     * lse.printOdd(lse.createIntStream(0, 5), lse.getLambdaPrinter("odd number:", "!"));
     * <p>
     * sout:
     * odd number:1!
     * odd number:3!
     * odd number:5!
     *
     * @param intStream
     * @param printer
     */
    @Override
    public void printOdd(IntStream intStream, Consumer<String> printer) {
        IntStream filtStream = intStream.filter(num -> num%2 == 1);
        filtStream.forEach(num -> printer.accept(String.valueOf(num)));
    }

    /**
     * Square each number from the input.
     * Please write two solutions and compare difference
     * - using flatMap
     *
     * @param ints
     * @return
     */
    @Override
    public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
        return ints.flatMap(num -> num.stream()).map(num -> num*num);
    }
}
