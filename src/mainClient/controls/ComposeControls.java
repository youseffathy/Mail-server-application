/**
 *
 */
package mainClient.controls;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import dataStructure.DoubleLinkedList;
import dataStructure.linkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mainClient.ClientFirstTry;
import serverClient.Request;

/**
 * @author SHIKO
 *
 */
public class ComposeControls {
	/**
	 * DoubleLinkedList contains the names of the users that an email is sent to
	 */
	private DoubleLinkedList tos = new DoubleLinkedList();
	/**
	 * DoubleLinkedList contains the attachments
	 */
	private linkedList attachfiles = new linkedList();
	/**
	 * contains the hboxs of the views of the attachments in compose
	 */
	private ObservableList<HBox> attachItems =
	FXCollections.observableArrayList();
	/**
	 * the main class of the client
	 */
	private ClientFirstTry c = null;
	@FXML
	Button Exit;
	@FXML
	MenuButton contacts;
	@FXML
	TextField subject;
	@FXML
	TextArea emailBody;
	@FXML
	Button send;
	@FXML
	Button attach;
	@FXML
	ListView<HBox> attachments;
	/**
	 *
	 */
	EmailLayoutControls elc;
	/**
	 * String the name of the receiver of the email
	 */
	private String toName = null;

	/**
	 *
	 * @param z
	 * @param kk
	 * @param elc
	 */
	public ComposeControls(ClientFirstTry z, String kk,
	EmailLayoutControls elc) {
		c = z;
		toName = kk;
		this.elc = elc;
	}

	@FXML
	private void initialize() {

		if (!toName.equals("")) {
			tos.add(toName);
		}
		contacts.setOnMouseClicked((event) -> {
			sendToContacts();
		});
		attachments.setItems(attachItems);
		exitButton();
		attachBtn();
		sendBtn();
	}

	/**
	 * exit button control
	 */
	private void exitButton() {
		Exit.setOnAction((event) -> {

			Stage stage = (Stage) Exit.getScene().getWindow();
			stage.close();
		});

	}

	/**
	 * add attachments button control
	 */
	private void attachBtn() {
		attach.setOnAction((event) -> {
			Stage stage = (Stage) attach.getScene().getWindow();
			FileChooser f = new FileChooser();
			List<File> files = f.showOpenMultipleDialog(stage);
			try {
				if (!files.equals(null)) {
					addAttachments(files);
				}
			} catch (Exception e) {

			}
		});

	}

	/**
	 *
	 * @param list
	 *            of files from file chooser
	 */
	private void addAttachments(List<File> l) {

		for (int i = 0; i < l.size(); i++) {
			try {
				File file = l.get(i);
				if (attachfiles.contains(file))
					continue;
				attachfiles.add(0, file);
				FXMLLoader f = new FXMLLoader();
				f.setLocation(
				ClientFirstTry.class.getResource("view/AttachmentHbox.fxml"));
				f.setController(
				new AttachControls(file.getName(), attachfiles, attachItems));
				HBox h = (HBox) f.load();
				attachItems.add(h);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * controls of sending email button
	 */
	private void sendBtn() {
		send.setOnAction((event) -> {
			for (int count = 0; count < tos.size(); count++) {

				String des = (String) tos.get(count);
				String sub = subject.getText();
				String body = emailBody.getText();
				int n = attachfiles.size();
				Request r = new Request(null, null);
				String toSend =
				r.sendEmailReq(des, sub, body, c.getUsername(),
				String.valueOf(n));
				try {
					/************************
					 * connecting to server
					 ************************************/
					InetAddress i = InetAddress.getByName(c.getIP());
					Socket skt = new Socket();
					skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
					DataInputStream dis =
					new DataInputStream(skt.getInputStream());
					DataOutputStream dos =
					new DataOutputStream(skt.getOutputStream());
					/************************
					 * sending first part of the email
					 ************************/
					dos.writeUTF(toSend);
					String res = dis.readUTF();
					if (res.equals("okay")) {
						for (int j = 0; j < n; j++) {
							File f = (File) attachfiles.get(j);
							dos.writeUTF(f.getName());
							dos.writeUTF(String.valueOf(f.length()));
							byte[] mybytes = new byte[(int) f.length()];
							FileInputStream fis = new FileInputStream(f);
							BufferedInputStream bis =
							new BufferedInputStream(fis);
							bis.read(mybytes, 0, mybytes.length);
							dos.write(mybytes);
						}
					}
					res = dis.readUTF();
					skt.close();
					dis.close();
					dos.close();
					JSONParser j = new JSONParser();
					JSONObject o = new JSONObject();
					o = (JSONObject) j.parse(res);
					boolean result = (boolean) o.get("res");
					if (!result) {
						throw new Exception();
					} else {
						Alert a = new Alert(AlertType.CONFIRMATION);
						a.setContentText("Email Sent Successfully");
						a.showAndWait();
						Stage stage = (Stage) send.getScene().getWindow();
						stage.close();
					}
				} catch (Exception e) {
					Alert a = new Alert(AlertType.ERROR);
					a.setContentText("Email Not Sent");
					a.showAndWait();
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * send to multiple contacts
	 */
	public void sendToContacts() {
		updateContacts();
	}
	/**
	 *	update contacts in the menubar
	 */
	public void updateContacts() {
		JSONObject cont = getContacts();
		int num = Integer.valueOf((String) cont.get("contacts"));
		ObservableList<MenuItem> con = contacts.getItems();
		con.removeAll(con);
		MenuItem newC = new MenuItem();
		newC.setText("New..");
		newC.setOnAction((event) -> {

			newContact();

		});
		con.add(newC);

		for (int i = 1; i <= num; i++) {
			CheckMenuItem m = new CheckMenuItem();
			m.setText((String) cont.get(String.valueOf(i)));

			if (tos.contains(m.getText())) {
				m.setSelected(true);
			}

			m.setOnAction((event) -> {
				if (m.isSelected()) {
					tos.add(m.getText());

				} else if (!m.isSelected()) {
					for (int l = 0; l < tos.size(); l++) {
						if (tos.get(l).equals(m.getText())) {
							tos.remove(l);
							break;
						}
					}
				}
			});
			con.add(m);
		}
	}

	/**
	 * add new contacts
	 */
	private void newContact() {
		TextInputDialog d = new TextInputDialog();
		d.setTitle("Add Contact");
		d.setHeaderText("Add Contact");
		d.setContentText("Enter Contact Name:");

		Optional<String> results = d.showAndWait();
		if (!results.isPresent()) {
			return;
		}

		try {
			String newContact = results.get();
			InetAddress i = InetAddress.getByName(c.getIP());
			Socket skt = new Socket();
			skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
			DataInputStream dis = new DataInputStream(skt.getInputStream());
			DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
			Request r = new Request(null, null);
			String req = r.newContactReq(c.getUsername(), newContact);
			dos.writeUTF(req);
			dis.readUTF();
			skt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 *
	 * @return JSONObject of names of user contacts
	 */
	public JSONObject getContacts() {
		try {
			InetAddress i = InetAddress.getByName(c.getIP());
			Socket skt = new Socket();
			skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
			DataInputStream dis = new DataInputStream(skt.getInputStream());
			DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
			Request r = new Request(null, null);
			String req = r.getContactsReq(c.getUsername());
			dos.writeUTF(req);
			String res = dis.readUTF();
			skt.close();
			return r.getContactsRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
