package app.model;

import java.io.File;
import java.io.Serializable;
import java.sql.Struct;
import java.util.Date;

public class messageStruct implements Serializable {
    public String type;
    public Date timestamp;
    public String source;
    public String destination;
    public String textMessage;
    public fileStruct file;

    /**
     * Initializes the message object
     * @param type
     * @param timestamp
     * @param source
     * @param destination
     * @param textMessage
     * @param file
     */
    public messageStruct(String type, Date timestamp, String source, String destination, String textMessage, fileStruct file) {
        this.type = type;
        // DIFF TYPES
        // text = String message
        // file = File message
        // connect = New member connect (place new member username in source)
        // disconnect = Other member disconnected (place disconnected member username in source)
        // establish = connect connections if 2 members have joined the server

        this.timestamp = timestamp; // NEVER NULL
        this.source= source; // NEVER NULL
        this.destination = destination;
        this.textMessage = textMessage;
        this.file = file;
    }
}
