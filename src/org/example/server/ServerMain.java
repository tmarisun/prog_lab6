package org.example.server;

import io.github.cdimascio.dotenv.Dotenv;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: ServerMain <collection-file> [port]");
            return;
        }
        Dotenv dotenv = Dotenv.configure().load();
        int port = Integer.parseInt(dotenv.get("SERVER_PORT", "5555"));
        String file = args.length > 0 ? args[0] : dotenv.get("COLLECTION_FILE", "cities.json");


        ServerCollectionService service = new ServerCollectionService(file);
        ServerCommandProcessor processor = new ServerCommandProcessor(service);
        new ServerConnectionAcceptor(port, processor).start();
    }
}

