import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class Master implements Runnable{
	private HashMap<String,String> servers;
	private String masterIP;
	private int masterPort; 
	private ArrayList<String> serverIDs;
	
	public Master(String IP_Port, String file) {
		String[] ip = IP_Port.split(":");
		this.masterIP = ip[0];
		this.masterPort = Integer.parseInt(ip[1]);
		this.servers = new HashMap<>();
		this.serverIDs = new ArrayList<>();

		try {
			BufferedReader r = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = r.readLine())!=null) {
				String[] parts = line.split(" ");
				this.serverIDs.add(parts[0]);
				this.servers.put(parts[0],parts[1]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		connectServers();
	}
	
	private void connectServers() {
		for(String ServerID:serverIDs) {
			String addr = servers.get(ServerID);
			Message m = new Message("Master", masterIP+":"+masterPort, addr, "hello, I am Master!");
			sendMessage(addr,m);
		}
	}
	
	private void sendMessage(String addr, Message m) {
		System.out.println(" [Send] Sends message to "+addr+" a init Message");
		String[] TAddr = addr.split(":");
		try {
			Socket sock = new Socket(TAddr[0], Integer.parseInt(TAddr[1]));
			OutputStream out = sock.getOutputStream();
			ObjectOutputStream outStream = new ObjectOutputStream(out);
			outStream.writeObject(m);
			outStream.close();
			out.close();
			sock.close();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		runListener();
		
	}
	
	private void runListener() {
		// args[0] = Master's IP:PORT, args[1] = servers.txt
		Thread mListener = new MasterListener(this);
		mListener.start();
	}
	
	public static void main(String[] args) {
		Master m = new Master(args[0], args[1]);
		Thread t = new Thread(m);
		t.start();
	}
}
