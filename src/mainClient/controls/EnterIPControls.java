package mainClient.controls;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import mainClient.ClientFirstTry;

/**
 * @author Muhammad Salah
 * the controls of the enter IP scene
 */
public class EnterIPControls {
    /**
     * button to connect to the server
     */
    @FXML
    private Button connect;
    /**
     * the ip to connect to
     */
    @FXML
    private TextField ip;
    /**
     * the port number
     */
    @FXML
    private TextField port;
    /**
     * main class of the client
     */
    private ClientFirstTry c = null;
    /**
     * the root layout border pane
     */
    private BorderPane border;

    /**
     * @param z main class of the client
     * @param b the root layout border pane
     */
    public EnterIPControls(ClientFirstTry z, BorderPane b) {
        // TODO Auto-generated constructor stub
        c = z;
        border = b;
    }
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Handle Button event.
        connect.setOnAction((event) -> {
             c.setIP(ip.getText());
             c.setPort(Integer.valueOf(port.getText()));
             try {
                  //getting localhost ip
                 InetAddress i = InetAddress.getByName(c.getIP());

                 // establish the connection with server


                 Socket skt = new Socket();
                 //connection timeout after one second
                 skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
                 DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
                 dos.writeUTF(new String("exit"));
                 skt.close();
                 //confirm that the server on the port and ip specified exists
                 Alert a = new Alert(AlertType.CONFIRMATION);
                 a.setContentText("Connection success");
                 a.showAndWait();
                 //load the login scene
                 try {
                     // Load person overview.
                     FXMLLoader loader = new FXMLLoader();
                     loader.setLocation(ClientFirstTry.class.getResource("view/LoginRegister.fxml"));
                     LoginRegControls l = new LoginRegControls(c);
                     loader.setController(l);
                     AnchorPane loginReg = (AnchorPane) loader.load(); 
                     // Set person overview into the center of root layout.
                     border.setCenter(loginReg);

                 } catch (Exception e) {
                	 e.printStackTrace();
                     Alert al = new Alert(AlertType.ERROR);
                     al.setContentText("Coudn't load next scene!\n please reinstall the program.");
                     al.showAndWait();
                 }
             }catch(Exception e){
                 Alert a = new Alert(AlertType.ERROR);
                 a.setContentText("no server on this port");
                 a.showAndWait();
             }
        });

    }
}
