package org.example.common.commands;

import org.example.Application;
import org.example.data.City;

public class Show implements Command {

    public Show(Application app) {
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription() {
        return "Show all elements of the collection";
    }

    @Override
    public void execute(String[] args) {
        if (Application.getCityStack().isEmpty()) {
            System.out.println("Collection is empty.");
            return;
        }
        for (City city : Application.getCityStack()) {
            System.out.println(city);
        }
    }
}