package tasks;

import java.io.Serializable;

public class SerializedObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String data;

    public SerializedObject(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}