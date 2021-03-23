package mainClient.controls;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import mainClient.ClientFirstTry;
import serverClient.Request;

/**
 * @author Muhammad Salah
 *
 */
/**
 * @author Muhammad Salah
 *
 */
public class LoginRegControls {

    /**
     * button that performs the acction of creating a new account
     */
    @FXML
    private Button CreateAcc;
    /**
     * user name in the registeration form
     */
    @FXML
    private TextField RegUser;
    /**
     * password in the reg form
     */
    @FXML
    private PasswordField RegPass;
	/**
	 * password confirmation
	 */
	@FXML
	private PasswordField RegRePass;
	/**
	 * button that performs the login acction
	 */
	@FXML
    private Button logBtn;
    /**
     * user name in the login form
     */
    @FXML
    private TextField LogUser;
    /**
     * password in the login form
     */
    @FXML
    private PasswordField LogPass;
    /**
     * main class of the client
     */
    private ClientFirstTry c = null;
    /**
     * user name of the logged in or registered user
     */
    String username = null;
    /**
     * password of the logged in or registered user
     */
    String password = null;

    /**
     * @param main main client of the user
     */
    public LoginRegControls(ClientFirstTry main) {
        c = main;

    }
    /**
     * initialize the login scene controls
     */
    @FXML
    private void initialize() {
        //login button controls
    	logBtn.setOnAction((event)->{
    	    //get the username and password from the text fields
    		username = LogUser.getText();
    		password = LogPass.getText();
    		try {
        		InetAddress i = InetAddress.getByName(c.getIP());
                // establish the connection with server port
                Socket skt = new Socket();
                skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
                Request request =  new Request(null, null);
                //create a login request with the username and password of the client
                String s = request.loginClient(username, password);

                DataInputStream dis = new DataInputStream(skt.getInputStream());
                DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
                //send to the server
                dos.writeUTF(s);
                String res = dis.readUTF();
                //get the response
                boolean b = request.loginResponse(res);
                skt.close();
                //validate and change the scene
                if(b) {
                	c.setUsername(username);
                	Alert a = new Alert(AlertType.CONFIRMATION);
                    a.setContentText("Logged in successfully");
                    a.showAndWait();
                    try {
                    	FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(ClientFirstTry.class.getResource("view/EmailLayout.fxml"));

                        EmailLayoutControls l = new EmailLayoutControls(c);
                        loader.setController(l);
                        AnchorPane EmailLayout = (AnchorPane) loader.load();
                        // Set person overview into the center of root layout.
                        c.getRoot().setCenter(EmailLayout);
					} catch (Exception e) {
						e.printStackTrace();
						Alert x = new Alert(AlertType.ERROR);
		                x.setContentText("Failed to load, reinstall the program and try again");
		                x.showAndWait();
					}
                } else {
                    throw new Exception();
                }

    		} catch (Exception e) {

    			Alert a = new Alert(AlertType.ERROR);
                a.setContentText("User Doesn't Match. Try Again");
                a.showAndWait();
    		}
    	});
    	//create account button action
    	CreateAcc.setOnAction((event)-> {
    	  //get the username and password from the text fields
    		username = RegUser.getText();
    		password = RegPass.getText();
    		//validate the password with the re enter password field and alert error
    		if(!(RegPass.getText()).equals(RegRePass.getText())) {
    			Alert a = new Alert(AlertType.ERROR);
                a.setContentText("password Doesn't Match. Try Again");
                a.showAndWait();
    		} else {
    			try {
    			    //connect to the server
    				InetAddress i = InetAddress.getByName(c.getIP());
                    Socket skt = new Socket();
                    skt.connect(new InetSocketAddress(i, c.getPort()), 1000);
                    //create a login req
                    Request request =  new Request(null, null);
                    String s = request.reqRegister(username, password);
                    DataInputStream dis = new DataInputStream(skt.getInputStream());
                    DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
                    //send to server
                    dos.writeUTF(s);
                    String res = dis.readUTF();
                    //get response
                    boolean b = request.loginResponse(res);
                    skt.close();
                    //validate and set the logged in username and load the email layout scene
                    if(b) {
                    	c.setUsername(username);
                    	Alert a = new Alert(AlertType.CONFIRMATION);
                        a.setContentText("Registeration done successfully");
                        a.showAndWait();
                        try {
                        	FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(ClientFirstTry.class.getResource("view/EmailLayout.fxml"));
                            EmailLayoutControls l = new EmailLayoutControls(c);
                            loader.setController(l);
                            AnchorPane EmailLayout = (AnchorPane) loader.load();
                            // Set person overview into the center of root layout.
                            c.getRoot().setCenter(EmailLayout);
						} catch (Exception e) {
							Alert x = new Alert(AlertType.ERROR);
			                x.setContentText("Failed to load, reinstall the program and try again");
			                x.showAndWait();
						}
                    } else {
                        throw new Exception();
                    }
				} catch (Exception e) {
					Alert a = new Alert(AlertType.ERROR);
	                a.setContentText("registeration failed. Try Again");
	                a.showAndWait();
				}

    		}
    	}
    	);

    }
}
