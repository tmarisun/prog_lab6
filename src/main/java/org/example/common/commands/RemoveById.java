package org.example.common.commands;

import org.example.Application;
import org.example.data.City;
import org.example.validate.InputValidator;

import java.time.format.DateTimeParseException;
import java.util.Stack;

public class RemoveById implements Command {
    private final Application app;

    public RemoveById(Application app) {
        this.app = app;
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String getDescription() {
        return "Remove an element by its ID";
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: remove_by_id <id>");
            return;
        }

        try {
            long id = Long.parseLong(args[1]);
            InputValidator.validateId(id);

            Stack<City> stack = Application.getCityStack();
            City toRemove = null;

            for (City city : stack) {
                if (city.getId() == id) {
                    toRemove = city;
                    break;
                }
            }

            if (toRemove == null) {
                System.out.println("City with ID " + id + " not found.");
                return;
            }

            stack.remove(toRemove);
            System.out.println("City removed successfully.");

        } catch (NumberFormatException | DateTimeParseException e) {
            System.out.println("Invalid ID format.");
        }
    }
}