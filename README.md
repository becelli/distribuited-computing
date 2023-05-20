### Distributed and Parallel Computing

Formerly known as "Computação distribuída e paralela"

#### Objective

The objective of this project is to develop distributed computing programs using Java as the programming language.

#### Services

There are three available services, which are not overly complex but are sufficient for the intended purpose. These services involve computationally heavy tasks, justifying the need for remote computing. The available services are:

- Image processing
- Mandelbrot fractal generator
- Fibonacci numbers

#### Running the Programs

To run these programs, follow these steps:

1. Navigate to the project root folder.
2. Start the Java RMI Registry by executing the following command in the terminal:
   ```shell
   rmiregistry
   ```
3. Run the Executor, who is responsible for doing the hard work.
   ```shell
   javac src/executor/Executor.java && java -cp . src.executor.Executor
   ```
4. Run the Server, who receives the client data and forwards the calculation to the executor.
   ```shell
   javac src/server/Server.java && java -cp . src.server.Server
   ```
5. Run the Client, who offers the service options.
   ```shell
   javac src/client/Client.java && java -cp . src.client.Client
   ```

Once the computation is completed, the client application offers the option to save the output in a file.
