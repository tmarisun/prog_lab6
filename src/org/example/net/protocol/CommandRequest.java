package org.example.net.protocol;

import org.example.data.City;

import java.io.Serializable;
@lombok.Getter
@lombok.Setter
public class CommandRequest implements Serializable {
    private CommandType type;
    private String arg;
    private Long id;
    private Integer index;
    private City city;

    /** Логин пользователя (передаётся с каждым запросом). */
    private String login;
    /** Пароль в открытом виде по сети (как требует задание). */
    private String password;

    /** Устанавливается на сервере после успешной аутентификации; не сериализуется клиенту обратно как часть ответа. */
    private transient Long authenticatedUserId;

}

