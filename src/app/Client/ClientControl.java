package app.Client;

import app.model.fileStruct;
import app.model.messageStruct;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @FXML
    private Text errorText;

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
            clientEndpoint = new Socket(serverIP, Integer.parseInt(serverPort));
            writer = new ObjectOutputStream(clientEndpoint.getOutputStream());
            reader = new ObjectInputStream(clientEndpoint.getInputStream());

            Date timeNow = new Date();
            writer.writeObject(new messageStruct("connect", timeNow, username, null, null, null));

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

            System.out.println("Connection done");
            errorText.setVisible(false);

        } catch (Exception e) {
            e.printStackTrace();
            errorText.setText("Server IP or Port is invalid!");
            errorText.setVisible(true);
        }
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
    public void readFile() throws IOException {
        // TODO: Open file directory, when Ok, send file to server
        FileChooser fc = new FileChooser();
        Stage fcStage = new Stage();
        File f = fc.showOpenDialog(fcStage);

        // Append to textFlow

        Date timeNow = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Text time = new Text(timeFormat.format(timeNow) + "   ");
        time.setStyle("-fx-color: grey");

        // Make text object for username
        Text user = new Text(username + "   ");
        user.setStyle("-fx-font-family: \"Lato Bold\", sans-serif;");

        // Make text object textField.getText()
        String message = "sent file " + f.getName();
        Text msg = new Text(message + "\n\n");

        // Fixing style and adding to textFlow
        msg.setStyle("-fx-font-size: 16px;");
        textFlow.getChildren().addAll(time, user, msg);
        scrollPane.vvalueProperty().bind(textFlow.heightProperty());
        textField.setText("");

        // TODO: Send file to server with timestamp, source username, dest username, and file object (Use messageStruct to construct message)
        writer.writeObject(new messageStruct("file", timeNow, username, destUsername, message, new fileStruct(f.getName(), Files.readAllBytes(f.toPath()))));
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

            }
            else if(obj.type.equals("invalid"))
            {
                postLogin.setVisible(false);
                loginPane.setVisible(true);
                errorText.setText(obj.textMessage);
                errorText.setVisible(true);
            }
            else
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
                        msg = new Text(obj.textMessage + "\n\n");
                        FileChooser fc = new FileChooser();
                        messageStruct finalObj = obj;
                        Platform.runLater(()->{
                            Stage fcStage = new Stage();
                            fc.setInitialFileName(finalObj.file.name);
                            File f = fc.showSaveDialog(fcStage);
                            Path path = Paths.get(f.getPath());
                            try {
                                Files.write(path, finalObj.file.data);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
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
