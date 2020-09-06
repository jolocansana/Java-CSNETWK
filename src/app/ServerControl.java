package app;

import app.model.messageStruct;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerControl implements Initializable {

    @FXML
    private TextFlow textFlow;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextArea activeMembers;

    @FXML
    private Group preSidebar;

    @FXML
    private TextField serverNameField;

    @FXML
    private Text serverNameDisplay;

    @FXML
    private TextArea serverDescriptionField;

    @FXML
    private Text serverDescDisplay;

    @FXML
    private TextField serverPortField;

    @FXML
    private Group postSidebar;

    @FXML
    private Text portDisplay;

    @FXML
    private Group postBody;

    private String serverName;
    private String serverDesc;
    private String serverPort;

    private messageStruct[] log;

    // TODO: From user input, start server on port x
    public void startServer() {
        // Get name, and description fields and assign to variables
        serverName = serverNameField.getText();
        serverDesc = serverDescriptionField.getText();
        serverPort = serverPortField.getText();

        // TODO: Start server on port x (Check if valid port number)

        // Setup body view
        serverNameDisplay.setText("#" + serverName);
        serverDescDisplay.setText(serverDesc);

        Text serverNameText = new Text("#" + serverName + "\n");
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

        portDisplay.setText("Listening on port " + serverPort);

        // Change pre to postSidebar
        preSidebar.setVisible(false);
        postSidebar.setVisible(true);
        postBody.setVisible(true);
    }

    // Accept incoming requests (messages, files)
    public void acceptRequests() {
        // TODO: Listen to incoming requests from connected clients

        // TODO: Append messageStruct object to log array

        // TODO: Update log string and display on server display
        // LOG FORMAT: timestamp source -> dest: msg/filename

        // TODO: Send user input to the destination user
    }

    // Listens and accepts incoming connection request
    public void acceptConnections() {
        // TODO: Get username of incoming connection, accept connection if username is not taken

        // TODO: Log new connection to log

        // TODO: Transmit to all clients connected about new connect client
    }

    // Listens and accepts incoming disconnect request
    public void acceptDisconnect() {
        // TODO: Get username of incoming disconnection

        // TODO: Log disconnection to log

        // TODO: Transmit to all clients connected about disconnected client
    }

    public void saveLog() {
        // TODO: Save log array to a text file
        System.out.println("Log saved!");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
