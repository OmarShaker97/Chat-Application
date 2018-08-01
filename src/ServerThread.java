import java.io.*;
import java.util.*;
import java.net.*;

public class ServerThread extends Thread
{
	ServerSocket serverSocket;
	public ArrayList<ClientThread> onlineClients = new ArrayList<>();

	public ServerThread(ServerSocket serverSocket)
	{
		this.serverSocket = serverSocket;
	}

	public ArrayList<ClientThread> MemberListResponse()
	{
		return onlineClients;
	}

	@Override
	public void run() {

		Socket socket = null;

		while (true) 
		{
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			DataInputStream input = null;
			DataOutputStream output = null;
			try {
				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//System.out.println("Please enter a unique name in format Join(name)");
			//System.out.println(Server.onlineServers);

			/*Scanner scan = new Scanner(System.in);
			String username = scan.nextLine();

			while(!username.contains("Join"))
			{
				System.out.println("Please make sure that you use the correct format.");
				username = scan.nextLine();
			}

			username = username.substring(5, username.length()-1);

			JoinResponse(username, scan);
			 */
			ClientThread ct = new ClientThread(socket, input, output, this);

			Thread thread = new Thread(ct);

			onlineClients.add(ct);

			//System.out.println(username + " has just joined the server. " + socket);

			thread.start();

		}



	}

}