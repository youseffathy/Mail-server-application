/**
 *
 */
package mainClient.controls;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import mainClient.ClientFirstTry;
import serverClient.Request;

/**
 * @author SHIKO
 *
 */
public class FolderHBoxControls {
	@FXML
	private Label folderName;
	@FXML
	private Label emailsNum;
	@FXML
	private MenuButton folderOptions;
	/**
	 * folder name
	 */
	private String folder = null;
	/**
	 * number of emails
	 */
	private String emails = null;
	/**
	 * main class of the client
	 */
	private ClientFirstTry c = null;
	/**
	 * the index of the folder in the list
	 */
	private int index = 0;
	/**
	 * controls of the layout of the mail
	 */
	private EmailLayoutControls e = null;
	/**
	 * the list of the folders
	 */
	private ListView<HBox> list = null;
	/**
	 * the essential folders that can't be removed or renamed 
	 */
	private String[] folders =
        {"Inbox", "Trash", "Sent", "Drafts", "Spam", "Stared" };
	/**
	 * @param fName folder name
	 * @param ENum number of emails
	 * @param z main class of the client
	 * @param l list of folders
	 * @param i index of the folder
	 * @param h email layout controls
	 */
	public FolderHBoxControls(String fName, String ENum, ClientFirstTry z, ListView<HBox> l, int i, EmailLayoutControls h) {
	    folder = fName;
	    emails = ENum;
	    c = z;
	    list = l;
	    index = i;
	    e = h;
	}
	/**
	 * initialize the HBox of the folder (folder name, number of emails, folder options)
	 */
	@FXML
	private void initialize() {
	    folderName.setText(folder);
        emailsNum.setText(emails);
        boolean essential = false;
        //check if the folder is in the essential folders
        for(int i = 0; i < folders.length; i++) {
            if(folder.equals(folders[i])) {
                essential = true;
                break;
            }
        }
        //load the emails on the folder click
        folderName.getParent().setOnMouseClicked((event) -> {
            e.emailsloader(folderName.getText());
        });
        //set the folder options if the folder is not essential
        if(!essential) {
            ObservableList<MenuItem> options = FXCollections.observableArrayList();
            options = folderOptions.getItems();
            MenuItem m1 = options.get(0);
            //first action delete
            m1.setOnAction((event) -> {
                boolean b = false;
                try {
                    b = deleteFolder();
                } catch (Exception e) {
                    b = false;
                }
                if(b) {
                    list.getItems().remove(index);
                    Alert a = new Alert(AlertType.CONFIRMATION);
                    a.setContentText("Deleted");
                    a.showAndWait();
                } else {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Cannot Delete This Folder!");
                    a.showAndWait();
                }

            });
            MenuItem m2 = options.get(1);
            //second action rename
            m2.setOnAction((event) -> {
                TextInputDialog d = new TextInputDialog();
                d.setTitle("Rename Folder");
                d.setHeaderText("Rename Folder");
                d.setContentText("Enter Folder Name:");

                Optional<String> results = d.showAndWait();
                if(!results.isPresent()) {
                    return;
                }
                String newFolder = results.get();
                boolean b = false;
                try {
                    b = renameFolder(newFolder);
                } catch (Exception e) {
                    b = false;
                }
                if(b) {
                    folderName.setText(newFolder);
                    folder = newFolder;
                    Alert a = new Alert(AlertType.CONFIRMATION);
                    a.setContentText("Renamed");
                    a.showAndWait();
                } else {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Cannot Rename This Folder!");
                    a.showAndWait();
                }
            });
        } else {
            folderOptions.setVisible(false);
        }

	}
	/**
	 * @return boolean deleted or not
	 * @throws Exception connection exception
	 * delete a folder: open socket, connect to server, create request, send, receive boolean response
	 * close the socket
	 */
	private boolean deleteFolder() throws Exception {
	    Request req = new Request(null, null);
	    String send = req.deleteFolderReq(c.getUsername(), folder);
	    InetAddress i = InetAddress.getByName(c.getIP());
        Socket skt = new Socket();
        skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
        DataInputStream dis = new DataInputStream(skt.getInputStream());
        DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
        dos.writeUTF(send);
        String res = dis.readUTF();
        skt.close();
        return req.deleteFolderRes(res);
	}
	/**
	 * @param newFolder the new name
	 * @return boolean renamed successfully
	 * @throws Exception connection exception
	 */
	private boolean renameFolder(String newFolder) throws Exception {
	    Request req = new Request(null, null);
        String send = req.renameFolderReq(c.getUsername(), folder, newFolder);
       
        InetAddress i = InetAddress.getByName(c.getIP());
        Socket skt = new Socket();
        skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
        DataInputStream dis = new DataInputStream(skt.getInputStream());
        DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
        dos.writeUTF(send);
        String res = dis.readUTF();
        skt.close();
        return req.renameFolderRes(res);
	}
}
