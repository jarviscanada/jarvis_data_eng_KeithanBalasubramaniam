package ca.jrvs.practice.codingChallenge;

public class EvenOdd {
    public String evenOddMod(int a){
        return a % 2 == 0 ? "Even" : "Odd";
    }

    public String evenOddBit(int a){
        return (a ^ 1) == (a + 1) ? "Even" : "Odd";
    }
}
