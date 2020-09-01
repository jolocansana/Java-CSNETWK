package app;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.*;

import javax.annotation.Resources;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ClientControl implements Initializable {
    // Variables for FXML elements
    @FXML
    private TextFlow textFlow; // Where messages are going to be displayed

    @FXML
    private TextField textField; // Where user will type message

    @FXML
    private ScrollPane scrollPane; // Wrapper container for textFlow to allow scrolling

    @FXML
    private Text bodyHeaderContent;

    private String username;
    private String serverName;
    private String serverDesc;

    // Sends message to server (text, and/or file)
    public void sendMessage() {
        if (!(textField.getText().equals(""))){
            // Get current time, format, and make text object for time
            LocalTime timeNow = LocalTime.now();
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
            Text time = new Text(timeNow.format(timeFormat)  + "   ");
            time.setStyle("-fx-color: grey");

            // Make text object for username
            Text user = new Text(username + "   ");
            user.setStyle("-fx-font-family: \"Lato Bold\", sans-serif;");

            // Make text object textField.getText()
            Text msg = new Text(textField.getText() + "\n\n");

            // Fixing style and adding to textFlow
            msg.setStyle("-fx-font-size: 16px;");
            textFlow.getChildren().addAll(time, user, msg);
            scrollPane.setVvalue(scrollPane.getVmax());
            textField.setText("");
        }
    }

    // Access local filesystem, reads file, and prepares file for transport
    public void readFile() {
        System.out.println("Reading File");
    }

    @FXML
    // On enter submission
    public void onEnter(ActionEvent e){
        sendMessage();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // TODO: Get username, IP of server, and port to connect
        username = "jolocansana";

        // TODO: Connect to server and get needed data (serverName, serverDesc, etc)
        serverName = "notion-test";
        serverDesc = "This is the very beginning of the #notion-test channel.";


        // Setup view
        bodyHeaderContent.setText("# " + serverName);
        textField.setPromptText("Message #" + serverName);

        Text serverNameText = new Text("\n\n#" + serverName + "\n");
        serverNameText.setStyle("-fx-font-family: \"Lato Bold\", sans-serif; -fx-font-size: 28;");

        Text spacing = new Text("\n");
        spacing.setStyle("-fx-font-size: 10;");

        Text serverDescription = new Text(serverDesc + "\n\n");
        serverDescription.setStyle("-fx-font-size: 18;");

        Line divider = new Line(0, 0, 600, 0);
        divider.setStyle("-fx-stroke: grey;");

        textFlow.getChildren().addAll(serverNameText, spacing, serverDescription, divider, new Text("\n\n"));

        // textFlow.setLineSpacing(20.0f);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
}
