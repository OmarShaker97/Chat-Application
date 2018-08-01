import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {

	public static ArrayList<ServerThread> onlineServers = new ArrayList<ServerThread>();

	public static void main(String[] args) throws IOException {
		ServerThread server1 = new ServerThread(new ServerSocket(6000));
		ServerThread server2 = new ServerThread(new ServerSocket(6001));

		onlineServers.add(server1);
		onlineServers.add(server2);

		server1.start();
		server2.start();

		//System.out.println(onlineServers.size());
	}

}