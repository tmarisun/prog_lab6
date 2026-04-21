package org.example.commands;

import org.example.Application;
import org.example.manager.ManagerCommands;
import org.example.service.CityReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExecuteScript implements Command {
    private final Application app;
    private final ManagerCommands managerCommands;

    private static final List<String> executingScripts = new ArrayList<>();

    public ExecuteScript(Application app, ManagerCommands managerCommands) {
        this.app = app;
        this.managerCommands = managerCommands;
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String getDescription() {
        return "Execute a script from a file";
    }

    @Override
    public void execute(String[] args) throws FileNotFoundException {
        if (args.length < 2) {
            System.out.println("Usage: execute_script <file_name>");
            return;
        }

        String fileName = args[1];
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("File not found: " + fileName);
            return;
        }

        if (!file.canRead()) {
            System.out.println("Cannot read file: " + fileName);
            return;
        }

        String absolutePath = file.getAbsolutePath();

        if (executingScripts.contains(absolutePath)) {
            System.out.println("Recursion detected! Script cannot call itself: " + fileName);
            return;
        }

        executingScripts.add(absolutePath);

        Scanner previousScanner = CityReader.scanner;
        boolean quiet = CityReader.isScriptMode();
        CityReader.enterScript();

        try (Scanner fileScanner = new Scanner(file)) {
            CityReader.setScanner(fileScanner);
            if (!quiet) {
                System.out.println("Executing script: " + fileName);
            }

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }

                if (!quiet) {
                    System.out.println("$ " + line);
                }
                String[] commandParts = line.split("\\s+", 2);
                managerCommands.callCommand(commandParts);
            }

            if (!quiet) {
                System.out.println("Script execution completed: " + fileName);
            }
        } catch (Exception e) {
            System.out.println("Error executing script: " + e.getMessage());
        } finally {
            CityReader.leaveScript();
            CityReader.setScanner(previousScanner);
            executingScripts.remove(absolutePath);
        }
    }
}