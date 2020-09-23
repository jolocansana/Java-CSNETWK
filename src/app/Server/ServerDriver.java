package app.Server;

import app.Client.ClientControl;
import app.model.messageStruct;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.Map;

public class ServerDriver extends Application {

    /**
     * Starts the FXML Application, creates the main stage, and shows it
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Server start
        Stage serverStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ServerUI.fxml"));
        Parent root = (Parent)loader.load();
        serverStage.setTitle("Server");
        serverStage.setScene(new Scene(root, 1024, 600));
        serverStage.setResizable(false);
        serverStage.show();
        ServerControl controller = (ServerControl) loader.getController();
        serverStage.setOnCloseRequest(event -> {
            try {
                // STOP SERVER FROM STOPPING WHILE AT LEAST 1 CLIENT CONNECTED
                if (controller.connectionThreads.isEmpty()) {
                    Platform.exit();
                    System.exit(0);
                } else {
                    event.consume();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to close server: At least 1 client is still connected!");
                    alert.show();
                }
                // DISCONNECT USERS
//                for(Map.Entry<String, ServerThread> thread : controller.connectionThreads.entrySet()) {
//                    thread.getValue().dosWriter.writeObject(new messageStruct("invalid", null, null, null, "Server closed.", null));
//                };

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    };

    /**
     * Main driver
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
