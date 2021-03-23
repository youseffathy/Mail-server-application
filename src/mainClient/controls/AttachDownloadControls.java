/**
 *
 */
package mainClient.controls;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.json.simple.JSONArray;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author SHIKO
 *
 */
public class AttachDownloadControls {

	private JSONArray file = null;
	private String name = null;
	@FXML
	private Label attachName;
	@FXML
	private Button downloadAttach;
	/**
	 *
	 * @param name
	 * @param file
	 */
	public AttachDownloadControls (String name , JSONArray file) {
		this.file =file;
		this.name = name;
	}
	/**
	 *intailize the controls of downloading attachments
	 */
	@FXML
	private void initialize() {
		attachName.setText(name);
		downloadAttach.setOnAction((event)->{
			try {
				byte[] filebytes = jsonArrayToByteArray();

				FileChooser fileDir = new FileChooser();
				int index = name.lastIndexOf(".");
				index++;
				String s = name.substring(index);
				String upper = touppercase(s);
				FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(upper, "*." + s);
				fileDir.getExtensionFilters().add(filter);
				fileDir.setInitialFileName(name);
				Stage stage = (Stage) downloadAttach.getScene().getWindow();
				File f = fileDir.showSaveDialog(stage);
				if(f !=null) {
				    FileOutputStream fos = new FileOutputStream(f);
	                BufferedOutputStream bos = new BufferedOutputStream(fos);
	                bos.write(filebytes);
	                bos.close();
	                fos.close();
	                Alert a = new Alert(AlertType.CONFIRMATION);
	                a.setContentText("file downloaded successfully");
	                a.showAndWait();
	                Desktop.getDesktop().open(f);
				}

			} catch (Exception e) {
				 Alert a = new Alert(AlertType.ERROR);
	              a.setContentText("error.Try again");
	              a.showAndWait();
	              e.printStackTrace();
			}

		});
	}
	private byte[] jsonArrayToByteArray () {
		byte[] array = new byte[file.size()];
		for(int i = 0 ;i<file.size();i++) {
			array[i] = Byte.valueOf((String)file.get(i));
		}
		return array;
	}

	private String touppercase (String s) {
		StringBuilder str = new StringBuilder();
		for(int j=0 ; j<s.length();j++) {
			str.append(Character.toUpperCase(s.charAt(j)));
		}
		str.append(" ");
		str.append("files (*.");
		str.append(s);
		str.append(")");

		return str.toString();
	}
}
