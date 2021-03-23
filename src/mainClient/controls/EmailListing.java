
package mainClient.controls;

import org.json.simple.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import mainClient.ClientFirstTry;

public class EmailListing {
	/**
	 * email number to be moved or deleted
	 */
	private String n1;
	JSONObject emails = null;
	AnchorPane view = null;
	String username = null;
	String foldername = null;
	private ObservableList<HBox> items = FXCollections.observableArrayList();
	ClientFirstTry c = null;
	private EmailLayoutControls elc = null;

	public EmailListing(ClientFirstTry z, AnchorPane scene,
	EmailLayoutControls y) {
		// TODO Auto-generated constructor stub
		view = scene;
		c = z;
		elc = y;
	}

	public ObservableList<HBox> getEmailList() {
		return items;
	}

	public void setUsername(String name) {
		username = name;
	}

	public void setfoldername(String fname) {
		foldername = fname;
	}

	public void setJson(JSONObject e) {
		emails = e;
	}

	public JSONObject getJson() {
		return emails;
	}
	/**
	 * set the e mail HBox in a list
	 * set the drag and drop action on the e mails
	 * @throws Exception no loading exception
	 */
	public void setEmailList() throws Exception {
		int emailsnum = Integer.valueOf((String) emails.get("EmailsNum"));
		for (int i = emailsnum; i > 0; i--) {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(
			ClientFirstTry.class.getResource("view/HboxEmails.fxml"));

			String[] contents =
			((String) emails.get(String.valueOf(i))).split(" : ");
			EmailListingControls x = new EmailListingControls(c, username,
			foldername, i, contents[0], contents[1], contents[2], contents[3],
			contents[4], contents[5], elc);
			loader.setController(x);
			HBox email = (HBox) loader.load();
			n1 = String.valueOf(i);
			email.setOnDragDetected((event) -> {
				elc.loadselectedOptions();
				Dragboard db = email.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				Node n = email;
				WritableImage image =
				n.snapshot(new SnapshotParameters(), null);

				content.putImage(image);
				content.putString(n1);
				db.setContent(content);
				event.consume();
			});
			email.setOnDragDone((event) -> {
				/* the drag and drop gesture ended */
				/* if the data was successfully moved, clear it */
				if (event.getTransferMode() == TransferMode.MOVE) {
					elc.emailsloader(foldername);
					elc.foldersLoader();
					elc.loadUnselectedOptions();
				}
				event.consume();

			});

			emailOpen(email, i);
			items.add(email);
		}

	}
	/**
	 *
	 * @param h HBox of the email that the user clicked to be viewed
	 *
	 * @param emailnum the email to be viewed
	 */
	private void emailOpen(HBox h, int emailnum) {

		h.setOnMouseClicked((event) -> {

			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(
				ClientFirstTry.class.getResource("view/EmailView.fxml"));
				EmailViewControls con =
				new EmailViewControls(c, String.valueOf(emailnum), foldername);
				loader.setController(con);
				view.getChildren().removeAll(view.getChildren());
				view.getChildren().add((AnchorPane) loader.load());

			} catch (Exception e) {
				e.printStackTrace();
			}

		});
	}
}
