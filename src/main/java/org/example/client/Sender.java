package org.example.client;

import org.example.common.network.Request;
import org.example.common.network.Response;

import java.io.*;
import java.net.Socket;

/**
 * Отвечает за сетевое взаимодействие: отправка Request и получение Response.
 */
public class Sender {

    private final String host;
    private final int port;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Sender(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Устанавливает соединение с сервером (если ещё не установлено).
     */
    private void ensureConnected() throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket(host, port);
            // ⚠️ Порядок: сначала out, потом in!
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush(); // отправляем заголовок сериализации
            in = new ObjectInputStream(socket.getInputStream());
        }
    }

    /**
     * Отправляет запрос и получает ответ.
     * @return Response от сервера или null при ошибке
     */
    public Response sendRequest(Request request) {
        try {
            ensureConnected();

            out.writeObject(request);
            out.flush();

            return (Response) in.readObject();

        } catch (IOException e) {
            System.err.println("🔌 Ошибка соединения: " + e.getMessage());
            close(); // закрываем, чтобы при следующем запросе переподключиться
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("Ошибка десериализации ответа");
            return null;
        }
    }

    /**
     * Закрывает соединение.
     */
    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
    }
}