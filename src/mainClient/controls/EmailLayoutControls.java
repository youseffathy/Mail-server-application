package mainClient.controls;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.json.simple.JSONObject;

import ServerFileSystem.Filter;
import dataStructure.DoubleLinkedList;
import dataStructure.Stack;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mainClient.ClientFirstTry;
import serverClient.Request;

/**
 *
 * @author SHIKO
 *
 */
public class EmailLayoutControls {
	/**
	 * client main class
	 */
	private ClientFirstTry c = null;
	/**
	 * folder name recently opened in the gui
	 */
	private String fname = null;

	/**
	 *
	 * @return foldername opened in the gui
	 */
	public String getfname() {
		return fname;
	}

	@FXML
	private MenuItem dateSearch;
	@FXML
	private MenuItem fromSearch;
	@FXML
	private MenuItem subSearch;
	@FXML
	private DatePicker date;
	@FXML
	private TextField subjectField;
	@FXML
	private TextField fromField;
	@FXML
	private AnchorPane emailScene;
	@FXML
	private ListView<HBox> foldersList;
	@FXML
	private Button newFolder;
	@FXML
	private Button compose;
	@FXML
	private MenuButton selectBtn;
	@FXML
	private MenuButton contacts;
	@FXML
	private AnchorPane options;
	@FXML
	private Button logout;
	/**
	 *
	 * @return listview of the folder shown
	 */
	public ListView getFolderList() {
		return foldersList;
	}
	/**
	 *
	 * @param z client main class
	 */

