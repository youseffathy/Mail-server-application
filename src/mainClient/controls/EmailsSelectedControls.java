package mainClient.controls;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import mainClient.ClientFirstTry;
import serverClient.Request;

/**
 * @author Muhammad Salah
 *
 */
public class EmailsSelectedControls {

	/**
	 * report as spam button
	 */
	@FXML
	private Button report;
	/**
	 * delete emails button
	 */
	@FXML
	private Button delete;
	/**
	 * move selected emails to...
	 */
	@FXML
	private MenuButton move;
	/**
	 * client class
	 */
	ClientFirstTry c = null;
	/**
	 * email layout controls
	 */
	EmailLayoutControls e = null;

	/**
	 * @param z client class
	 * @param d email layout controls
	 */
	public EmailsSelectedControls(ClientFirstTry z, EmailLayoutControls d) {
		c = z;
		e = d;
	}

	/**
	 * initailize the actions that can be done on the selected emails
	 */
	@FXML
	private void initialize() {
		delete.setOnAction((event) -> {
			DeleteSelected();
		});

		delete.setOnDragOver((event)->{

	        if (event.getGestureSource() != delete &&
	                event.getDragboard().hasString()) {

	            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
	        }

	        event.consume();

		});

		delete.setOnDragEntered((event)->{
		    delete.setStyle("-fx-border-color: #1a474a;");
		});
		delete.setOnDragExited((event)->{
		    delete.setStyle("");
		});
		delete.setOnDragDropped((event)->{
		    Dragboard db = event.getDragboard();
	        boolean success = false;
	        if (db.hasString()) {
				deletEmail(db.getString());
	           success = true;
	        }
	        event.setDropCompleted(success);
	        event.consume();
		});

		moveSelectedEmails();
		report.setOnAction((event) -> {
			reportSelected();
		});

	}

	/**
	 * @param emailNum number of the email to delete
	 * delete an email
	 */
	private void deletEmail(String emailNum) {
		try {
			Request r = new Request(null, null);
			InetAddress i = InetAddress.getByName(c.getIP());
			Socket skt = new Socket();
			skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
			DataInputStream dis = new DataInputStream(skt.getInputStream());
			DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
			String req =
			r.DeleteEmailReq(c.getUsername(), e.getfname(), emailNum);
			dos.writeUTF(req);
			String res = dis.readUTF();
			boolean b = r.deleteFolderRes(res);
			skt.close();
		} catch (Exception er) {
			er.printStackTrace();
			Alert al = new Alert(AlertType.ERROR);
			al.setContentText("Couldn't Delete Email");
			al.showAndWait();
		}
	}

	/**
	 * loop and delete the selected emails
	 */
	private void DeleteSelected() {
		ListView<HBox> mails =
		(ListView) e.getEmailScene().getChildren().get(0);
		ObservableList<HBox> mailslist = mails.getItems();
		for (int i = 0; i < mailslist.size(); i++) {
			HBox h = mailslist.get(i);
			ObservableList<Node> nodes = h.getChildren();
			Node n = nodes.get(1);
			if (((CheckBox) n).isSelected()) {
				String en = ((Label) nodes.get(6)).getText();
			/*	int nn = Integer.valueOf(en);

				if (nn > lastemailnumber) {
					nn = nn - 1;
				}
				lastemailnumber = nn;

				en = String.valueOf(nn);
*/
				deletEmail(en);

			}
		}
		e.emailsloader(e.getfname());
		e.foldersLoader();
		e.isSelection();
		Alert al = new Alert(AlertType.CONFIRMATION);
		al.setContentText("Deleted");
		al.showAndWait();
	}

	/**
	 * move the selected email to the destination
	 */
	private void moveSelectedEmails() {
		ObservableList<HBox> items = e.getFolderList().getItems();
		for (int i = 0; i < items.size(); i++) {
			MenuItem m = new MenuItem();
			m.setText(((Label) items.get(i).getChildren().get(0)).getText());
			m.setOnAction((event) -> {
				setMenuItemAction(m.getText());

			});
			move.getItems().add(m);
		}
	}

	/**
	 * @param fname folder name
	 * move email to the selected menu item text (folder)
	 */
	private void setMenuItemAction(String fname) {
		ListView<HBox> mails =
		(ListView) e.getEmailScene().getChildren().get(0);
		ObservableList<HBox> mailslist = mails.getItems();
		for (int i = 0; i < mailslist.size(); i++) {
			HBox h = mailslist.get(i);
			ObservableList<Node> nodes = h.getChildren();
			Node n = nodes.get(1);

			if (((CheckBox) n).isSelected()) {

				String en = ((Label) nodes.get(6)).getText();
			/*	int nn = Integer.valueOf(en);

				if (nn > lastemailnumber) {
					nn = nn - 1;
				}
				lastemailnumber = nn;

				en = String.valueOf(nn);
*/
				moveEmail(en, fname);

			}
		}
		e.emailsloader(e.getfname());
		e.foldersLoader();
		e.isSelection();
		Alert al = new Alert(AlertType.CONFIRMATION);
		al.setContentText("Moved successfully");
		al.showAndWait();
	}

	 /**
	 * @param emailNum email number
	 * @param to destination
	 * perform move email request
	 */
	public void moveEmail(String emailNum, String to) {
		try {
			Request r = new Request(null, null);

			InetAddress i = InetAddress.getByName(c.getIP());
			Socket skt = new Socket();
			skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
			DataInputStream dis = new DataInputStream(skt.getInputStream());
			DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
			String req =
			r.moveEmailReq(c.getUsername(), e.getfname(), to, emailNum);
			dos.writeUTF(req);
			String res = dis.readUTF();
			boolean b = r.deleteFolderRes(res);
			skt.close();
		} catch (Exception er) {
			er.printStackTrace();
			Alert al = new Alert(AlertType.ERROR);
			al.setContentText("Couldn't move Email");
			al.showAndWait();
		}
	}

	/**
	 * @param emailNum email number
	 * perfom report as spam request
	 */
	private void reportEmail(String emailNum) {
		try {
			Request r = new Request(null, null);

			InetAddress i = InetAddress.getByName(c.getIP());
			Socket skt = new Socket();
			skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
			DataInputStream dis = new DataInputStream(skt.getInputStream());
			DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
			String req =
			r.reportSpamReq(c.getUsername(), e.getfname(), emailNum);
			dos.writeUTF(req);
			String res = dis.readUTF();
			boolean b = r.deleteFolderRes(res);
			skt.close();
		} catch (Exception er) {
			er.printStackTrace();
			Alert al = new Alert(AlertType.ERROR);
			al.setContentText("Couldn't report Email As Spam");
			al.showAndWait();
		}
	}

	/**
	 * report the selected emails as spam
	 */
	private void reportSelected() {
		ListView<HBox> mails =
		(ListView) e.getEmailScene().getChildren().get(0);
		ObservableList<HBox> mailslist = mails.getItems();
		for (int i = 0; i < mailslist.size(); i++) {
			HBox h = mailslist.get(i);
			ObservableList<Node> nodes = h.getChildren();
			Node n = nodes.get(1);
			if (((CheckBox) n).isSelected()) {
				String en = ((Label) nodes.get(6)).getText();
				reportEmail(en);
			}
		}
		e.emailsloader(e.getfname());
		e.foldersLoader();
		e.isSelection();
		Alert al = new Alert(AlertType.CONFIRMATION);
		al.setContentText("Done!");
		al.showAndWait();
	}
}
