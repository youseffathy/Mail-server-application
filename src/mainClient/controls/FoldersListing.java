package mainClient.controls;

import java.io.IOException;

import org.json.simple.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import mainClient.ClientFirstTry;

/**
 * @author SHIKO
 *
 */
public class FoldersListing {
	/**
	 * the json object that contains the users folders
	 */
	private JSONObject folders = null;
	/**
	 * the observable list that holds the folders of the user to set the items of the listview
	 */
	private ObservableList<HBox> items = FXCollections.observableArrayList();
	/**
	 * the controls of the layout of the mail
	 */
	private EmailLayoutControls e;
	/**
	 * the listviw that holds the folders
	 */
	ListView<HBox> list = null;
	/**
	 * main class of the client
	 */
	ClientFirstTry x = null;
	/**
	 * @param z main class of the client
	 * @param l the listviw that holds the folders
	 * @param h the controls of the layout of the mail
	 */
	public FoldersListing(ClientFirstTry z, ListView<HBox> l, EmailLayoutControls h) {
	    x = z;
	    list = l;
	    e = h;
	}
	/**
	 * @param f the json that holds the folders of the user
	 * set the folders
	 */
	public void setJson(JSONObject f) {
		folders = f;
	}

	/**
	 * @return the json that holds the folders of the user
	 * get the folders
	 */
	public JSONObject getJson() {
		return folders;
	}

	/**
	 * @return the items of the folders
	 */
	public ObservableList<HBox> getEmailList() {
		return items;
	}

	/**
	 * @throws IOException loader exception
	 * sets the observable list that holds the folders from the JSON object
	 */
	public void setFolderItems() throws IOException {
		int foldersnum = Integer.valueOf((String) folders.get("foldersNum"));
		for (int i = 0; i <= foldersnum; i++) {
			String fname = (String) folders.get(String.valueOf(i));
			if(fname.equals("Stared")) {
			    continue;
			}
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(
			ClientFirstTry.class.getResource("view/HboxFolder.fxml"));

			FolderHBoxControls c =
			new FolderHBoxControls(fname, (String) folders.get(fname), x, list, i, e);
			loader.setController(c);
			HBox folder = (HBox) loader.load();
			folder.setOnDragOver((event)->{

			        if (event.getGestureSource() != folder &&
			                event.getDragboard().hasString()) {

			            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			        }

			        event.consume();

			});

			folder.setOnDragEntered((event)->{
				folder.setStyle("-fx-border-color: #1a474a;");
			});
			folder.setOnDragExited((event)->{
				folder.setStyle("");
			});
			folder.setOnDragDropped((event)->{
				 Dragboard db = event.getDragboard();
			        boolean success = false;
			        if (db.hasString()) {
			        	EmailsSelectedControls esc = new EmailsSelectedControls(x, e);
						esc.moveEmail(db.getString(), fname);
			           success = true;
			        }
			        event.setDropCompleted(success);
			        event.consume();
			});

			items.add(folder);
		}
	}
}
