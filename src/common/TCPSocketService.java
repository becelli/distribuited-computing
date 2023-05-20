package src.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TCPSocketService {
  private Socket socket;
  private ObjectInputStream socketInput;
  private ObjectOutputStream socketOutput;

  public TCPSocketService(InetAddress address, int port) throws Exception {
    this.socket = new Socket(address, port);
    this.socketInput = new ObjectInputStream(socket.getInputStream());
    this.socketOutput = new ObjectOutputStream(socket.getOutputStream());
  }

  public TCPSocketService(Socket socket) throws IOException {
    this.socket = socket;
    this.socketInput = new ObjectInputStream(socket.getInputStream());
    this.socketOutput = new ObjectOutputStream(socket.getOutputStream());
  }

  public void send(Object object) throws IOException {
    socketOutput.writeObject(object);
  }

  public Object receive() throws IOException, ClassNotFoundException {
    return socketInput.readObject();
  }

  public void close() throws IOException {
    socket.close();
  }
}
