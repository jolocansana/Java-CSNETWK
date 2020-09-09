package app.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class ServerDriver extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Server start
        Stage serverStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ServerUI.fxml"));
        serverStage.setTitle("Server");
        serverStage.setScene(new Scene(root, 1024, 600));
        serverStage.setResizable(false);
        serverStage.show();
    };
}
