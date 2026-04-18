package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    // Порт, на котором сервер будет слушать подключения
    private static final int PORT = 5555;

    public static void main(String[] args) {
        System.out.println("🚀 Запуск сервера на порту " + PORT + "...");

        // try-with-resources автоматически закроет ServerSocket при завершении или ошибке
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен. Ожидание подключений...");

            while (true) {
                // accept() блокирует поток, пока не подключится клиент
                Socket clientSocket = serverSocket.accept();

                System.out.println("📡 Клиент подключился: " + clientSocket.getRemoteSocketAddress());

                // TODO: Здесь будет создание потока для обработки клиента
            }
        } catch (IOException e) {
            System.err.println("Ошибка при запуске сервера: " + e.getMessage());
            e.printStackTrace();
        }
    }
}