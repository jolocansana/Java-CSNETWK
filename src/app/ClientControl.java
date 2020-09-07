package app;

import app.model.messageStruct;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.*;

import javax.annotation.Resources;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class ClientControl implements Initializable {
    // Variables for FXML elements
    @FXML
    private Pane loginPane;

    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    @FXML
    private TextField usernameField;

    @FXML
    private Group postLogin;

    @FXML
    private TextField textField; // Where user will type message

    @FXML
    private ScrollPane scrollPane; // Wrapper container for textFlow to allow scrolling

    @FXML
    private TextFlow textFlow;

    @FXML
    private Text usernameDisplay;

    private String username;
    private String serverIP;
    private String serverPort;

    // TODO: Make Stream variables

    // Connect user to server, and assign text fields to variables
    public void login() {
        // TODO: Get username, IP of server, and port to connect
        username = usernameField.getText();

        // TODO: Connect to server
        serverIP = ipField.getText();
        serverPort = portField.getText();
        System.out.println(String.format("%s:%s", serverIP, serverPort));

        // Setup view, purely styling and assignment
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Change view
        loginPane.setVisible(false);
        postLogin.setVisible(true);
    }

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
            scrollPane.vvalueProperty().bind(textFlow.heightProperty());
            textField.setText("");

            // TODO: Send msg to server with timestamp, source username, dest username, and msg string (Use messageStruct to construct message)
        }
    }

    // Access local filesystem, reads file, and prepares file for transport
    public void readFile() {
        // TODO: Open file directory, when Ok, send file to server
        System.out.println("Reading File");

        // TODO: Send file to server with timestamp, source username, dest username, and file object (Use messageStruct to construct message)
    }

    // Listens to server for any broadcast on update(new, leave) members or new message/file via DataInputStream
    public void listenServer() {
        // Types 'member' for member updates and 'message' for text/file messages
        // TODO: If type is member
            // TODO: If new member is connected: activate textField (If new user, clear textFlow first)

            // TODO: If member is disconnected: deactivate textField
        // TODO: If type is message
            // TODO: Update textFlow of dest using format timestamp source message/file
    }

    // Logs current user out
    public void logOut() {
        // TODO: On log out, disconnect from server (call disconnectServer function) and go back to login screen

        // TODO: Disconnect from server session
    }

    @FXML
    // On enter submission
    public void onEnter(ActionEvent e){
        sendMessage();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
