package src.executor;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import src.interfaces.Compute;
import src.interfaces.Task;

public class Executor implements Compute {
  public static void main(final String[] args) {
    final int port = 1099;
    final Executor executorInstance = new Executor();

    try {
      final String host = InetAddress.getLocalHost().getHostAddress();
      final Compute executor = (Compute) UnicastRemoteObject.exportObject(executorInstance, 0);
      final Registry registry = LocateRegistry.getRegistry(host, port);
      registry.rebind("Executor", executor);
      System.out.println("Executor is available for tasks.");
    } catch (final Exception e) {
      System.err.println(String.format("Executor exception: %s", e.getMessage()));
    }
  }

  public Executor() {
    super();
  }

  public <T> T executeTask(final Task<T> task) throws RemoteException {
    System.out.println("-----------------------------");
    System.out.println("Executor is executing a task.");
    final T result = task.execute();
    System.out.println("Executor has finished executing a task.");
    System.out.println("-----------------------------");
    return result;

  }
}
