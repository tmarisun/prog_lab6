package org.example.common.network;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Response implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private Object data;

    public static Response error(String message) {
        return new Response(false, message, null);
    }


}