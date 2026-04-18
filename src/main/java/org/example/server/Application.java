package org.example.server;

import lombok.Getter;
import org.example.common.data.City;

import java.io.*;


import java.util.Map;
import java.util.Stack;



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

    public void help(){
        System.out.println("The list of commands available to you: ");
        Map<String, Command> commands = managerCommands.getCommands();
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            Command value = entry.getValue();
            System.out.print(value.getName() + " ------- ");
            System.out.println(value.getDescription());
        }
    }


}