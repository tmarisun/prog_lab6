package org.example.commands;

import org.example.Application;
import org.example.data.City;

import java.io.*;
import org.example.service.CityReader;
import org.example.validate.CityValidator;
import org.example.validate.InputValidator;

import java.util.Stack;

public class Update implements Command {
    private final Application app;

    public Update(Application app) {
        this.app = app;
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "Update an element by its ID";
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: update <id> [city.json]");
            return;
        }

        try {
            String[] tokens = args[1].split("\\s+");
            long id = Long.parseLong(tokens[0]);
            InputValidator.validateId(id);

            Stack<City> CityStack = Application.getCityStack();
            City found = null;

            for (City city : CityStack) {
                if (city.getId() == id) {
                    found = city;
                    break;
                }
            }

            if (found == null) {
                System.out.println("City with ID " + id + " not found.");
                return;
            }

            City newCity = CityReader.readCity();
            CityValidator.validateCity(newCity);

            found.setName(newCity.getName());
            found.setCoordinates(newCity.getCoordinates());
            found.setArea(newCity.getArea());
            found.setPopulation(newCity.getPopulation());
            found.setMetersAboveSeaLevel(newCity.getMetersAboveSeaLevel());
            found.setClimate(newCity.getClimate());
            found.setGovernment(newCity.getGovernment());
            found.setStandardOfLiving(newCity.getStandardOfLiving());
            found.setGovernor(newCity.getGovernor());

            System.out.println("City updated successfully (element kept in collection, id preserved).");

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid ID format.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}