/**
 *
 */
package server;



import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import serverClient.Server1;

/**
 * @author SHIKO
 *
 */
public class ServerControl {
	private Stage ps ;
	public ServerControl(Stage ps) {
		this.ps = ps;
	}
	private String port ;
	@FXML
	private Button getPort;
	@FXML
	private TextField portNum;
	@FXML
	private void initialize() {
		getPort.setOnAction((event)->{
			port = portNum.getText();
			ps.hide();
			Server1 s = new Server1();
			s.setPort(port);
			try {
				s.serverstart();
			} catch (Exception e) {
				// TODO: handle exception
			}

		});
	}
}
