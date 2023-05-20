package src.server.services;

import java.io.Serializable;
import java.math.BigInteger;

import src.interfaces.Task;

public class FibonacciGeneratorService implements Task<BigInteger[]>, Serializable {
  public static final long serialVersionUID = 1L;
  private final int termCount;

  public FibonacciGeneratorService(final int termCount) {
    this.termCount = termCount;
  }

  public BigInteger[] execute() {
    System.out.println(String.format("I'm generating %d fibonacci numbers!", termCount));
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
