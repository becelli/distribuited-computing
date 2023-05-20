package src.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

import src.common.ImageConverterService;
import src.common.TCPSocketService;

public class Server {
  public static void main(String args[]) {
    try {
      Server server = new Server(5000);
      server.listen();
    } catch (Exception e) {
      System.out.println("Failed to create server: " + e.getMessage());
    }
  }

  private ServerSocket serverSocket;

  public Server(int port) throws Exception {
    serverSocket = new ServerSocket(port);
  }

  private void listen() throws Exception {
    System.out.println(String.format("Server listening on port %d", this.serverSocket.getLocalPort()));
    while (true) {
      Socket clientSocket = serverSocket.accept();
      System.out.println("Connection established with new client");
      handleNewClient(clientSocket);
    }
  }

  private void handleNewClient(Socket clientSocket) {
    ClientConnection clientConnection = new ClientConnection(clientSocket);
    Thread connectionThread = new Thread(clientConnection);
    connectionThread.start();
  }
}

class ClientConnection implements Runnable {
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
          case "B" -> {
            handleMandelbrot();
          }
          case "C" -> {
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

    ImageProcessor imageProcessor = new ImageProcessor("grayscale", image);
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
    double zoom = params[2].doubleValue();
    double x_offset = params[3].doubleValue();
    double y_offset = params[4].doubleValue();
    int maxIterations = params[5].intValue();


    MandelbrotGenerator mandelbrotGenerator = new MandelbrotGenerator(width, height, zoom, x_offset, y_offset,
        maxIterations);
    BufferedImage image = mandelbrotGenerator.execute();

    byte[] imageBytes = ImageConverterService.imageToBytes(image);
    socketService.send(imageBytes);
  }
}
