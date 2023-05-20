package src.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import src.interfaces.Compute;
import src.interfaces.Task;

public class ExecutorService {
  private final Compute compute;

  public ExecutorService() throws UnknownHostException, RemoteException, NotBoundException {
    final String name = "Executor";
    final String host = InetAddress.getLocalHost().getHostAddress();
    final int port = 1099;

    final Registry registry = LocateRegistry.getRegistry(host, port);
    compute = (Compute) registry.lookup(name);
  }

  public <T> T executeTask(final Task<T> task) throws RemoteException {
    return compute.executeTask(task);
  }
}
