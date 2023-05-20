package src.server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  public static void main(final String args[]) {
    try {
      final Server server = new Server(5000);
      server.listen();
    } catch (final Exception e) {
      System.out.println(String.format("Failed to start server: %s", e.getMessage()));
    }
  }

  private final ServerSocket serverSocket;
  private final ExecutorService executorService;

  public Server(final int port) throws Exception {
    serverSocket = new ServerSocket(port);
    executorService = new ExecutorService();
  }

  private void listen() throws Exception {
    System.out.println(String.format("Server listening on port %d", this.serverSocket.getLocalPort()));
    while (true) {
      final Socket clientSocket = serverSocket.accept();
      System.out.println("Connection established with new client");
      handleNewClient(clientSocket);
    }
  }

  private void handleNewClient(final Socket clientSocket) {
    final ClientConnection clientConnection = new ClientConnection(executorService, clientSocket);
    final Thread connectionThread = new Thread(clientConnection);
    connectionThread.start();
  }
}
