package app.Server;

import app.model.messageStruct;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    @FXML
    private Text errorText;

    private int serverPort;

    private messageStruct[] log;

    public Map<String, ServerThread> connectionThreads;

    private ServerSocket serverSocket;

    // TODO: From user input, start server on port x
    public void startServer() throws IOException {
        try {
            // Get name, and description fields and assign to variables
            serverPort = Integer.parseInt(serverPortField.getText());

            // TODO: Start server on port x (Check if valid port number)
            serverSocket = new ServerSocket(serverPort);
            connectionThreads = new HashMap<>();

            // Setup body view

            // textFlow.setLineSpacing(20.0f);
            scrollPane.setFitToWidth(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            portDisplay.setText("Listening on port " + serverPort);

            // Change pre to postSidebar
            preSidebar.setVisible(false);
            postSidebar.setVisible(true);
            postBody.setVisible(true);

            new Thread(() -> {
                try {
                    listenConnections();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            errorText.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
            errorText.setText("Port given is either invalid or taken!");
            errorText.setVisible(true);
        }
    }

    // Listens for incoming connections
    public void listenConnections () throws IOException {
        int numOfMembers;
        while(true){
            numOfMembers = connectionThreads.size();

            while (numOfMembers < 2)
            {
                // TODO: Listen to incoming requests and accepts() them
                Socket endpoint = serverSocket.accept();

                // TODO: Create Thread, starts thread, puts thread in Dict for easy access with username as key and Thread as value
                ServerThread serverThread = new ServerThread(endpoint, this);
                serverThread.start();
            }
        }
    }

    public void addThreadToMap(String username, ServerThread thread) {
        connectionThreads.put(username, thread);
    }

    // Routing messages dynamically
    public void appendTextFlow(String msg) {
        textFlow.getChildren().add(new Text(msg + "\n\n"));
    }

    public void routeMessage(messageStruct msg) throws IOException {
        switch(msg.type){
            case "connect":
                Platform.runLater(()->{
                    String connectionString = String.format("[%s]    %s has connected to the server.", msg.timestamp, msg.source);
                    appendTextFlow(connectionString);
                    String currMembers = "";
                    for(Map.Entry<String, ServerThread> thread : connectionThreads.entrySet()) {
                        currMembers += thread.getKey() + "\n";
                        try {
                            connectionThreads.get(thread.getKey()).dosWriter.writeObject(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    };
                    activeMembers.setText(currMembers);
                });
                if(connectionThreads.size()==2)
                {
                    for(Map.Entry<String, ServerThread> thread : connectionThreads.entrySet()) {
                        for(Map.Entry<String, ServerThread> thread2 : connectionThreads.entrySet()) {
                            {
                                if(!(thread.getKey().equals(thread2.getKey()))){
                                    try {
                                        connectionThreads.get(thread.getKey()).dosWriter.writeObject(new messageStruct("establish", null, null, thread2.getKey(), null, null));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                    };
                }
                break;
            case "text": case "file":
                connectionThreads.get(msg.destination).dosWriter.writeObject(msg);
                Platform.runLater(()->{
                    appendTextFlow(String.format("[%s]    \"%s\" >>> \"%s\":    %s", msg.timestamp.toString(), msg.source, msg.destination, msg.textMessage));
                });
                break;
            case "disconnect":
                Platform.runLater(()->{
                    String connectionString = String.format("[%s]    %s has disconnected to the server.", msg.timestamp, msg.source);
                    appendTextFlow(connectionString);

                    connectionThreads.remove(msg.source);

                    String currMembers = "";
                    for(Map.Entry<String, ServerThread> thread : connectionThreads.entrySet()) {
                        currMembers += thread.getKey() + "\n";
                        try {
                            connectionThreads.get(thread.getKey()).dosWriter.writeObject(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    };
                    activeMembers.setText(currMembers);
                });
                break;
            default:
                break;
        }
    }

    public void saveLog() throws IOException {
        // TODO: Save log array to a text file
        String outputString = "";
        for (Node line : textFlow.getChildren()){
            String l = ((Text) line).getText();
            outputString += l.substring(0, l.length() - 1);
        }
        Date timeNow = new Date();
        FileWriter output = new FileWriter("src/app/logs/" + timeNow + ".txt");
        output.write(outputString);
        output.close();

        Platform.runLater(()->{
            appendTextFlow(String.format("[%s]    %s", timeNow.toString(), "LOG HAS BEEN SAVED IN LOGS FOLDER!"));
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
