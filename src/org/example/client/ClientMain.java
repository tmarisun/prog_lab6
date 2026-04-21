package org.example.client;

import org.example.data.City;
import org.example.net.protocol.CommandRequest;
import org.example.net.protocol.CommandResponse;
import org.example.net.protocol.CommandType;
import org.example.service.CityReader;

import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        CityReader.setScanner(scanner);

        ClientNetworkChannel network = new ClientNetworkChannel();
        ClientCommandParser parser = new ClientCommandParser();

        System.out.println("Client started. Enter command:");
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line.length() == 0) continue;

            CommandRequest req = parser.parse(line);
            if (req == null) {
                String parseError = parser.getLastError();
                if (parseError == null || parseError.length() == 0) {
                    System.out.println("Unknown command or invalid format");
                } else {
                    System.out.println("Input error: " + parseError);
                }
                continue;
            }

            if (req.getType() == CommandType.EXIT) {
                System.out.println("Client finished.");
                break;
            }

            CommandResponse resp = network.send(req);
            printResponse(resp);
        }
    }

    private static void printResponse(CommandResponse resp) {
        System.out.println();
        System.out.println(resp.getMessage());
        for (City city : resp.getCities()) {
            System.out.println(city);
        }
        System.out.println();
    }
}

