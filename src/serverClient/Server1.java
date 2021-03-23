/**
 *
 */
package serverClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import dataStructure.DoubleLinkedList;
import mailServerInterfaces.IServer;

/**
 * @author SHIKO
 *
 */
public class Server1 implements IServer  {
	 private String port =  null;
	 public void setPort(String port) {
		this.port = port;
	}
     static DoubleLinkedList users = new DoubleLinkedList();
	 public void serverstart ()throws IOException {
	     // server is listening on port 5056
	     ServerSocket ss = new ServerSocket(Integer.valueOf(port));

	     // running infinite loop for getting
	     // client request
	     while (true)
	     {
	         Socket s = null;



	         try
	         {
	             // socket object to receive incoming client requests
	             s = ss.accept();

	             System.out.println("A new client is connected : " + s);

	             // obtaining input and out streams
	             DataInputStream dis = new DataInputStream(s.getInputStream());
	             DataOutputStream dos = new DataOutputStream(s.getOutputStream());

	             System.out.println("Assigning new thread for this client");

	             // create a new thread object
	             Thread t = new ClientHandler1(s, dis, dos, users);

	             // Invoking the start() method
	             t.start();

	         }

	         catch (Exception e){

	             s.close();
	             e.printStackTrace();
	             break;
	         }

	     }
	     ss.close();

	 }
}


