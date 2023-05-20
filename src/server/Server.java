package src.server;

import java.net.ServerSocket;
import java.net.Socket;

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
