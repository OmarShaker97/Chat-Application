import java.io.*;
import java.net.*;

class ClientThread implements Runnable 
{

	private String name;
	private DataInputStream input;
	private DataOutputStream output;
	private Socket socket;
	private boolean isLoggedIn = false;

	public ClientThread(Socket socket,
			DataInputStream input, DataOutputStream output, ServerThread serverThread) {
		this.input = input;
		this.output = output;
		this.socket = socket;
	}

	@Override
	public void run() {

		String received;
		while (true) 
		{
			try
			{
				if(input!=null){
					received = input.readUTF();

					//System.out.println(received);

					if(isLoggedIn==false){

						if(received.contains("Join("))
						{
							received = received.substring(5, received.length()-1);
							JoinResponse(received);
							this.name = received;
							isLoggedIn = true;
							output.writeUTF("Welcome to Shaker's Messaging App, " + received + "! " + "\n" + "Please type help if you require assistance with the chat commands.");
							output.flush();
						}

						else
						{
							output.writeUTF("Please enter a unique name in format Join(name)");
						}
					}



					else if(received.equalsIgnoreCase("bye")|| received.equalsIgnoreCase("quit")){
						for(int i = 0; i<Server.onlineServers.size();i++){
							for(ClientThread myClient : Server.onlineServers.get(i).onlineClients){
								if(myClient.name.equals(this.name)){
									this.isLoggedIn=false;
									Server.onlineServers.get(i).onlineClients.remove(this);
								}
								//System.out.println(Server.onlineServers.get(i).onlineClients);
							}
						}
						input.close();
						output.close();
						break;

					}

					else if(received.equalsIgnoreCase("GetMemberList()"))
					{
						GetMemberList();
					}

					else if (received.contains("Chat(")){

						received = received.substring(5,received.length()-1);			

						String[] myArray = received.split(",");

						String MsgToSend = myArray[3];
						String recipient = myArray[1];
						int TTL = Integer.parseInt(myArray[2]);

						boolean isRecipientExist = isExist(recipient);
						boolean isSourceExist = isExist(this.name);
						if(isRecipientExist && isSourceExist){
							Chat(this.name, recipient, TTL,MsgToSend);
						}
						else
						{
							output.writeUTF("Please enter a valid source name & recipient name");
						}
					}

					else if(received.equalsIgnoreCase("help"))
					{
						for(int i = 0; i<Server.onlineServers.size();i++){
							for(ClientThread myClient : Server.onlineServers.get(i).onlineClients)
							{
								if(myClient.name.equals(this.name))
								{        
									myClient.output.writeUTF("Chat(Source,Destination,TTL,Message): Source is your name, Destination \nis the recipient,"
											+ " TTL is time to live, Message is the message you would like \nto send.\nGetMemberList(): "
											+ "returns you a list of active users\nbye or quit: Quit the session.\n"
											+ "Clear(): Clear the chat log.");
									myClient.output.flush();
								}
							}
						}
					}

					else if(received.equalsIgnoreCase("Clear()"))
					{
						this.output.writeUTF("Chat Cleared!");
					}

					else
					{
						for(int i = 0; i<Server.onlineServers.size();i++){
							for(ClientThread myClient : Server.onlineServers.get(i).onlineClients)
							{
								if(this.name == null)
								{
									myClient.output.writeUTF("Please enter a unique name in format Join(name)");
								}
								else if (myClient.name.equals(this.name))
								{
									myClient.output.writeUTF("Invalid Command. Please type help if you require assistance with the chat commands.");
									myClient.output.flush();
								}
							}
						}
					}
				}
			} catch (IOException e) {
				// e.printStackTrace();
			}

		}
		try
		{
			this.input.close();
			this.output.close();

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void GetMemberList()
	{
		for(int i = 0; i<Server.onlineServers.size();i++){
			try {
				int serverNo = i + 1;
				output.writeUTF("Server " + serverNo + ":");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for(ClientThread myClient : Server.onlineServers.get(i).MemberListResponse())
			{

				try {
					if(myClient.isLoggedIn){
						output.writeUTF(myClient.name);
						output.flush();}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public boolean isExist(String username)
	{
		boolean isExist = false;
		for(int i = 0; i<Server.onlineServers.size();i++){
			for(ClientThread myClient : Server.onlineServers.get(i).MemberListResponse())
			{
				if(myClient.name.equals(username))
					isExist=true;
			}
		}

		return isExist;
	}

	public void Chat(String source, String destination, int TTL, String message)
	{

		for(int i = 0; i<Server.onlineServers.size();i++){
			for (ClientThread myClient : Server.onlineServers.get(i).onlineClients) 
			{
				if (myClient.name.equals(destination) && TTL>0) 
				{
					try {
						myClient.output.writeUTF(this.name+": "+message);
						myClient.output.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					break;
				}
				TTL--;
			}
		}	

	}

	public String getName() {
		return name;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public DataInputStream getInput() {
		return input;
	}

	public DataOutputStream getOutput() {
		return output;
	}

	public void setName(String name) {
		this.name = name;
	}

	private void JoinResponse(String username) throws IOException
	{
		while(checkIfExists(username))
		{
			this.output.writeUTF("The username " + username + " already exists. Please try again with another username.");
			username = this.input.readUTF();
		}

	}

	private boolean checkIfExists(String username)
	{
		boolean bool = false;

		for(int i = 0; i<Server.onlineServers.size();i++){
			for (ClientThread myClient : Server.onlineServers.get(i).onlineClients) 
			{
				if(myClient.getName()==null)
				{}

				else if(myClient.getName().equals(username))
					bool = true;
			}
		}

		return bool;
	}



}