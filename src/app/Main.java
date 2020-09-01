package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Client start
        Parent client = FXMLLoader.load(getClass().getResource("ClientUI.fxml"));
        primaryStage.setTitle("Client");
        primaryStage.setScene(new Scene(client, 1024, 600));
        primaryStage.setResizable(false);
        primaryStage.show();

        // Server start
//        Stage serverStage = new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("ServerUI.fxml"));
//        serverStage.setTitle("Server");
//        serverStage.setScene(new Scene(root, 300, 275));
//        serverStage.setResizable(false);
//        serverStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
