package mainClient;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mainClient.controls.EnterIPControls;
import mainClient.controls.rootlayoutControl;

public class ClientFirstTry extends Application {
	private String username = null;
	Stage primaryStage;
    private BorderPane rootLayout;
    private String Ip = "55";
    private int port = 0;

    public BorderPane getRoot() {
    	return rootLayout;
    }
    public  void setUsername(String n) {
		username = n;
	}
    public  String getUsername() {
	 return	username ;
	}
    public void setIP(String s) {
        Ip = s;
    }
    public void setPort(int n) {
        port = n;
    }
    public int getPort() {
        return port;
    }
    public String getIP() {
        return Ip;
    }
    @Override

	public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TAK Email");
        this.primaryStage.centerOnScreen();
        this.primaryStage.initStyle(StageStyle.TRANSPARENT);
       // this.primaryStage.setFullScreen(true);



       initRootLayout();
        addEnterIP();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientFirstTry.class.getResource("view/RootLayout.fxml"));
            rootlayoutControl dd = new rootlayoutControl();
            loader.setController(dd);
            rootLayout = (BorderPane) loader.load();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
              primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addEnterIP() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientFirstTry.class.getResource("view/EnterIP.fxml"));
            EnterIPControls c = new EnterIPControls(this, rootLayout);
            loader.setController(c);
            AnchorPane enterIp = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(enterIp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

}
