/**
 *
 */
package server;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author SHIKO
 *
 */
public class ServerStart extends Application {
	private Stage primaryStage ;
	private BorderPane rootLayout;
	   @Override
		public void start(Stage primaryStage) throws Exception {
		   this.primaryStage = primaryStage;
	        this.primaryStage.setTitle("server");
	        initRootLayout();

	    }
	   public void initRootLayout() {
	        try {

	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(ServerStart.class.getResource("view/serverstart.fxml"));
	            ServerControl sc = new ServerControl(primaryStage);
	            loader.setController(sc);
	            rootLayout = (BorderPane) loader.load();
	            // Show the scene containing the root layout.
	            Scene scene = new Scene(rootLayout);
	            primaryStage.setScene(scene);
	             primaryStage.show();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	   public static void main(String[] args) {
	        launch(args);
	    }
}
