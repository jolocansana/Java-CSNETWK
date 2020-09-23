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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private Text ipDisplay;

    @FXML
    private Group postBody;

    @FXML
    private Text errorText;

    private int serverPort;

    private messageStruct[] log;

    public Map<String, ServerThread> connectionThreads;

    private ServerSocket serverSocket;

    /**
     * Starts the server instance on serverPort
     * @throws IOException
     */
    public void startServer() throws IOException {
        try {
            // Get name, and description fields and assign to variables
            serverPort = Integer.parseInt(serverPortField.getText());

            // TODO: Start server on port x (Check if valid port number)
            serverSocket = new ServerSocket(serverPort);
            connectionThreads = new HashMap<>();

            // Setup body view

            scrollPane.setFitToWidth(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            String serverIP = serverSocket.getInetAddress().getHostAddress();

            ipDisplay.setText("IP: " + serverIP);
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

    /**
     * Listens for incoming client connections
     * @throws IOException
     */
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

    /**
     * Add thread to the thread map with username as key for reference in routing
     * @param username
     * @param thread
     */
    public void addThreadToMap(String username, ServerThread thread) {
        connectionThreads.put(username, thread);
    }

    /**
     * Append the received message from clients to the server display
     * @param msg
     */
    public void appendTextFlow(String msg) {
        textFlow.getChildren().add(new Text(msg + "\n\n"));
    }

    /**
     * Route the incoming message from source client to destination client
     * @param msg
     * @throws IOException
     */
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

    /**
     * Save the server display log on a text file
     * @throws IOException
     */
    public void saveLog() throws IOException {
        // TODO: Save log array to a text file
        String outputString = "";
        for (Node line : textFlow.getChildren()){
            String l = ((Text) line).getText();
            outputString += l.substring(0, l.length() - 1);
        }
        Date timeNow = new Date();

        // Choose the text file to save the log string. If file doesn't exist, it creates a new one.
        FileChooser fc = new FileChooser();
        String finalOutputString = outputString;
        Platform.runLater(()->{
            Stage fcStage = new Stage();
            fc.setInitialFileName(timeNow.toString() + ".txt");
            File f = fc.showSaveDialog(fcStage);
            Path path = Paths.get(f.getPath());
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(String.valueOf(path), false)).printf("%s", finalOutputString);
                pw.close();
                System.out.println(finalOutputString);
                System.out.println(String.valueOf(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Platform.runLater(()->{
            appendTextFlow(String.format("[%s]    %s", timeNow.toString(), "LOG HAS BEEN SAVED!"));
        });
    }

    /**
     * Class inherited from implemented class
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
