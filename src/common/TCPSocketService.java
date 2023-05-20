package src.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TCPSocketService {
  private final Socket socket;
  private final ObjectInputStream socketInput;
  private final ObjectOutputStream socketOutput;

  public TCPSocketService(final InetAddress address, final int port) throws IOException {
    this.socket = new Socket(address, port);
    this.socketInput = new ObjectInputStream(socket.getInputStream());
    this.socketOutput = new ObjectOutputStream(socket.getOutputStream());
  }

  public TCPSocketService(final Socket socket) throws IOException {
    this.socket = socket;
    this.socketOutput = new ObjectOutputStream(socket.getOutputStream());
    this.socketInput = new ObjectInputStream(socket.getInputStream());
  }

  public void send(final Object object) throws IOException {
    socketOutput.writeObject(object);
  }

  public Object receive() throws IOException, ClassNotFoundException {
    return socketInput.readObject();
  }

  public void close() throws IOException {
    socket.close();
  }
}
