package org.example.common.commands;

import org.example.Application;

public class Exit implements Command {

    public Exit(Application app) {
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getDescription() {
        return "Exit the program (without saving)";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Exiting without saving...");
        System.exit(0);
    }
}