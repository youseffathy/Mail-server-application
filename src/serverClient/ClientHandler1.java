/**
 *
 */
package serverClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import dataStructure.DoubleLinkedList;
import mailServerInterfaces.IClientHandler;

/**
 * @author SHIKO
 *
 */
public class ClientHandler1 extends Thread implements IClientHandler {
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;
	String name;
	boolean isLoggedin;
	DoubleLinkedList users = null;

	// Constructor
	public ClientHandler1(Socket s, DataInputStream dis, DataOutputStream dos, DoubleLinkedList users) {
		this.s = s;
		this.dis = dis;
		this.dos = dos;
		this.users = users;
		isLoggedin = true;
	}

	@Override
	public void run() {
		Request request = new Request(this.dis, this.dos);
		String received = null;
		try {
			received = dis.readUTF();

			if(!received.equals("exit")) {
			    request.setUsers(users);
			    request.setskt(s);
			    request.setName(name);
			    request.recieveReq(received);
			    dos.writeUTF(request.response());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			// closing resources
			this.dis.close();
			this.dos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}