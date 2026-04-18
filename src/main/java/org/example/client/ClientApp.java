package org.example.client;

import org.example.common.network.Request;
import org.example.common.network.Response;
import java.io.*;
import java.net.Socket;

public class ClientApp {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5555;

    public static void main(String[] args) {
        System.out.println("Подключение к серверу " + SERVER_HOST + ":" + SERVER_PORT + "...");

        // try-with-resources закроет сокет и потоки при выходе
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            System.out.println("Соединение установлено!");

            // 1. Сначала создаём OutputStream, потом InputStream (важно!)
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // 2. Формируем тестовый запрос
            Request testRequest = new Request("show", new String[0], null);
            System.out.println("Отправка запроса: " + testRequest);

            // 3. Отправляем
            out.writeObject(testRequest);
            out.flush();

            // 4. Ждём и читаем ответ (блокирует поток, пока сервер не ответит)
            System.out.println("Ожидание ответа...");
            Response response = (Response) in.readObject();

            // 5. Вывод результата
            System.out.println("Получен ответ: " + response);
            System.out.println("Статус: " + (response.isSuccess() ? "УСПЕХ" : "ОШИБКА"));
            System.out.println("Сообщение: " + response.getMessage());

            if (response.getData() != null) {
                System.out.println("Данные: " + response.getData());
            }

        } catch (IOException e) {
            System.err.println("Ошибка сети: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Ошибка десериализации: класс не найден на сервере");
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}