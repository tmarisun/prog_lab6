package org.example.common.commands;

import org.example.Application;
import org.example.data.City;
import org.example.service.CityReader;
import org.example.validate.CityValidator;

import java.util.Stack;

public class InsertAt implements Command {
    private final Application app;

    public InsertAt(Application app) {
        this.app = app;
    }

    @Override
    public String getName() {
        return "insert_at";
    }

    @Override
    public String getDescription() {
        return "Insert an element at the specified position";
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: insert_at <index>");
            return;
        }

        try {
            String[] tokens = args[1].trim().split("\\s+");
            int index = Integer.parseInt(tokens[0]);
            Stack<City> stack = Application.getCityStack();

            if (index < 0 || index > stack.size()) {
                System.out.println("Index must be between 0 and " + stack.size());
                return;
            }

            City city = CityReader.readCity();
            CityValidator.validateCity(city);

            stack.add(index, city);
            System.out.println("City inserted at position " + index);

        } catch (NumberFormatException e) {
            System.out.println("Invalid index format.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}