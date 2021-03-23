/**
 *
 */
package mainClient.controls;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import mainClient.ClientFirstTry;
import serverClient.Request;

/**
 * @author SHIKO
 *
 */
public class RemoveContactControls {
	/**
	 * controls of the layout
	 */
	private EmailLayoutControls c = null;
	/**
	 * main class of the client
	 */
	private ClientFirstTry z = null;
	/**
	 * contact name
	 */
	private String contact =null;
	/**
	 * @param c controls of the layout
	 * @param z main class of the client
	 * @param contact contact name
	 */
	public RemoveContactControls(EmailLayoutControls c,ClientFirstTry z, String contact ) {
		this.c = c;
		this.z = z;
		this.contact = contact;
	}
	/**
	 * exit button that removes contact
	 */
	@FXML
	private Button removeContact;

	/**
	 * initialize the button that removes the contact
	 */
	@FXML
	private void initialize() {
		removeContact.setOnAction((event)->{
			removecon();
		});
	}
	/**
	 * function that removes the contact specified
	 */
	private void removecon() {
		try {
			InetAddress i = InetAddress.getByName(z.getIP());
			Socket skt = new Socket();
			skt.connect(new InetSocketAddress(i, z.getPort()), 1000);
			DataInputStream dis = new DataInputStream(skt.getInputStream());
			DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
			Request r = new Request(null, null);
			String req = r.removeContactReq(z.getUsername(),contact);
			dos.writeUTF(req);
			dis.readUTF();
			skt.close();
			c.updateContacts();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
