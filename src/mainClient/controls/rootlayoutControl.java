/**
 *
 */
package mainClient.controls;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * @author SHIKO
 *
 */
public class rootlayoutControl {
	/**
	 * exit Button in the layout
	 */
	@FXML
	private Button exit;
	/**
	 * initialize the exit button of the program
	 */
	@FXML
	private void initialize() {
		exit.setOnAction((event) -> {
			Stage stage = (Stage) exit.getScene().getWindow();
		    // do what you have to do
		    stage.close();
		});
	}
}
