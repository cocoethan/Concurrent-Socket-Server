import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.lang.management.*;

class serverMultiThread extends Thread {
	private Socket socket;

public serverMultiThread(Socket socket){
this.socket = socket;
}

  public void run() {
	String clientChoice = server.clientChoice;
    try {
      InputStream inClient = socket.getInputStream();
      OutputStream outServer = socket.getOutputStream();
      BufferedReader myReader = new BufferedReader((new InputStreamReader(inClient)));
      PrintWriter writer = new PrintWriter(outServer, true);

      if (clientChoice.equals("1")) {
          String date = new Date().toString();
          writer.println(date);
          System.out.println("Client Request: Date and Time\n" + date + "\n");
        } else if (clientChoice.equals("2")) {
          RuntimeMXBean rtBean = ManagementFactory.getRuntimeMXBean();
          long uptime = TimeUnit.MILLISECONDS.toSeconds(rtBean.getUptime());
          writer.println("Uptime: " + uptime + " s");
          System.out.println("Client Request: Uptime\n" + "Uptime: " + uptime + " s\n");
        } else if (clientChoice.equals("3")) {
          Runtime runtime = Runtime.getRuntime();
          long memoryInUse = runtime.totalMemory() - runtime.freeMemory();
          writer.println("Total Memory on Server: " + runtime.totalMemory() + " Bytes");
          writer.println("Memory in Use: " + memoryInUse + " Bytes");
          System.out.println("Client Request: Memory Use\n" + "Total Memory on Server: " + runtime.totalMemory() + " Bytes");
          System.out.println("Memory in Use: " + memoryInUse + " Bytes\n");
        } else if (clientChoice.equals("4")) {
          String cmd = "netstat --all";
          Process netStatProcess = Runtime.getRuntime().exec(cmd);
          BufferedReader netstatReader = new BufferedReader(new InputStreamReader(netStatProcess.getInputStream()));
          String line;
          while ((line = netstatReader.readLine()) != null) {
            writer.println(line);
            System.out.println("Client Request: Netstat\n" + line + "\n");
          }
        } else if (clientChoice.equals("5")) {
          String cmd = "who -H";
          Process listUsersProcess = Runtime.getRuntime().exec(cmd);
          BufferedReader listUsersReader = new BufferedReader(new InputStreamReader(listUsersProcess.getInputStream()));
          String listUsers;
          while ((listUsers = listUsersReader.readLine()) != null) {
            writer.println(listUsers);
            System.out.println("Client Request: Current Users\n" + listUsers + "\n");
          }
        } else if (clientChoice.equals("6")) {
          String cmd = "ps -ef";
          Process psProcess = Runtime.getRuntime().exec(cmd);
          BufferedReader psReader = new BufferedReader(new InputStreamReader(psProcess.getInputStream()));
          String ps;
          while ((ps = psReader.readLine()) != null) {
            writer.println(ps);
            System.out.println("Client Request: Running Processes\n" + ps + "\n");
          }
        } else if (clientChoice.equals("7")) {
          System.out.println();
          System.out.println("Client Request: Closing Servers and Ending Program");
          System.out.println();
          System.out.println("Servers closed");
          System.out.println();
          System.exit(0);
        }
        writer.close();
        myReader.close();
	      socket.close();

    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}

public class server {

  static String clientChoice;
  public static void main(String[] args) throws IOException, InterruptedException {
    
    Scanner scan = new Scanner(System.in);
	  String numClients;
    Socket socket;
    InputStream inClient;
    OutputStream outServer;
    BufferedReader myReader;
    PrintWriter writer;

    System.out.print("Enter the port number: ");
    int port = scan.nextInt();

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("Server is listening on port " + port + " ...\n");
      while (true) {
        socket = serverSocket.accept();
        inClient = socket.getInputStream();
        outServer = socket.getOutputStream();
        myReader = new BufferedReader((new InputStreamReader(inClient)));
        writer = new PrintWriter(outServer, true);
	
	clientChoice = myReader.readLine();

	new serverMultiThread(socket).start();

      }  
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}
