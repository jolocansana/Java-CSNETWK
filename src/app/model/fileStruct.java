package app.model;

import java.io.Serializable;

public class fileStruct implements Serializable {
    public String name;
    public byte[] data;

    public fileStruct(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }
}