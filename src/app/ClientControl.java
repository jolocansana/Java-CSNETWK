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
    private Text bodyHeaderContent;

    @FXML
    private Text usernameDisplay;

    @FXML
    private VBox directMessages;

    private Hashtable<String, TextFlow> textFlowDict;
    private TextFlow activeTextFlow;
    private String username;
    private String serverName;
    private String serverDesc;
    private String serverIP;
    private String serverPort;
    private boolean isAlone = true;

    // Connect user to server, and assign text fields to variables
    public void login() {
        // TODO: Get username, IP of server, and port to connect
        username = usernameField.getText();

        // TODO: Connect to server
        serverIP = ipField.getText();
        serverPort = portField.getText();
        System.out.println(String.format("%s:%s", serverIP, serverPort));

        // TODO: Get data from server (serverName, serverDesc, msgs)
        serverName = "notion-test"; // TEMP VALUES
        serverDesc = "This is the very beginning of the #notion-test channel."; // TEMP VALUES

        // Setup view, purely styling and assignment
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

        activeTextFlow.getChildren().addAll(serverNameText, spacing, serverDescription, divider, new Text("\n\n"));

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
            activeTextFlow.getChildren().addAll(time, user, msg);
            scrollPane.vvalueProperty().bind(activeTextFlow.heightProperty());
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

    // Logs current user out
    public void logOut() {
        // TODO: On log out, disconnect from server (call disconnectServer function) and go back to login screen

        // TODO: Disconnect from server session
    }

    // Listens to server for any broadcast on update(new, leave) members or new message/file
    public void listenServer() {
        // Types 'member' for member updates and 'message' for text/file messages
        // TODO: If type is member
            // TODO: If new member is connected: add new button, text flow for the user (1)

            // TODO: If member is disconnected: gray out member name and make non-clickable
        // TODO: If type is message
            // TODO: Update textFlow of dest using format timestamp source message/file
    }

    @FXML
    // Change chat display on username click
    public void changeCurrentChat(MouseEvent e, String username){

        // Change usernameDisplay to e.source's username
        usernameDisplay.setText(username);

        // Change text display to username
        scrollPane.setContent((Node) textFlowDict.get(username));
        activeTextFlow = (TextFlow) textFlowDict.get(username);

        textField.setPromptText("Message " + username);
    }

    @FXML
    // On enter submission
    public void onEnter(ActionEvent e){
        sendMessage();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: MOVE TO LOGIN AFTER FULL IMPLEMENTATION
        directMessages.setSpacing(10);

        // TODO: MOVE ALL BOTTOM TO (1)
        // Adds button for user
        username = "testUser";
        Button button1 = new Button("johndoe");
        button1.setMaxWidth(1000);
        button1.setStyle("-fx-border: 0; -fx-background-color: white; -fx-alignment: top-left;");
        button1.setOnMouseEntered(e -> button1.setStyle("-fx-border: 0; -fx-background-color: grey; -fx-alignment: top-left;"));
        button1.setOnMouseExited(e -> button1.setStyle("-fx-border: 0; -fx-background-color: white; -fx-alignment: top-left;"));
        button1.setOnMousePressed(e -> button1.setStyle("-fx-border: 0; -fx-background-color: orange; -fx-alignment: top-left;"));
        button1.setOnMouseReleased(e -> button1.setStyle("-fx-border: 0; -fx-background-color: grey; -fx-alignment: top-left;"));
        button1.setOnMouseClicked(e -> changeCurrentChat(e, ((Button) (e.getSource())).getText()));

        Button button2 = new Button("jilldoe");
        button2.setMaxWidth(1000);
        button2.setStyle("-fx-border: 0; -fx-background-color: white; -fx-alignment: top-left;");
        button2.setOnMouseEntered(e -> button2.setStyle("-fx-border: 0; -fx-background-color: grey; -fx-alignment: top-left;"));
        button2.setOnMouseExited(e -> button2.setStyle("-fx-border: 0; -fx-background-color: white; -fx-alignment: top-left;"));
        button2.setOnMousePressed(e -> button2.setStyle("-fx-border: 0; -fx-background-color: orange; -fx-alignment: top-left;"));
        button2.setOnMouseReleased(e -> button2.setStyle("-fx-border: 0; -fx-background-color: grey; -fx-alignment: top-left;"));
        button2.setOnMouseClicked(e -> changeCurrentChat(e, ((Button) (e.getSource())).getText()));

        directMessages.getChildren().addAll(button1, button2);

        // Add test text flow
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: white;");
        scrollPane.setVvalue(1.0);

        textFlowDict = new Hashtable<String, TextFlow>();

        TextFlow testTextFlow = new TextFlow();
        String key = "johndoe";
        testTextFlow.setPrefWidth(650);
        textFlowDict.put(key, testTextFlow);

        TextFlow testTextFlow2 = new TextFlow();
        String key2 = "jilldoe";
        testTextFlow2.setPrefWidth(650);
        textFlowDict.put(key2, testTextFlow2);
    }
}
