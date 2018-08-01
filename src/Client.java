import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

public class Client {

	static JTextArea clientReceived;
	static JTextField clientSend;
	static int port;
	static JPanel top;
	static JPanel bottom;
	static JPanel chooseServer;
	static JButton sendButton;
	static JScrollPane jsp ;

	public static void main(String args[]) throws UnknownHostException, IOException {
		JFrame clientWindow = new JFrame();
		//clientWindow.setBackground(Color.black);
		clientWindow.setBounds(340, 120, 550, 500);

		Font font1 = new Font("Century Gothic", Font.PLAIN, 16);

		clientWindow.setResizable(false);
		clientReceived = new JTextArea();
		clientReceived.setFont(font1);
		clientReceived.setEditable(false);
		//clientReceived.setPreferredSize(new Dimension(520,400));
		clientReceived.setVisible(true);
		clientSend = new JTextField();
		clientSend.setPreferredSize(new Dimension(440,50));
		clientSend.setVisible(true);
		clientSend.setFont(font1);
		ImageIcon sendImage = new ImageIcon("Images/button3.png");
		sendButton = new JButton(sendImage);
		sendButton.setOpaque(false);
		sendButton.setContentAreaFilled(false);
		sendButton.setBorderPainted(false);
		sendButton.setVisible(true);
		sendButton.setVisible(true);
		sendButton.setPreferredSize(new Dimension(90,50));
		sendButton.setText("");
		sendButton.setBackground(Color.GREEN);

		top = new JPanel();
		top.setVisible(false);
		top.setSize(250, 250);
		bottom = new JPanel();
		bottom.setVisible(false);
		bottom.setSize(250,250);

		DefaultCaret caret = (DefaultCaret) clientReceived.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		clientReceived.setCaret(caret);
		jsp = new JScrollPane(clientReceived);
		jsp.setAutoscrolls(true);
		jsp.setPreferredSize(new Dimension(540,400));
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		top.add(jsp, BorderLayout.CENTER);
		bottom.add(clientSend, BorderLayout.WEST);
		bottom.add(sendButton, BorderLayout.EAST);

		clientWindow.add(top,BorderLayout.NORTH);
		clientWindow.add(bottom, BorderLayout.SOUTH);

		clientWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientWindow.setVisible(true);
		clientWindow.setTitle("Shaker Messaging App");
		//clientWindow.setSize(new Dimension(520,500));

		chooseServer = new JPanel();
		chooseServer.setVisible(true);
		ImageIcon server1Image = new ImageIcon("Images/button1.png");
		JButton server1 = new JButton(server1Image);
		server1.setOpaque(false);
		server1.setContentAreaFilled(false);
		server1.setBorderPainted(false);
		server1.setVisible(true);
		ImageIcon server2Image = new ImageIcon("Images/button2.png");
		JButton server2 = new JButton(server2Image);
		server2.setOpaque(false);
		server2.setContentAreaFilled(false);
		server2.setBorderPainted(false);
		server2.setVisible(true);
		chooseServer.add(server1, BorderLayout.WEST);
		chooseServer.add(server2, BorderLayout.EAST);
		//chooseServer.setBackground(Color.darkGray);
		server1.setPreferredSize(new Dimension(500,250));
		server1.setText(" ");

		server2.setPreferredSize(new Dimension(500,100));
		clientWindow.add(chooseServer);
		server2.setText(" ");
		server2.setBackground(Color.WHITE);

		server1.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				port = 6000;

				InetAddress ip;
				try {
					ip = InetAddress.getByName("localhost");
					Socket socket = null;

					socket = new Socket(ip, port);

					//serverToConnect++;
					//System.out.println(serverToConnect);
					DataInputStream input = new DataInputStream(socket.getInputStream());
					DataOutputStream output = new DataOutputStream(socket.getOutputStream());

					sendMsg(output, socket);
					listenForMsg(input);

					chooseServer.setVisible(false);
					top.setVisible(true);
					bottom.setVisible(true);
					jsp.setPreferredSize(jsp.getPreferredSize());

				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


			}

		});

		server2.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				port = 6001;

				InetAddress ip;
				try {
					ip = InetAddress.getByName("localhost");
					Socket socket = null;

					socket = new Socket(ip, port);

					//serverToConnect++;
					//System.out.println(serverToConnect);
					DataInputStream input = new DataInputStream(socket.getInputStream());
					DataOutputStream output = new DataOutputStream(socket.getOutputStream());

					sendMsg(output, socket);
					listenForMsg(input);

					chooseServer.setVisible(false);
					top.setVisible(true);
					bottom.setVisible(true);
					jsp.setPreferredSize(jsp.getPreferredSize());
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


			}

		});



	}

	public static void sendMsg(DataOutputStream output, Socket socket) {
		Thread sendMsg = new Thread(new Runnable() {
			@Override
			public void run() {
				sendButton.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e) {
						//while (true) {
						//String message = scan.nextLine();
						// System.out.println(Server.onlineServers+"LoL");
						String message = clientSend.getText();
						if (message.equalsIgnoreCase("quit") || message.equalsIgnoreCase("bye")) {
							// ServerThread.onlineClients.remove(this);
							Client.clientReceived.append("Server closed the connection." + "\n");
							//System.out.println("Server closed the connection.");
							try {
								socket.close();
								System.exit(0);
								//break;
							} catch (IOException e1) {
								e1.printStackTrace();
							}

						}

						if(message.equalsIgnoreCase("Clear()"))
						{
							Client.clientReceived.setText("");
						}

						try {
							//Client.clientReceived.append(message + "\n");
							output.writeUTF(message);
						} catch (IOException e1) {
							e1.printStackTrace();
						}

						clientSend.setText("");
						//	}

					}



				});

				clientSend.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e) {
						//while (true) {
						//String message = scan.nextLine();
						// System.out.println(Server.onlineServers+"LoL");
						String message = clientSend.getText();
						if (message.equalsIgnoreCase("quit") || message.equalsIgnoreCase("bye")) {
							// ServerThread.onlineClients.remove(this);
							Client.clientReceived.append("Server closed the connection." + "\n");
							//System.out.println("Server closed the connection.");
							try {
								socket.close();
								System.exit(0);
								//break;
							} catch (IOException e1) {
								e1.printStackTrace();
							}

						}

						if(message.equalsIgnoreCase("Clear()"))
						{
							Client.clientReceived.setText("");
						}

						try {
							//Client.clientReceived.append(message + "\n");
							output.writeUTF(message);
						} catch (IOException e1) {
							e1.printStackTrace();
						}

						clientSend.setText("");
						//	}

					}



				});

			}
		});

		sendMsg.start();

	}

	public static void listenForMsg(DataInputStream refactor) {
		Thread readMsg = new Thread(new Runnable() {
			@Override
			public void run() {

				while (true) {
					try {
						String message = refactor.readUTF();
						clientReceived.append(message + "\n");
						//System.out.println(message);
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}
		});

		readMsg.start();

	}	
}