import java.net.*;
import java.io.*;

public class TCPClient {
  private Socket socket;
  private DataInputStream input;
  private DataOutputStream output;

  private TCPClient(String hostname, int port) {
    this.createSocket();
  }

  private void createSocket() {
    try {
      this.socket = new Socket(hostname, port);
      this.input = new DataInputStream(socket.getInputStream());
      this.output = new DataOutputStream(socket.getOutputStream());
    } catch (UnknownHostException e) {
      System.out.println("Socket:" + e.getMessage());
    } catch (EOFException e) {
      System.out.println("EOF:" + e.getMessage());
    }
  }

  private void loop() {
    while (true) {
      try {
        output.writeUTF(args[0]); // UTF is a string encoding see Sn. 4.4
        String data = input.readUTF(); // read a line of data from the stream
        System.out.println("Received: " + data);
      } catch (UnknownHostException e) {
        System.out.println("Socket:" + e.getMessage());
      }
    }
  }

  public static void main(String args[]) {
    int serverPort = 5000;
    String hostname = args[1];
    new TCPClient(hostname, serverPort);
  }
}
