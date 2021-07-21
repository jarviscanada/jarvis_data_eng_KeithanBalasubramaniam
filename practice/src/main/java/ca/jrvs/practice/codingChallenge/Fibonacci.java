package ca.jrvs.practice.codingChallenge;

public class Fibonacci {

    public int fibonacciRecursion(int n){
        if (n <= 1){
            return n;
        }

        return fibonacciRecursion(n-1) + fibonacciRecursion(n-2);
    }

    public int fibonacciDynamic(int n){
        if (n<= 1){
            return n;
        }
        return dynamicHelper(n);
    }

    public int dynamicHelper(int n){
        int[] memory = new int[n];
        memory[0] = memory[1] = 1;
        for (int i= 2; i<=n-1 ;i++){
            memory[i] = memory[i-1] + memory[i-2];
        }
        return memory[n-1];
    }


}

