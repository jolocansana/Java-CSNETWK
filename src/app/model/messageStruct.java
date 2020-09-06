package app.model;

import java.io.File;
import java.util.Date;

public class messageStruct {
    public Date timestamp;
    public String source;
    public String destination;
    public String textMessage;
    public File file;

    public messageStruct(Date timestamp, String source, String destination, String textMessage, File file) {
        this.timestamp = timestamp;
        this.source= source;
        this.destination = destination;
        this.textMessage = textMessage;
        this.file = file;
    }
}
