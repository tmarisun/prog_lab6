package org.example;

import lombok.Getter;
import java.io.*;
import org.example.data.City;
import org.example.manager.ManagerCommands;
import org.example.service.JsonFileLoader;
import org.example.validate.CityValidator;


import java.util.Stack;

import static org.example.validate.InputValidator.validateUniqueIds;


public class Application {

    @Getter
    private static Stack<City> cityStack;
    @Getter
    private String fileName;
    @Getter
    private ManagerCommands managerCommands;


    public Application(String filename) throws IOException, NumberFormatException {
        this.fileName = filename;
        cityStack = JsonFileLoader.loadCollection(filename);

        for (City city : cityStack) {
            CityValidator.validateCity(city);
        }

        validateUniqueIds(cityStack);
        //mergeScriptCityFileIfPresent();
        this.managerCommands = new ManagerCommands(this);
    }


    public static long getSize() {
        return cityStack.size();
    }


    public static long getNextId() {
        long maxId = 0;
        for (City city : cityStack) {
            if (city.getId() > maxId) {
                maxId = city.getId();
            }
        }
        return maxId + 1;
    }

    public void addCity(City city) {
        cityStack.push(city);
    }

    public void help() {
        HelpFormatter.printStandalone(managerCommands.getCommands());
    }


}