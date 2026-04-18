package org.example.client.service;

import org.example.data.City;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Stack;
import java.util.TimeZone;

/**
 * Сохраняет коллекцию {@link City} в JSON-файл в кодировке UTF-8.
 * Даты форматируются как {@code yyyy-MM-dd'T'HH:mm:ss}, birthday — {@code yyyy-MM-dd}.
 * @see JsonFileLoader
 */
public class JsonFileSaver {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat BIRTHDAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getDefault());
    }


    public static void saveCitiesToFile(Stack<City> cities, String filename) throws IOException {
        JSONArray jsonArray = new JSONArray();

        for (City city : cities) {
            jsonArray.put(cityToJson(city));
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"))) {
            writer.write(jsonArray.toString(2));
            writer.newLine();
        }
    }

    private static JSONObject cityToJson(City city) {
        JSONObject json = new JSONObject();

        json.put("id", city.getId());
        json.put("name", city.getName());
        json.put("creationDate", DATE_FORMAT.format(city.getCreationDate()));
        json.put("area", city.getArea());
        json.put("population", city.getPopulation());
        json.put("metersAboveSeaLevel", city.getMetersAboveSeaLevel());
        json.put("government", city.getGovernment().name());

        JSONObject coordJson = new JSONObject();
        coordJson.put("x", city.getCoordinates().getX());
        coordJson.put("y", city.getCoordinates().getY());
        json.put("coordinates", coordJson);

        if (city.getClimate() != null) {
            json.put("climate", city.getClimate().name());
        }
        if (city.getStandardOfLiving() != null) {
            json.put("standardOfLiving", city.getStandardOfLiving().name());
        }

        if (city.getGovernor() != null) {
            JSONObject govJson = new JSONObject();
            govJson.put("birthday", BIRTHDAY_FORMAT.format(city.getGovernor().birthday()));
            json.put("governor", govJson);
        }

        return json;
    }
}