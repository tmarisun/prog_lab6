package org.example.server;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.config.AppConfig;
import org.example.db.CityRepository;
import org.example.db.Database;
import org.example.db.DatabaseCreator;
import org.example.db.SchemaInitializer;
import org.example.db.UserRepository;

import java.sql.Connection;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        int port = args.length >= 1
                ? Integer.parseInt(args[0])
                : Integer.parseInt(AppConfig.get(dotenv, "SERVER_PORT", "5555"));

        DatabaseCreator.tryCreateApplicationDatabase(dotenv);

        Database database = new Database(dotenv);
        System.out.println("Подключение к " + database.getJdbcUrlForLogging() + " как " + database.getUser());
        try (Connection c = database.getConnection()) {
            new SchemaInitializer().ensureSchema(c);
        } catch (Exception e) {
            System.err.println("Ошибка PostgreSQL: " + e.getMessage());
            System.err.println("На helios: export PG_HOST=pg PG_DATABASE=studs PG_USER=$USER PG_PASSWORD=$USER");
            System.err.println("Создайте БД: createdb -h pg studs   или AUTO_CREATE_DATABASE=true при правах CREATEDB");
            throw e;
        }

        UserRepository userRepository = new UserRepository(database);
        CityRepository cityRepository = new CityRepository(database);
        ServerCollectionService service = new ServerCollectionService(cityRepository);
        service.loadFromDatabase();

        ServerCommandProcessor processor = new ServerCommandProcessor(service, userRepository);
        System.out.println("TCP-сервер слушает порт " + port + " (подключайте клиентов к хосту helios и этому порту)");
        new ServerConnectionAcceptor(port, processor).start();
    }
}
