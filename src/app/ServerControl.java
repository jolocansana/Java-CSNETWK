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
    private TextField serverPortField;

    @FXML
    private Group postSidebar;

    @FXML
    private Text portDisplay;

    @FXML
    private Group postBody;

    private String serverPort;

    private messageStruct[] log;

    // TODO: From user input, start server on port x
    public void startServer() {
        // Get name, and description fields and assign to variables
        serverPort = serverPortField.getText();

        // TODO: Start server on port x (Check if valid port number)

        // Setup body view

        // textFlow.setLineSpacing(20.0f);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        portDisplay.setText("Listening on port " + serverPort);

        // Change pre to postSidebar
        preSidebar.setVisible(false);
        postSidebar.setVisible(true);
        postBody.setVisible(true);
    }

    // listens for incoming connections, accepts them, and creates a thread for them.
    private void listenConnections() {
        while(true){
            // TODO: Listen to incoming requests and accepts() them

            // TODO: Create Thread, starts thread, puts thread in Dict for easy access with username as key and Thread as value
        }
    }

    public void saveLog() {
        // TODO: Save log array to a text file
        System.out.println("Log saved!");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
