import java.io.IOException;
import java.util.Scanner;
import java.io.*;
import java.net.Socket;

class clientMultiThread extends Thread {
  String IPAddress = client.IPAddress;
  int port = client.port;
  static long totalTime = 0;
  String serverOut = "";
  int clientChoice = client.clientChoice;

  public void run() {
    long turnAroundTime;
    long start;
    long end;
    try {
      Socket socket = new Socket(IPAddress, port);
      PrintWriter myWriter = new PrintWriter(socket.getOutputStream(), true);
      InputStream input = socket.getInputStream();
      BufferedReader myReader = new BufferedReader(new InputStreamReader(input));
      myWriter.println(clientChoice);
      start = System.currentTimeMillis();

      StringBuilder serverOutput = new StringBuilder();

      while ((serverOut = myReader.readLine()) != null) {
        serverOutput.append(serverOut);
        serverOutput.append("\n");
      }
      end = System.currentTimeMillis();
      turnAroundTime = end - start;
      totalTime = totalTime + turnAroundTime;

      System.out.print(serverOutput);

      System.out.println("Client turn around Time: " + turnAroundTime + "ms");
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}

public class client {
  static String IPAddress = "";
  static int port;
  static int numClients;
  static int clientChoice = 0;

  public static void main(String[] args) throws IOException, InterruptedException {
    Scanner scan = new Scanner(System.in);
    System.out.println();
    System.out.print("What is the network address? (IP Address) : ");
    IPAddress = scan.nextLine();
    System.out.print("What is the desired port? : ");
    port = scan.nextInt();
    scan.nextLine();
    System.out.println();

    do {
      System.out.println(" _____________________________________________________ ");
      System.out.println("|                                                     |");
      System.out.println("|                     ~ Menu ~                        |");
      System.out.println("|_____________________________________________________|");
      System.out.println("|                                                     |");
      System.out.println("| Please choose one of the following options...       |");
      System.out.println("|                                                     |");
      System.out.println("| 1 - Date and Time                                   |");
      System.out.println("| 2 - Uptime                                          |");
      System.out.println("| 3 - Memory Use                                      |");
      System.out.println("| 4 - Netstat                                         |");
      System.out.println("| 5 - Current Users                                   |");
      System.out.println("| 6 - Running Processes                               |");
      System.out.println("| 7 - End Program                                     |");
      System.out.println("|_____________________________________________________|");
      clientChoice = scan.nextInt();
      scan.nextLine();
      
      if (clientChoice < 1 || clientChoice > 7) {
        System.out.println("Option not available. Please select a vaild option...\n");
        numClients = scan.nextInt();
        scan.nextLine();
        continue;
      }
      
      if (clientChoice == 7) {
	      clientMultiThread clientThreads = new clientMultiThread();
		    clientThreads.start();
		    clientThreads.join();
        System.out.println("Thank you! Have a great day.");
        System.out.println();
       	System.exit(0);
      }
      
      System.out.println();
      System.out.println("How many clients do you want to spawn?");
      System.out.println("Please select 1, 5, 10, 15, 20, 25, or 100.");
      numClients = scan.nextInt();
      scan.nextLine();
      System.out.println("Spawning " + numClients + " clients...");
      clientMultiThread[] clientThreads = new clientMultiThread[numClients];

      for (int i = 0; i < numClients; i++) {
        clientThreads[i] = new clientMultiThread();
      }
      for (int i = 0; i < numClients; i++) {
        clientThreads[i].start();
      }
      for (int i = 0; i < numClients; i++) {
        clientThreads[i].join();
      }
      
      long avgTime = clientMultiThread.totalTime / numClients;
      System.out.println();
      System.out.println("Total turn around time: " + clientMultiThread.totalTime + " ms");
      System.out.println("Average turn around time: " + avgTime + " ms");
      System.out.println();
      clientMultiThread.totalTime = 0;
    } while (clientChoice != 7);
  }
}