	public EmailLayoutControls(ClientFirstTry z) {
		// TODO Auto-generated constructor stub
		c = z;
	}
	/**
	 *
	 * @return emails anchorpane to be set as email view or emailslist
	 */
	public AnchorPane getEmailScene() {
		return emailScene;
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		// Handle Button event.
		foldersLoader();
		emailsloader("Inbox");
		newFolderControls();
		composecontrol();
		selectBtnControls();
		loadUnselectedOptions();
		contactsAction();
		dateBinarySearch();
		subSearchAction();
		fromSearchAction();
		logoutCont();

		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/**
	 *
	 * @return JSONObject of user email folders
	 */
	private JSONObject folders() {
		try {
			InetAddress i = InetAddress.getByName(c.getIP());
			Socket skt = new Socket();
			skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
			Request request = new Request(null, null);
			String s = request.getFoldersReq(c.getUsername());
			DataInputStream dis = new DataInputStream(skt.getInputStream());
			DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
			dos.writeUTF(s);
			String res = dis.readUTF();
			skt.close();
			return request.foldersResponse(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * loads folder in the scene of the user interface
	 */
	public void foldersLoader() {
		try {
			JSONObject j = folders();

			FoldersListing list = new FoldersListing(c, foldersList, this);
			list.setJson(j);
			list.setFolderItems();
			ObservableList<HBox> items = list.getEmailList();
			foldersList.setItems(items);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 *
	 * @param n name of the folder of emails to be loaded
	 * @return JSONObject of emails
	 */
	private JSONObject loadEmails(String n) {
		try {
			InetAddress i = InetAddress.getByName(c.getIP());
			Socket skt = new Socket();
			skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
			Request request = new Request(null, null);
			String s = request.getEmailsInFolderRequest(c.getUsername(), n);
			DataInputStream dis = new DataInputStream(skt.getInputStream());
			DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
			dos.writeUTF(s);
			String res = dis.readUTF();
			skt.close();
			return request.EmailsResponse(res);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	/**
	 *
	 * @param n emails folder to be loaded
	 */
	public void emailsloader(String n) {
		fname = n;
		try {
			// ListView<HBox> emailList = null;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClientFirstTry.class
			.getResource("view/EmailsAnchorPane.fxml"));

			ListView<HBox> emailList = (ListView) loader.load();

			ObservableList<Node> childern = emailScene.getChildren();

			JSONObject emails = loadEmails(n);
			EmailListing list = new EmailListing(c, emailScene, this);
			list.setUsername(c.getUsername());
			list.setfoldername(n);
			list.setJson(emails);
			list.setEmailList();
			ObservableList<HBox> items = list.getEmailList();
			emailList.setItems(items);
			childern.removeAll(childern);
			childern.add(emailList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * creating a new folder controls
	 */
	private void newFolderControls() {
		newFolder.setOnAction((event) -> {
			TextInputDialog d = new TextInputDialog();
			d.setTitle("New Folder");
			d.setHeaderText("New Folder");
			d.setContentText("Enter Folder Name:");
			Optional<String> results = d.showAndWait();
			if (!results.isPresent()) {
				return;
			}
			boolean b = false;
			try {
				String folderName = results.get();
				Request request = new Request(null, null);
				String req =
				request.createFolderReq(c.getUsername(), folderName);
				InetAddress i = InetAddress.getByName(c.getIP());
				Socket skt = new Socket();
				skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
				DataInputStream dis = new DataInputStream(skt.getInputStream());
				DataOutputStream dos =
				new DataOutputStream(skt.getOutputStream());
				dos.writeUTF(req);
				String res = dis.readUTF();
				skt.close();
				b = request.newFolderRes(res);
			} catch (Exception e) {
				b = false;
			}
			if (b) {
				foldersLoader();
			}
		});
	}
	/**
	 * compose a new email controls
	 */
	private void composecontrol() {
		compose.setOnAction((event) -> {
			openCompose("");
		});
	}
	/**
	 * open a new scene for the controls
	 * @param to
	 */
	private void openCompose(String to) {
		try {

			Stage stage = new Stage();
			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setX(748);
			stage.setY(248);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClientFirstTry.class
			.getResource("view/ComposeLayout.fxml"));
			ComposeControls x = new ComposeControls(c, to, this);
			loader.setController(x);
			BorderPane bb = (BorderPane) loader.load();
			Scene s = new Scene(bb);
			stage.setScene(s);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * selecting emails controls
	 */
	private void selectBtnControls() {
		ObservableList<MenuItem> selections = selectBtn.getItems();
		MenuItem m1 = selections.get(0);
		selectAll(m1);
		MenuItem m2 = selections.get(1);
		selectNone(m2);
		MenuItem m3 = selections.get(2);
		selectRead(m3);
		MenuItem m4 = selections.get(3);
		selectUnRead(m4);
		MenuItem m5 = selections.get(4);
		selectStarred(m5);
		MenuItem m6 = selections.get(5);
		selectUnStarred(m6);

	}
	/**
	 * select all e mails
	 * @param m menu item to be used to select all e mails
	 */
	private void selectAll(MenuItem m) {
		m.setOnAction((event) -> {
			ListView<HBox> mails = (ListView) emailScene.getChildren().get(0);
			ObservableList<HBox> mailslist = mails.getItems();
			for (int i = 0; i < mailslist.size(); i++) {
				HBox h = mailslist.get(i);
				ObservableList<Node> nodes = h.getChildren();
				Node n = nodes.get(1);
				((CheckBox) n).setSelected(true);

			}
			isSelection();
		});
	}
	/**
	 * select none e mails
	 * @param m menu item to be used to select none e mails
	 */
	private void selectNone(MenuItem m) {
		m.setOnAction((event) -> {
			UnSelect();
			isSelection();
		});
	}
	/**
	 * unselect the selected e mails
	 */
	private void UnSelect() {
		ListView<HBox> mails = (ListView) emailScene.getChildren().get(0);
		ObservableList<HBox> mailslist = mails.getItems();
		for (int i = 0; i < mailslist.size(); i++) {
			HBox h = mailslist.get(i);
			ObservableList<Node> nodes = h.getChildren();
			Node n = nodes.get(1);
			((CheckBox) n).setSelected(false);

		}
	}
	/**
	 * select the read emails
	 * @param m m menu item to be used to select read e mails
	 */
	private void selectRead(MenuItem m) {
		m.setOnAction((event) -> {
			UnSelect();
			ListView<HBox> mails = (ListView) emailScene.getChildren().get(0);
			ObservableList<HBox> mailslist = mails.getItems();
			for (int i = 0; i < mailslist.size(); i++) {
				HBox h = mailslist.get(i);
				ObservableList<Node> nodes = h.getChildren();

				Node n = nodes.get(8);
				if (((Label) n).getText().equals("read")) {
					Node no = nodes.get(1);
					((CheckBox) no).setSelected(true);
				}

			}
			isSelection();
		});
	}
	/**
	 * select the unread emails
	 * @param m m menu item to be used to select unread e mails
	 */
	private void selectUnRead(MenuItem m) {
		m.setOnAction((event) -> {
			UnSelect();
			ListView<HBox> mails = (ListView) emailScene.getChildren().get(0);
			ObservableList<HBox> mailslist = mails.getItems();
			for (int i = 0; i < mailslist.size(); i++) {
				HBox h = mailslist.get(i);
				ObservableList<Node> nodes = h.getChildren();

				Node n = nodes.get(8);
				if (((Label) n).getText().equals("unread")) {
					Node no = nodes.get(1);
					((CheckBox) no).setSelected(true);
				}
			}
			isSelection();
		});
	}
	/**
	 * select the starred emails
	 * @param m m menu item to be used to select starred e mails
	 */
	private void selectStarred(MenuItem m) {
		m.setOnAction((event) -> {
			UnSelect();
			ListView<HBox> mails = (ListView) emailScene.getChildren().get(0);
			ObservableList<HBox> mailslist = mails.getItems();
			for (int i = 0; i < mailslist.size(); i++) {
				HBox h = mailslist.get(i);
				ObservableList<Node> nodes = h.getChildren();

				Node n = nodes.get(2);
				if (((Button) n).getText().equals("starred")) {
					Node no = nodes.get(1);
					((CheckBox) no).setSelected(true);
				}
			}
			isSelection();
		});
	}
	/**
	 * select the unstarred emails
	 * @param m m menu item to be used to select unstarred e mails
	 */
	private void selectUnStarred(MenuItem m) {
		m.setOnAction((event) -> {
			UnSelect();
			ListView<HBox> mails = (ListView) emailScene.getChildren().get(0);
			ObservableList<HBox> mailslist = mails.getItems();
			for (int i = 0; i < mailslist.size(); i++) {
				HBox h = mailslist.get(i);
				ObservableList<Node> nodes = h.getChildren();

				Node n = nodes.get(2);
				if (((Button) n).getText().equals("unstarred")) {
					Node no = nodes.get(1);
					((CheckBox) no).setSelected(true);
				}
			}
			isSelection();
		});
	}
	/**
	 * load the  refresh button ,select menu button ,mark all as read , sort by
	 */
	public void loadUnselectedOptions() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(
			ClientFirstTry.class.getResource("view/ViewOptions.fxml"));
			ViewOptionsControls con = new ViewOptionsControls(this, c);
			loader.setController(con);
			AnchorPane a = (AnchorPane) loader.load();
			options.getChildren().removeAll(options.getChildren());
			options.getChildren().add(a);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * load the options of e mails (move ,delete,select)
	 */
	public void loadselectedOptions() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(
			ClientFirstTry.class.getResource("view/EmailOptions.fxml"));
			EmailsSelectedControls es = new EmailsSelectedControls(c, this);
			loader.setController(es);
			AnchorPane a = (AnchorPane) loader.load();
			options.getChildren().removeAll(options.getChildren());
			options.getChildren().add(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * function check if there is at least an email selected or not to load the selected emails options
	 */
	public void isSelection() {
		ListView<HBox> mails = (ListView) emailScene.getChildren().get(0);
		ObservableList<HBox> mailslist = mails.getItems();
		for (int i = 0; i < mailslist.size(); i++) {
			HBox h = mailslist.get(i);
			ObservableList<Node> nodes = h.getChildren();
			Node n = nodes.get(1);
			if (((CheckBox) n).isSelected()) {
				loadselectedOptions();
				return;
			}
		}
		loadUnselectedOptions();
	}
	/**
	 *
	 * @return JSONObject of names  of user contacts
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
	/**
	 * set the control  of the contacts MenuButton
	 */
	private void contactsAction() {
		contacts.setOnMouseClicked((event) -> {
			updateContacts();
		});

	}
	/**
	 * update the added or removed contacts
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
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(
				ClientFirstTry.class.getResource("view/ContactMenuItem.fxml"));
				RemoveContactControls co = new RemoveContactControls(this, c,
				(String) cont.get(String.valueOf(i)));
				loader.setController(co);
				MenuItem newm = loader.load();
				newm.setText((String) cont.get(String.valueOf(i)));
				newm.setOnAction((event) -> {
					openCompose(newm.getText());
				});
				con.add(newm);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * creates an new contact
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
	 * @return list of the hboxs of the emails
	 */
	public ObservableList<HBox> getEmails() {
		return ((ListView) emailScene.getChildren().get(0)).getItems();
	}
	/**
	 * date binary search implementation using stack
	 */
	private void dateBinarySearch() {
		dateSearch.setOnAction((event) -> {
			emailsloader(fname);
			Date d = java.sql.Date.valueOf(date.getValue());
			try {
				searchByDate(d);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
	}
	/**
	 *
	 * @param d date to be searched about
	 * @throws ParseException
	 */
	public void searchByDate(Date d) throws ParseException {
		ObservableList<HBox> dates = getEmails();
		Stack s = new Stack();
		HBox[] arr = new HBox[dates.size()];
		for (int j = 0; j < dates.size(); j++) {
			arr[j] = dates.get(j);
		}
		// clearing observable list to view search results
		dates.clear();
		int l = 0;
		int r = arr.length - 1;
		int mid;
		int index;
		s.push(new Point(l, r));
		while (!s.isEmpty()) {
			Point p = (Point) s.pop();
			mid = (p.x + p.y) / 2;
			String dateOfEmail =
			((Label) arr[mid].getChildren().get(5)).getText();
			String str = dateOfEmail.substring(5, 13);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date edate = sdf.parse(str);
			if (edate.compareTo(d) == 0) {
				index = mid;
				dates.add(arr[mid]);
				mid--;
				if (mid >= 0) {
					dateOfEmail =
					((Label) arr[mid].getChildren().get(5)).getText();
					str = dateOfEmail.substring(5, 13);
					edate = sdf.parse(str);
					while (mid >= 0 && edate.compareTo(d) == 0) {
						dateOfEmail =
						((Label) arr[mid].getChildren().get(5)).getText();
						str = dateOfEmail.substring(5, 13);
						edate = sdf.parse(str);
						dates.add(arr[mid]);
						mid--;
					}
				}

				mid = index;
				mid++;
				if (mid < arr.length) {
					dateOfEmail =
					((Label) arr[mid].getChildren().get(5)).getText();
					str = dateOfEmail.substring(5, 13);
					edate = sdf.parse(str);
					while (mid < arr.length && edate.compareTo(d) == 0) {
						dateOfEmail =
						((Label) arr[mid].getChildren().get(5)).getText();
						str = dateOfEmail.substring(5, 13);
						edate = sdf.parse(str);
						dates.add(arr[mid]);
						mid++;
					}
				}
				break;
			}
			if (edate.compareTo(d) < 0 && p.x != p.y) {
				s.push(new Point(p.x, mid - 1));

			} else if (edate.compareTo(d) > 0 && p.x != p.y) {
				s.push(new Point(mid + 1, p.y));

			}
		}
	}
	/**
	 * function search among e mails by subject
	 */
	private void subSearchAction() {
		subSearch.setOnAction((event) -> {
			emailsloader(fname);
			searchBySubject();
		});
	}
	/**
	 * function search among e mails by source
	 */
	private void fromSearchAction() {
		fromSearch.setOnAction((event) -> {
			emailsloader(fname);
			searchByFrom();
		});
	}
	/**
	 * search implementation for subject
	 */
	private void searchBySubject() {
		String sub = subjectField.getText();
		ObservableList<HBox> emails = getEmails();
		DoubleLinkedList dl = new DoubleLinkedList();
		Filter filter = new Filter();
		for (int i = 0; i < emails.size(); i++) {
			String emailSub =
			((Label) emails.get(i).getChildren().get(4)).getText();
			String[] arr = {emailSub, sub };
			if (!filter.isSimilar(arr)) {
				emails.remove(i);
				i--;
			}
		}
	}
	/**
	 * search implementation for soucre
	 */
	private void searchByFrom() {
		String fr = fromField.getText();
		ObservableList<HBox> emails = getEmails();
		DoubleLinkedList dl = new DoubleLinkedList();
		Filter filter = new Filter();
		for (int i = 0; i < emails.size(); i++) {
			String emailfrom =
			((Label) emails.get(i).getChildren().get(3)).getText();
			String[] arr = {emailfrom, fr };
			if (!filter.isSimilar(arr)) {
				emails.remove(i);
				i--;
			}
		}
	}
	/**
	 * logout button controls loads the log in scene
	 */
	private void logoutCont() {
		logout.setOnAction((event) -> {
			try {
				c.setUsername(null);
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(ClientFirstTry.class
				.getResource("view/LoginRegister.fxml"));
				LoginRegControls l = new LoginRegControls(c);
				loader.setController(l);
				AnchorPane loginReg = (AnchorPane) loader.load();
				// Set person overview into the center of root layout.
				c.getRoot().setCenter(loginReg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
