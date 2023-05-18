package src.server;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
  public static void main(String[] args) {
    Integer portaServidor = 5000;

    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(portaServidor);
      while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Connection established with new client");
        ClientConnection clientConnection = new ClientConnection(clientSocket);
        Thread connectionThread = new Thread(clientConnection);
        connectionThread.start();
      }
    } catch (IOException e) {
      System.out.println("Erro: " + e.getMessage());
    } finally {
      try {
        serverSocket.close();
      } catch (IOException e) {
        System.out.println("Erro: " + e.getMessage());
      }
    }
  }
}

class ClientConnection implements Runnable {
  private DataOutputStream output;
  private BufferedReader input;
  private Socket clientSocket;

  public ClientConnection(Socket clientSocket) {
    try {
      this.clientSocket = clientSocket;
      this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      this.output = new DataOutputStream(clientSocket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    try {
      while (true) {
        String text = input.readLine();
        String mensagem = String.format("Mensagem recebida: %s", text);
        System.out.println("Mensagem recebida: " + mensagem);
      }
    } catch (IOException e) {
      System.out.println("Erro: " + e.getMessage());
    }
  }
}
