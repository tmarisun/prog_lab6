package org.example.common.commands;

import org.example.Application;
import org.example.data.City;
import org.example.service.CityReader;
import org.example.validate.CityValidator;

import java.util.Stack;

public class AddIfMax implements Command {
    private final Application app;

    public AddIfMax(Application app) {
        this.app = app;
    }

    @Override
    public String getName() {
        return "add_if_max";
    }

    @Override
    public String getDescription() {
        return "Add an element if its value is greater than the maximum";
    }

    @Override
    public void execute(String[] args) {
        try {
            City candidate = CityReader.readCity();
            CityValidator.validateCity(candidate);
            Stack<City> stack = Application.getCityStack();

            if (stack.isEmpty() || candidate.compareTo(stack.stream().max(City::compareTo).get()) > 0) {
                stack.push(candidate);
                System.out.println("City added (ID: " + candidate.getId() + ")");
            } else {
                System.out.println("City not added - does not exceed maximum ID.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}