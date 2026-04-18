package org.example.common.commands;

import org.example.Application;

import java.util.Date;

public class Info implements Command {

    public Info(Application app) {
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Show information about the collection";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Collection type: java.util.Stack");
        System.out.println("Initialization date: " + new Date());
        System.out.println("Number of elements: " + Application.getSize());
        System.out.println("Next auto-generated ID: " + Application.getNextId());
    }   
}