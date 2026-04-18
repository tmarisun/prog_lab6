package org.example.server.service;

import lombok.Getter;
import org.example.common.commands.Command;

import java.util.HashMap;
import java.util.Map;

/**
 * Диспетчер команд: регистрирует доступные {@link Command} и выполняет их по имени.
 * Обрабатывает неизвестные команды и ошибки выполнения без прерывания работы приложения.
 * @see Command
 * @see Application
 */

@Getter
public class ManagerCommands {
    private final Map<String, Command> commands = new HashMap<>();

    public ManagerCommands(Application app) {

        commands.put("help", new Help(app));
        commands.put("info", new Info(app));
        commands.put("show", new Show(app));
        commands.put("add", new Add(app));
        commands.put("update", new Update(app));
        commands.put("remove_by_id", new RemoveById(app));
        commands.put("clear", new Clear(app));
        commands.put("save", new Save(app));
        commands.put("execute_script", new ExecuteScript(app, this));
        commands.put("exit", new Exit(app));
        commands.put("insert_at", new InsertAt(app));
        commands.put("add_if_max", new AddIfMax(app));
        commands.put("sort", new Sort(app));
        commands.put("count_less_than_standard_of_living", new CountLessThanStandardOfLiving(app));
        commands.put("filter_by_governor", new FilterByGovernor(app));
        commands.put("print_field_ascending_standard_of_living", new PrintFieldAscendingStandardOfLiving(app));
    }



    public void callCommand(String[] commandParts) {
        if (commandParts.length == 0) {
            return;
        }

        String commandName = commandParts[0].toLowerCase();

        Command command = commands.get(commandName);
        if (command != null) {
            try {
                command.execute();
            } catch (Exception e) {
                System.out.println("Error when executing the command: " + e.getMessage());
            }
        } else {
            System.out.println("Unidentified command: " + commandName);
            System.out.println("Enter 'help' for reference.");
        }
    }


}