package org.example.server;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.db.CityRepository;
import org.example.db.Database;
import org.example.db.SchemaInitializer;
import org.example.db.UserRepository;

import java.sql.Connection;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        int port = args.length >= 1
                ? Integer.parseInt(args[0])
                : Integer.parseInt(dotenv.get("SERVER_PORT", "5555"));

        Database database = new Database(dotenv);
        try (Connection c = database.getConnection()) {
            new SchemaInitializer().ensureSchema(c);
        }

        UserRepository userRepository = new UserRepository(database);
        CityRepository cityRepository = new CityRepository(database);
        ServerCollectionService service = new ServerCollectionService(cityRepository);
        service.loadFromDatabase();

        ServerCommandProcessor processor = new ServerCommandProcessor(service, userRepository);
        new ServerConnectionAcceptor(port, processor).start();
    }
}
