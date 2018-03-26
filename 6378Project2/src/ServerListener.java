import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread{
	private Server server;
	
	public ServerListener(Server s) {
		this.server = s;
		try(ServerSocket serverSock = new ServerSocket(server.getPort())){
			while(true) {
				Socket sock = serverSock.accept();
				
				InputStream in = sock.getInputStream();
				ObjectInputStream inStream = new ObjectInputStream(in);
				
				try {
					Message m = (Message) inStream.readObject();
					System.out.println(server.getID()+":"+m);
					
					// 3 opt: WRITE - READ - ENQUIRY
					server.deliverMessage(m);
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					inStream.close();
					in.close();
					sock.close();
				}
			}
		} catch(IOException e) {
			System.err.println(e);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
