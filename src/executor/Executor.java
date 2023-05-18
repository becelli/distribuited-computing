package src.executor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import src.interfaces.Compute;
import src.interfaces.Task;

public class Executor implements Compute {

  public static void main(final String[] args) {
    try {
      final String name = "Executor";
      final Executor executor = new Executor();
      final Compute stub = (Compute) UnicastRemoteObject.exportObject(executor, 0);
      final Registry registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
      registry.rebind(name, stub);
      System.out.println("Executor is available for tasks.");
    } catch (Exception e) {
      System.err.println("Executor exception:");
      e.printStackTrace();
    }
  }

  public Executor() {
    super();
  }

  public <T> T executeTask(Task<T> task) {
    return task.execute();
  }
}
