package org.example.server;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: ServerMain <collection-file> [port]");
            return;
        }
        String file = args[0];
        int port = 5555;

        ServerCollectionService service = new ServerCollectionService(file);
        ServerCommandProcessor processor = new ServerCommandProcessor(service);
        new ServerConnectionAcceptor(port, processor).start();
    }
}

