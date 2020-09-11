package app.Server;

import app.Client.ClientControl;
import app.model.messageStruct;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.Map;

public class ServerDriver extends Application {

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
                for(Map.Entry<String, ServerThread> thread : controller.connectionThreads.entrySet()) {
                    thread.getValue().dosWriter.writeObject(new messageStruct("invalid", null, null, null, "Server closed.", null));
                };
                Platform.exit();
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    };
}
