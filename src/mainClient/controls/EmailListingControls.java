/**
 *
 */
package mainClient.controls;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.shape.Rectangle;
import mainClient.ClientFirstTry;
import serverClient.Request;

/**
 * @author SHIKO
 *
 */
public class EmailListingControls {
	/**
	 * String subject of the email
	 */
	String subject = null;
	/**
	 * source of the email
	 */
	String From = null;
	/**
	 * date of the email
	 */
	String Date = null;
	/**
	 * status of the email as read or unread
	 */
	String reademail = null;
	/**
	 * priority of the email important or not important or very important
	 */
	String Priority = null;
	/**
	 * status of the email starred o unstarred
	 */
	String starred = null;
	/**
	 * number of the email
	 */
	String email = null;
	/**
	 * String username of the email owner
	 */
	String username =  null;
	/**
	 *  String foldername containing the email
	 */
	String foldername = null;
	/**
	 * client main class
	 */
	ClientFirstTry c = null;

	@FXML
	private Label emailNumber;
	@FXML
	private Label sub;
	@FXML
	private Label read;
	@FXML
	private Label from;
	@FXML
	private MenuButton priority;
	@FXML
	private Button star;
	@FXML
	private Label date;
	@FXML
	private CheckBox selectEmail;
	@FXML
	private Rectangle drag;
	/**
	 * class emaillayoutcontrols
	 */
	private EmailLayoutControls ec = null;
	/**
	 *
	 * @param z client main class
	 * @param userName
	 * @param folderName
	 * @param emailnum
	 * @param sub
	 * @param from
	 * @param date
	 * @param read
	 * @param priority
	 * @param star
	 * @param y EmailLayoutControls class
	 */
	public EmailListingControls(ClientFirstTry z,String userName , String folderName,int emailnum ,String sub, String from, String date,
	String read, String  priority, String star, EmailLayoutControls y) {
		username = userName;
		foldername = folderName;
		email = String.valueOf(emailnum);
		subject = sub;
		From = from;
		reademail = read;
		Priority = priority;
		Date = date;
		starred = star;
		c = z;
		ec = y;
	}

	@FXML
	private void initialize() {


	    emailNumber.setText(email);
	    selectEmail.setOnAction((event) -> {
	        ec.isSelection();
	    });

		sub.setText(subject);

		if (reademail.equals("true")) {


			read.setText("read");
		} else {
			read.setText("unread");
		}
		from.setText(From);
		date.setText(Date);
		/**
		 * priority
		 */
		if(Priority.equals("1")) {
			priority.setText("Very Important");
		} else if (Priority.equals("2")) {
			priority.setText("Important");
		} else {
			priority.setText("Not Important");
		}

		ObservableList<MenuItem> list =  FXCollections.observableArrayList();
		list = priority.getItems();
		MenuItem m1 = list.get(0);
		m1.setOnAction((event)->{
			priority.setText("Very Important");
			setPriority(1);
		});
		MenuItem m2 = list.get(1);
		m2.setOnAction((event)->{
			priority.setText("Important");
			setPriority(2);
		});
		MenuItem m3 = list.get(2);
		m3.setOnAction((event)->{
			priority.setText("Not Important");
			setPriority(3);
		});

		/**
		 * star emails
		 */
		if (starred.equals("true")) {
			star.setText("starred");
			star.setStyle("-fx-background-color: gold ;");
		} else {
			star.setText("unstarred");
			star.setStyle("-fx-background-color: gray ;");
		}
		star.setOnAction((event)->{
			if(starred.equals("false")) {
				starUnstar();
				starred = "true";
				star.setText("starred");
				star.setStyle("-fx-background-color: gold ;");
			} else {
				starUnstar();
				starred = "false";
				star.setText("unstarred");
				star.setStyle("-fx-background-color: gray ;");
			}

		});
	}
	/**
	 * linking the client side with the server to set email priority
	 * @param n
	 */
	private void setPriority(int n) {
		try {
			Request r = new Request(null, null);
			InetAddress i = InetAddress.getByName(c.getIP());
	        Socket skt = new Socket();
	        skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
			String s = r.setPriorityReq(String.valueOf(n),username, foldername, email);
			DataInputStream dis = new DataInputStream(skt.getInputStream());
            DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
            dos.writeUTF(s);
            String res = dis.readUTF();
            skt.close();
            boolean b = r.setPriorityRes(res);
            if(!b) {
            	throw new Exception();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 *linking the client side with the server to set star or unstar an email
	 */
	private void starUnstar() {
		try {
			Request r = new Request(null, null);
			InetAddress i = InetAddress.getByName(c.getIP());
	        Socket skt = new Socket();
	        skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
			String s = r.starUnstarEmailReq(starred,username, foldername, email);
			DataInputStream dis = new DataInputStream(skt.getInputStream());
            DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
            dos.writeUTF(s);
            String res = dis.readUTF();
            skt.close();
            boolean b = r.starUnstarEmailRes(res);
            if(!b) {
            	throw new Exception();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

}
