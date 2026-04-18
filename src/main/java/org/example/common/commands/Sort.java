package org.example.common.commands;

import org.example.Application;

import java.util.Collections;

public class Sort implements Command {

    public Sort(Application app) {
    }

    @Override
    public String getName() {
        return "sort";
    }

    @Override
    public String getDescription() {
        return "Sort the collection in natural order";
    }

    @Override
    public void execute(String[] args) {
        Collections.sort(Application.getCityStack());
        System.out.println("Collection sorted by ID (natural order).");
    }
}