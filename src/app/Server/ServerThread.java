package app.Server;

import app.model.messageStruct;
import javafx.scene.text.Text;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ServerThread extends Thread {

    public Socket serverEndpoint;
    public ObjectInputStream disReader;
    public ObjectOutputStream dosWriter;
    public ServerControl source;
    public String threadID;

    /**
     * Initialize the ServerThread object
     * @param endpoint
     * @param source
     * @throws IOException
     */
    public ServerThread (Socket endpoint, ServerControl source) throws IOException {
        serverEndpoint = endpoint;
        this.source = source;
    }

    /**
     * Actual running instance of the thread. Listens to messages from client endpoint.
     */
    @Override
    public void run() {
        // Read on DataInputStream
        try {
            dosWriter = new ObjectOutputStream(serverEndpoint.getOutputStream());
            disReader = new ObjectInputStream(serverEndpoint.getInputStream());
            System.out.println("Done establish");

            while(true){
                messageStruct incomingMsg = ((messageStruct) disReader.readObject());
                if(incomingMsg.type.equals("connect")) {
                    boolean flag = false;
                    for(Map.Entry<String, ServerThread> thread : source.connectionThreads.entrySet()) {
                        flag = thread.getKey().equals(incomingMsg.source);
                        if (flag) break;
                    };

                    if(flag) {
                        dosWriter.writeObject(new messageStruct("invalid", null, null, null, "Username is taken!", null));
                        this.stop();
                    } else {
                        threadID = incomingMsg.source;
                        source.addThreadToMap(incomingMsg.source, this);
                        source.routeMessage(incomingMsg);
                    }
                }
                else if(incomingMsg.type.equals("disconnect")) {
                    source.routeMessage(incomingMsg);
                    this.stop();
                } else {
                    source.routeMessage(incomingMsg);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
