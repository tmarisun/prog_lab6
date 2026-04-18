package org.example.common.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.*;
import java.util.Date;

public record Human(@JsonFormat(pattern = "yyyy-MM-dd") Date birthday) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Human {
        if (birthday == null) {
            throw new IllegalArgumentException("Birthday cannot be null");
        }
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (birthday == null) {
            throw new InvalidObjectException("Deserialized birthday cannot be null");
        }
    }

    @Override
    public String toString() {
        return "Governor born on " + birthday;
    }
}