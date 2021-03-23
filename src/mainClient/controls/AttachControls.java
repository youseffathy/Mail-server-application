/**
 *
 */
package mainClient.controls;

import java.io.File;

import dataStructure.linkedList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * @author SHIKO
 *
 */
public class AttachControls {
	/**
	 * String filename
	 */
	private String filename = null;
	/**
	 * linkedList files contains attachments
	 */
	private linkedList files = null;
	/**
	 *  observable list of HBox view of attachments
	 */
	private ObservableList<HBox> list;
	/**
	 *
	 * @param file
	 * @param l
	 * @param o
	 */
	public AttachControls(String file, linkedList l, ObservableList<HBox> o) {
		filename = file ;
		files = l;
		list = o;

	}

	@FXML
	private Label attachName ;
	@FXML
	private Button removeAttach;

	/**
	 * initialize the controls of the removing attachments
	 */
	@FXML
	private void initialize() {
		attachName.setText(filename);
		removeAttach.setOnAction((event)->{

			for(int i = 0; i < list.size(); i++) {
				HBox h = list.get(i);
				ObservableList<Node> childern = h.getChildren();
				for(int j=0;j<childern.size();j++) {
					Node n = childern.get(j);
					if( n instanceof Label) {
						Label ll = (Label)n;
						if(ll.getText().equals(filename)) {
							list.remove(i);
							break;
						}
					}
				}


			}
			for(int i = 0; i < files.size(); i++) {
				File f = (File) files.get(i);
				if(f.getName().equals(filename)) {
					files.remove(i);

				}
			}
		});
	}

}
