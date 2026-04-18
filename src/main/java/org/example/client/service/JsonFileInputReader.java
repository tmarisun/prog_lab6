package org.example.client.service;

import org.example.data.City;
import org.example.validate.CityValidator;

import java.util.Stack;

import static org.example.service.JsonFileLoader.loadCollection;

/**
 * Читает объект {@link City} из JSON-файла по заданному пути.
 * Валидирует данные через {@link CityValidator}, возвращает {@code null} при ошибке.
 * @see JsonFileLoader#loadCollection(String)
 */

public class JsonFileInputReader implements InputReader {
    private final String filePath;

    public JsonFileInputReader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public City readCity() {
        try {
            Stack<City> city = loadCollection(filePath);
            CityValidator.validateCity(city.get(0));
            return city.get(0);
        } catch (Exception e) {
            System.err.println("JSON input error: " + e.getMessage());
            return null;
        }
    }
}

