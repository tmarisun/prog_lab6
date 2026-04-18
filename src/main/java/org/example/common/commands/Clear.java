package org.example.common.commands;

import org.example.Application;

public class Clear implements Command {

    public Clear(Application app) {}

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "Clear the collection";
    }

    @Override
    public void execute(String[] args) {
        Application.getCityStack().clear();
        System.out.println("Collection cleared.");
    }
}