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
  private ExecutorService executorService;

  public ClientConnection(final ExecutorService executorService, final Socket clientSocket) {
    try {
      this.executorService = executorService;
      this.socketService = new TCPSocketService(clientSocket);
    } catch (final Exception e) {
      System.out.println(String.format("Failed to create socket: %s", e.getMessage()));
    }
  }

  public void run() {
    handleClient();
  }

  private void handleClient() {
    try {
      while (true) {
        final String option = (String) socketService.receive();
        System.out.println(String.format("Received option: %s", option));

        System.out.println("-----------------------------");
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
        System.out.println("-----------------------------");
      }
    } catch (final Exception e) {
      System.out.println(String.format("Failed to handle client: %s", e.getMessage()));
    }
  }

  private void handleImageProcessing() throws Exception {
    System.out.println("Handling image processing");

    final String operation = (String) socketService.receive();
    System.out.println("Received option: " + operation);

    final byte[] imageBytes = (byte[]) socketService.receive();
    final BufferedImage image = ImageConverterService.bytesToImage(imageBytes);
    System.out.println("Image received and is null? " + (image == null));

    final ImageProcessingService imageProcessor = new ImageProcessingService(operation, image);
    final byte[] processedImage = executorService.executeTask(imageProcessor);

    socketService.send(processedImage);
    System.out.println("Image processing done");
  }

  private void handleMandelbrot() throws IOException, ClassNotFoundException {
    System.out.println("Handling mandelbrot");

    final Number[] params = (Number[]) socketService.receive();
    final int width = params[0].intValue();
    final int height = params[1].intValue();
    final int maxIterations = params[2].intValue();

    System.out.println(String.format("Generating mandelbrot with width: %d, height: %d, maxIterations: %d", width,
        height, maxIterations));
    final MandelbrotGeneratorService mandelbrotGenerator = new MandelbrotGeneratorService(width, height, maxIterations);
    final byte[] imageAsBytes = executorService.executeTask(mandelbrotGenerator);

    System.out.println("Sending image");
    socketService.send(imageAsBytes);

    System.out.println("Mandelbrot done");
  }

  private void handleFibonacci() throws IOException, ClassNotFoundException {
    System.out.println("Handling fibonacci");

    final int n = (int) socketService.receive();
    System.out.println(String.format("Calculating the first %d fibonacci numbers", n));

    final FibonacciGeneratorService fibonacciCalculator = new FibonacciGeneratorService(n);
    final BigInteger[] result = executorService.executeTask(fibonacciCalculator);

    System.out.println("Sending result");
    socketService.send(result);

    System.out.println("Fibonacci done");
  }
}
