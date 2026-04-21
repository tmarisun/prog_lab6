package org.example;

import lombok.Getter;
import org.example.manager.ManagerCommands;
import org.example.service.CityReader;

import java.util.Scanner;
import java.util.NoSuchElementException;


@Getter
public class Scene {
    private final Application app;
    private static ManagerCommands manager;
    private final Scanner scanner;

    public  Scene(Application app) {
        this.app = app;
        manager = app.getManagerCommands();
        this.scanner = new Scanner(System.in);
    }


    public void run() {
        System.out.println("The program is running. Enter 'help' for help.");
        while (true) {
            System.out.print("> ");
            System.out.flush();
            String line;
            try {
                line = CityReader.scanner.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println("\nInput stream was closed. Exiting program.");
                break;
            }
            if (line.length() == 0) continue;

            String[] parts = line.split("\\s+", 2);
            manager.callCommand(parts);

            if (parts[0].equalsIgnoreCase("exit")) break;
        }
    }

}