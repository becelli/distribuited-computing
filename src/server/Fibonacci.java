package src.server;

import java.io.Serializable;
import java.math.BigInteger;

import src.interfaces.Task;

public class Fibonacci implements Task<BigInteger[]>, Serializable {
  public static void main(final String[] args) {
    final Fibonacci fibonacci = new Fibonacci(1000);
    final BigInteger[] fibonacciNumbers = fibonacci.calculateFibonacciNumbers();

    for (int i = 0; i < fibonacciNumbers.length; i++) {
      System.out.println(String.format("Fibonacci term %d: %d", i, fibonacciNumbers[i]));
    }

  }

  private final int termCount;

  public Fibonacci(final int termCount) {
    this.termCount = termCount;
  }

  public BigInteger[] execute() {
    return calculateFibonacciNumbers();
  }

  public BigInteger[] calculateFibonacciNumbers() {
    final BigInteger[] fibonacciNumbers = new BigInteger[termCount];
    BigInteger firstTerm = BigInteger.ZERO;
    BigInteger secondTerm = BigInteger.ONE;

    for (int i = 0; i < termCount; i++) {
      fibonacciNumbers[i] = firstTerm;
      final BigInteger sum = firstTerm.add(secondTerm);
      firstTerm = secondTerm;
      secondTerm = sum;
    }

    return fibonacciNumbers;
  }
}
