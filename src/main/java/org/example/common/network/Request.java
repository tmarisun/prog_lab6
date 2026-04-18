package org.example.common.network;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String commandName;
    private String[] arguments;
    private Object payload;


}