package src.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.security.InvalidParameterException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import src.common.ImageConverterService;
import src.common.TCPSocketService;

public class Client {
  public static void main(final String args[]) {
    try {
      final Client client = new Client();
      client.loop();
    } catch (final Exception e) {
      System.out.println("Failed to start client: " + e.getMessage());
    }
  }

  private final TCPSocketService socketService;
  private final InputReaderService inputReaderService;
  private Number[] mandelbrotParams;

  private Client() throws Exception {
    this.socketService = new TCPSocketService(InetAddress.getLocalHost(), 5000);
    this.inputReaderService = new InputReaderService();
  }

  private void loop() {
    while (true) {
      printMenu();

      try {
        optionHandler();
      } catch (final Exception e) {
        System.out.println("Failed to handle option: " + e.getMessage());
      }
    }
  }

  private void printMenu() {
    System.out.println("Available options:");
    System.out.println("A - Image Processing");
    System.out.println("B - Mandelbrot generator");
    System.out.println("C - Fibonacci calculator");
  }

  private void optionHandler() throws ClassNotFoundException, IOException {
    final String option = inputReaderService.readString();
    switch (option) {
      case "A" -> {
        handleImageProcessing();
      }
      case "B" -> {
        handleMandelbrot();
      }
      case "C" -> {
        handleFibonacci();
      }
      default -> {
        throw new InvalidParameterException("Invalid option");
      }
    }
  }

  private void handleImageProcessing() throws IOException, ClassNotFoundException {
    System.out.println("Choose an image processing operation:");
    System.out.println("1 - Grayscale");
    System.out.println("2 - Blur");
    String operation = null;
    switch (inputReaderService.readString()) {
      case "1" -> operation = "grayscale";
      case "2" -> operation = "blur";
      default -> throw new InvalidParameterException("Invalid operation");
    }

    System.out.println("Type the path of the image to be processed:");

    final String inputImagePath = inputReaderService.readString();

    System.out.println("Reading image...");
    final BufferedImage inputImage = ImageIO.read(new File(inputImagePath));

    System.out.println("Sending image to server...");
    final byte[] imageBytes = ImageConverterService.imageToBytes(
        inputImage);
    socketService.send("IMAGE_PROCESSING");
    socketService.send(operation);
    socketService.send(imageBytes);

    System.out.println("Receiving the processed image...");
    final byte[] outputImageBytes = (byte[]) socketService.receive();
    final BufferedImage outputImage = ImageConverterService.bytesToImage(outputImageBytes);

    System.out.println("Type the path to save the processed image:");
    final String outputImagePath = inputReaderService.readString();

    System.out.println("Saving the processed image...");
    ImageIO.write(outputImage, "png", new File(outputImagePath));

    System.out.println("Image saved successfully");
  }

  private void handleMandelbrot() throws IOException, ClassNotFoundException {
    System.out.println("What is the width of the image?");
    final int width = Integer.parseInt(inputReaderService.readString());

    System.out.println("What is the height of the image?");
    final int height = Integer.parseInt(inputReaderService.readString());

    System.out.println("What is the maximum number of iterations?");
    final int maxIterations = Integer.parseInt(inputReaderService.readString());

    System.out.println("Sending request to server...");
    socketService.send("MANDELBROT");
    mandelbrotParams = new Number[] { width, height, maxIterations };
    socketService.send(mandelbrotParams);

    System.out.println("Receiving the mandelbrot image...");
    final byte[] outputImageBytes = (byte[]) socketService.receive();
    final BufferedImage outputImage = ImageConverterService.bytesToImage(outputImageBytes);

    System.out.println("Type the path to save the mandelbrot image:");
    final String outputImagePath = inputReaderService.readString();

    System.out.println("Saving the mandelbrot image...");
    ImageIO.write(outputImage, "png", new File(outputImagePath));

    System.out.println("Mandelbrot image saved successfully");
  }

  private void handleFibonacci() throws IOException, ClassNotFoundException {
    System.out.println("How many numbers do you want to calculate?");
    final int number = Integer.parseInt(inputReaderService.readString());

    System.out.println("Sending request to server...");
    socketService.send("FIBONACCI");
    socketService.send(number);

    System.out.println("Receiving the fibonacci sequence...");
    final BigInteger[] fibonacciSequence = (BigInteger[]) socketService.receive();

    System.out.println("Saving the fibonacci sequence...");

    final String output = Stream.iterate(0, i -> i + 1)
        .limit(fibonacciSequence.length)
        .map(i -> String.format("%d: %d\n", i + 1, fibonacciSequence[i]))
        .collect(Collectors.joining());

    System.out.println("Type the path to save the fibonacci sequence:");
    final String outputFilePath = inputReaderService.readString();

    final File outputFile = new File(outputFilePath);
    try {
      final FileWriter fileWriter = new FileWriter(outputFile);
      fileWriter.write(output);
      fileWriter.close();
    } catch (final Exception e) {
      System.out.println("Failed to save fibonacci sequence: " + e.getMessage());
    }
  }
}

class InputReaderService {
  private final Scanner scanner;

  public InputReaderService() {
    this.scanner = new Scanner(System.in);
  }

  public String readString() {
    return scanner.nextLine();
  }
}
