package org.example.client;

import java.io.InputStreamReader;

public class ClientApp {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5555;

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : DEFAULT_HOST;
        int port = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PORT;

        Sender sender = new Sender(host, port);
        ClientConsole console = new ClientConsole(sender);

        System.out.println("Подключено к " + host + ":" + port);
        System.out.println("Введите 'help' для списка команд. 'exit' для выхода.\n");

        // Запуск с чтением из System.in, fileFlag = false (интерактивный режим)
        console.runApp(new InputStreamReader(System.in), false);

        sender.close();
    }
}