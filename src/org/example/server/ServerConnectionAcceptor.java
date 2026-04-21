package org.example.server;

import org.example.net.protocol.CommandRequest;
import org.example.net.protocol.CommandResponse;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ServerConnectionAcceptor {
    private final int port;
    private final ServerRequestReader requestReader = new ServerRequestReader();
    private final ServerResponseSender responseSender = new ServerResponseSender();
    private final ServerCommandProcessor processor;

    public ServerConnectionAcceptor(int port, ServerCommandProcessor processor) {
        this.port = port;
        this.processor = processor;
    }

    public void start() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Server started on port " + port);
            System.out.println("Server console command available: save");

            while (true) {
                processServerConsoleInput(consoleReader);
                try (Socket socket = serverSocket.accept()) {
                    CommandRequest request = requestReader.read(socket.getInputStream());
                    CommandResponse response = processor.process(request);
                    responseSender.send(socket.getOutputStream(), response);
                }
                catch (EOFException | SocketException e) {
                        // Клиент просто закрыл программу. Это норма, не ошибка.
                        System.out.println("Client disconnected.");
                    }
                catch (SocketTimeoutException  ignored) {
                    System.out.println("Client disconnected.");
                }

                catch (Exception e) {
                    System.out.println("Request error: " + e.getMessage());
                }
            }
        }
    }

    private void processServerConsoleInput(BufferedReader consoleReader) {
        try {
            while (consoleReader.ready()) {
                String line = consoleReader.readLine();
                CommandResponse response = processor.processServerConsoleCommand(line);
                if (response != null) {
                    System.out.println("[SERVER] " + response.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Server console error: " + e.getMessage());
        }
    }
}

