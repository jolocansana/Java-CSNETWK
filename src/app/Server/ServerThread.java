package app.Server;

import app.model.messageStruct;
import javafx.scene.text.Text;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {

    public Socket serverEndpoint;
    public ObjectInputStream disReader;
    public ObjectOutputStream dosWriter;
    public ServerControl source;
    public String threadID;

    public ServerThread (Socket endpoint, ServerControl source) throws IOException {
        serverEndpoint = endpoint;
        this.source = source;
    }

    @Override
    public void run() {
        // TODO: Read on DataInputStream
        try {
            dosWriter = new ObjectOutputStream(serverEndpoint.getOutputStream());
            disReader = new ObjectInputStream(serverEndpoint.getInputStream());
            System.out.println("Done establish");

            while(true){
                messageStruct incomingMsg = ((messageStruct) disReader.readObject());
                if(incomingMsg.type.equals("connect")) {
                    threadID = incomingMsg.source;
                    source.addThreadToMap(incomingMsg.source, this);
                }
                source.routeMessage(incomingMsg);
                if(incomingMsg.type.equals("disconnect")) {
                    this.stop();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
