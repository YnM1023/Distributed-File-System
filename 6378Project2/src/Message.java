
public class Message {
	private String ID;
	private String fromAddr;
	private String targetAddr;
	private String content;
	
	public Message(String ID, String fromAddr, String targetAddr, String content) {
		this.ID = ID;
		this.fromAddr = fromAddr;
		this.targetAddr = targetAddr;
		this.content = content;
	}
	
	public String getID() {return ID;}
	public String getFromAddr() {return fromAddr;}
	public String getTargetAddr() {return targetAddr;}
	public String getContent() {return content;}
}
