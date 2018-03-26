import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Server implements Runnable{
	private String masterIP;
	private int masterPort;
	private String serverID;
	private String serverIP;
	private int serverPort;

	public Server(String ID, String IP, int Port) {
		this.serverID = ID;
		this.serverIP = IP;
		this.serverPort = Port;
	}
	
	public void getFromMaster(String IP, int Port) {
		this.masterIP = IP;
		this.masterPort = Port;
	}
	
	public int getPort() {return serverPort;}
	public String getID() {return serverID;}
	
	@Override
	public void run() {
		System.out.println("Start!");
		// Run the Listener of this Server
		runListener();
		
		while(masterIP==null) {
			try {
				Thread.sleep(500);
				System.out.println("waiting for Master!");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Creat a Timer Thread sends heartbeats to Master every 5 seconds.
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				heartbeat();
			}
		}, 1000, 5000);
	}
	
	private void heartbeat() {
		try {
			Message m = new Message("Server", serverIP+":"+serverPort, masterIP+":"+masterPort, "heartbeat");
			Socket sock = new Socket(masterIP, masterPort);
			OutputStream out = sock.getOutputStream();
			ObjectOutputStream outStream = new ObjectOutputStream(out);
			outStream.writeObject(m);
			outStream.close();
			out.close();
			sock.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void deliverMessage(Message m) {
		String type = m.getID();
		String addr[] = m.getFromAddr().split(":");

		if(type.equals("Master")) {
			getFromMaster(addr[0], Integer.parseInt(addr[1]));
		}
	}
	
	private void runListener() {
		Thread sListener = new ServerListener(this);
		sListener.start();
	}
	
	
	
	public static void main(String[] args) {
		// args[0] = ID; args[1] = IP; args[2] = Port;
		Server s = new Server(args[0],args[1],Integer.parseInt(args[2]));
		Thread t = new Thread(s);
		t.start();
	}
	
}
