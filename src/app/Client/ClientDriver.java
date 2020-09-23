package app.Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientDriver extends Application {

    /**
     * Starts the FXML Application, creates the main stage, and shows it
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Client start
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientUI.fxml"));
        Parent client = (Parent)loader.load();
        primaryStage.setTitle("Client");
        primaryStage.setScene(new Scene(client, 768, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
        ClientControl controller = (ClientControl)loader.getController();
        primaryStage.setOnCloseRequest(event -> {
            try {
                controller.logOut();
                System.out.println("Client process closed");
                Platform.exit();
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}
