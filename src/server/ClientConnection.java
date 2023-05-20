package src.server;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;

import src.common.ImageConverterService;
import src.common.TCPSocketService;
import src.server.services.FibonacciGeneratorService;
import src.server.services.ImageProcessingService;
import src.server.services.MandelbrotGeneratorService;

public class ClientConnection implements Runnable {
  private TCPSocketService socketService;

  public ClientConnection(Socket clientSocket) {
    try {
      this.socketService = new TCPSocketService(clientSocket);
    } catch (Exception e) {
      System.out.println(String.format("Failed to create socket: %s", e.getMessage()));
    }
  }

  public void run() {
    handleClient();
  }

  private void handleClient() {
    try {
      while (true) {
        String option = (String) socketService.receive();
        System.out.println(String.format("Received option: %s", option));

        switch (option) {
          case "IMAGE_PROCESSING" -> {
            handleImageProcessing();
          }
          case "MANDELBROT" -> {
            handleMandelbrot();
          }
          case "FIBONACCI" -> {
            handleFibonacci();
          }
        }
      }
    } catch (Exception e) {
      System.out.println(String.format("Failed to handle client: %s", e.getMessage()));
    }
  }

  private void handleImageProcessing() throws Exception {
    System.out.println("Handling image processing");

    String operation = (String) socketService.receive();
    System.out.println("Received option: " + operation);

    byte[] imageBytes = (byte[]) socketService.receive();
    BufferedImage image = ImageConverterService.bytesToImage(imageBytes);
    System.out.println("Image received and is null? " + (image == null));

    ImageProcessingService imageProcessor = new ImageProcessingService(operation, image);
    BufferedImage processedImage = imageProcessor.execute();

    byte[] processedImageBytes = ImageConverterService.imageToBytes(processedImage);
    socketService.send(processedImageBytes);
    System.out.println("Image processing done");
  }

  private void handleMandelbrot() throws IOException, ClassNotFoundException {
    System.out.println("Handling mandelbrot");

    Number[] params = (Number[]) socketService.receive();
    int width = params[0].intValue();
    int height = params[1].intValue();
    int maxIterations = params[2].intValue();

    System.out.println(String.format("Generating mandelbrot with width: %d, height: %d, maxIterations: %d", width,
        height, maxIterations));
    MandelbrotGeneratorService mandelbrotGenerator = new MandelbrotGeneratorService(width, height, maxIterations);
    BufferedImage image = mandelbrotGenerator.execute();

    System.out.println("Sending image");
    byte[] imageBytes = ImageConverterService.imageToBytes(image);
    socketService.send(imageBytes);

    System.out.println("Mandelbrot done");
  }

  private void handleFibonacci() throws IOException, ClassNotFoundException {
    System.out.println("Handling fibonacci");

    int n = (int) socketService.receive();
    System.out.println(String.format("Calculating the first %d fibonacci numbers", n));

    FibonacciGeneratorService fibonacciCalculator = new FibonacciGeneratorService(n);
    BigInteger[] result = fibonacciCalculator.execute();

    System.out.println("Sending result");
    socketService.send(result);

    System.out.println("Fibonacci done");
  }
}
