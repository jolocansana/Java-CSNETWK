package app.Client;

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
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
    private Pane inputField;

    @FXML
    private ScrollPane scrollPane; // Wrapper container for textFlow to allow scrolling

    @FXML
    private TextFlow textFlow;

    @FXML
    private Text usernameDisplay;

    private String username;
    private String destUsername;
    private String serverIP;
    private String serverPort;

    // TODO: Make Stream variables
    private Socket clientEndpoint;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    // Connect user to server, and assign text fields to variables
    public void login() throws IOException, ClassNotFoundException {
        // TODO: Get username, IP of server, and port to connect
        username = usernameField.getText();
        serverIP = ipField.getText();
        serverPort = portField.getText();

        // TODO: Remove this, it is TEMP
        destUsername = "destination";

        // System.out.println(String.format("%s:%s", serverIP, serverPort));

        // TODO: Connect to server
        try {
            System.out.println("Inside");
            clientEndpoint = new Socket(serverIP, Integer.parseInt(serverPort));
            System.out.println(clientEndpoint.toString());
            writer = new ObjectOutputStream(clientEndpoint.getOutputStream());
            System.out.println("Inside");
            reader = new ObjectInputStream(clientEndpoint.getInputStream());
            System.out.println("Inside");

            Date timeNow = new Date();
            writer.writeObject(new messageStruct("connect", timeNow, username, null, null, null));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Operation done");
        }

        // Setup view, purely styling and assignment
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Change view
        loginPane.setVisible(false);
        postLogin.setVisible(true);

        new Thread(() -> {
            try {
                listenServer();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Sends message to server (text, and/or file)
    public void sendMessage() throws IOException {
        if (!(textField.getText().equals(""))){
            // Get current time, format, and make text object for time
            Date timeNow = new Date();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Text time = new Text(timeFormat.format(timeNow) + "   ");
            time.setStyle("-fx-color: grey");

            // Make text object for username
            Text user = new Text(username + "   ");
            user.setStyle("-fx-font-family: \"Lato Bold\", sans-serif;");

            // Make text object textField.getText()
            String message = textField.getText();
            Text msg = new Text(message + "\n\n");

            // Fixing style and adding to textFlow
            msg.setStyle("-fx-font-size: 16px;");
            textFlow.getChildren().addAll(time, user, msg);
            scrollPane.vvalueProperty().bind(textFlow.heightProperty());
            textField.setText("");

            // TODO: Send msg to server with timestamp, source username, dest username, and msg string (Use messageStruct to construct message)
            writer.writeObject(new messageStruct("text", timeNow, username, destUsername, message, null));
        }
    }

    // Access local filesystem, reads file, and prepares file for transport
    public void readFile() {
        // TODO: Open file directory, when Ok, send file to server
        System.out.println("Reading File");

        // TODO: Send file to server with timestamp, source username, dest username, and file object (Use messageStruct to construct message)
    }

    // Listens to server for any broadcast on update(new, leave) members or new message/file via DataInputStream
    public void listenServer() throws IOException, ClassNotFoundException {

        messageStruct obj;
        Date timeNow;
        SimpleDateFormat timeFormat;
        Text time;
        Text user;
        Text msg = null;

        while(true){
            obj = (messageStruct) reader.readObject();
            if(obj.type.equals("establish"))
            {
                if(!(destUsername.equals(obj.destination)))
                {
                    Platform.runLater(()->{
                        textFlow.getChildren().clear();
                    });
                    // TODO: Add connected string again since it was removed since establish requests comes after connect
                }

                inputField.setVisible(true);
                destUsername = obj.destination;
                Platform.runLater(()->{
                    usernameDisplay.setText(destUsername);
                    textField.setPromptText("Message " + destUsername);
                });

            } else
            {
                timeNow = obj.timestamp;
                timeFormat = new SimpleDateFormat("HH:mm");
                time = new Text(timeFormat.format(timeNow) + "   ");
                time.setStyle("-fx-color: grey");

                // Make text object for username
                user = new Text(obj.source + "   ");
                user.setStyle("-fx-font-family: \"Lato Bold\", sans-serif;");

                switch(obj.type){
                    case "text":
                        // Make text object textField.getText()
                        msg = new Text(obj.textMessage + "\n\n");
                        break;
                    case "file":
                        msg = new Text("has sent a file.\n\n");
                        break;
                    case "connect":
                        msg = new Text("has connected to the server!\n\n");
                        break;
                    case "disconnect":
                        msg = new Text("has disconnected from the server!\n\n");
                        break;
                    default:
                        System.out.println("Illegal type of package received!");
                        break;
                }
                msg.setStyle("-fx-font-size: 16px;");

                Text textMsg = msg;
                Text textUser = user;
                Text textTime = time;

                Platform.runLater(()->{
                    textFlow.getChildren().addAll(textTime, textUser, textMsg);
                });
                scrollPane.vvalueProperty().bind(textFlow.heightProperty());

                System.out.println("Successfully received!");
            }

            if(obj.type.equals("disconnect")){
                inputField.setVisible(false);
                usernameDisplay.setText("Waiting for other user");
            }
        }
    }

    // Logs current user out
    public void logOut() throws IOException {
        // TODO: On log out, disconnect from server and go back to login screen
        Date timeNow = new Date();
        // TODO: Disconnect from server session (Server kills thread)
        writer.writeObject(new messageStruct("disconnect", timeNow, username,null,null,null));
        postLogin.setVisible(false);
        loginPane.setVisible(true);
    }

    @FXML
    // On enter submission
    public void onEnter(ActionEvent e) throws IOException{
        sendMessage();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
