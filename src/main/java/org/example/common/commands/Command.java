package org.example.common.commands;

import org.example.common.CollectionManager;
import org.example.common.network.Response;

public interface Command {
    /**
     * Выполняет команду.
     * @param args аргументы из запроса клиента
     * @param payload объект из запроса (например, City для add)
     * @param manager доступ к коллекции
     * @return ответ для отправки клиенту
     */
    Response execute(String[] args, Object payload, CollectionManager manager);

    String getName();
    String getDescription();
}