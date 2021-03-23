package mainClient.controls;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import mainClient.ClientFirstTry;
import serverClient.Request;

/**
 * @author Muhammad Salah
 *
 */
public class EmailViewControls {
	/**
	 * folder name
	 */
	private String folder = null;
	/**
	 * the number of the email to view 
	 */
	private String emailNum = null;
	/**
	 * the main class of the client
	 */
	private ClientFirstTry c = null;
	/**
	 * the jason file that holds the data of the email
	 */
	private JSONObject email = null;

	/**
	 * author of the email
	 */
	@FXML
	private TextField from;
	/**
	 * subject of the email
	 */
	@FXML
	private TextField sub;
	/**
	 * the body of the email
	 */
	@FXML
	private TextArea emailBody;
	/**
	 * List view of the attachments
	 */
	@FXML
	private ListView<HBox> attach;
	/**
	 * the destination
	 */
	@FXML
	private TextField to;

	/**
	 * @param z the mail class of the client
	 * @param emailnum the number of the email
	 * @param foldername the name of the folder
	 */
	public EmailViewControls(ClientFirstTry z, String emailnum,
	String foldername) {
		folder = foldername;
		c = z;
		emailNum = emailnum;
	}

	/**
	 * initialize the controls of the email viewing scene
	 */
	@FXML
	private void initialize() {
		Request r = new Request(null, null);
		String tosend = r.laodEmailreq(c.getUsername(), folder, emailNum);
		try {
			/************************ connecting to server ********************/
			InetAddress i = InetAddress.getByName(c.getIP());
			Socket skt = new Socket();
			skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
			DataInputStream dis = new DataInputStream(skt.getInputStream());
			DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
			/******************** sending request ***************************/
			dos.writeUTF(tosend);
			String res = dis.readUTF();
			/******************** receive attachments ***********************/

			int size = Integer.valueOf(res);
			//attachments
			byte[] file = new byte[size];
			if (size > 0) {
				dis.readFully(file, 0, size);
				FileOutputStream fos = new FileOutputStream("email.json");
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				bos.write(file);
				bos.flush();
				bos.close();
				fos.close();
				JSONParser j = new JSONParser();
				File fi = new File("email.json");
				FileReader fr = new FileReader(fi);
				email = (JSONObject) j.parse(fr);
				fr.close();
				fi.delete();
				skt.close();
			} else {
				throw new Exception();
			}
		} catch (Exception e) {

		}
		setMainEmail();
		showAttach();
	}

	/**
	 * set the data of the email
	 */
	private void setMainEmail() {
		to.setText((String)email.get("to"));
		from.setText((String) email.get("auther"));
		sub.setText((String) email.get("sub"));
		emailBody.setText((String) email.get("body"));
	}


	/**
	 * show the attachmentts in the list view
	 */
	private void showAttach() {
		int attachnum = Integer.valueOf((String) email.get("attachments"));
		ObservableList<HBox> attachmentHboxes = FXCollections.observableArrayList();

		for (int k = 1; k <= attachnum; k++) {
			String name = (String) email.get(String.valueOf(k));
			JSONArray file = (JSONArray) email.get(name);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(
			ClientFirstTry.class.getResource("view/AttachView.fxml"));

			AttachDownloadControls con = new AttachDownloadControls(name, file);
			loader.setController(con);
			HBox h;
			try {
				h = (HBox) loader.load();
				attachmentHboxes.add(h);
			} catch (Exception e) {

			}
		}
		attach.setItems(attachmentHboxes);

	}
}
