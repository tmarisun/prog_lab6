package org.example.service;

import org.example.data.City;
import org.example.data.Climate;
import org.example.data.Coordinates;
import org.example.data.Government;
import org.example.data.Human;
import org.example.data.StandardOfLiving;
import org.example.validate.CityValidator;
import org.example.validate.InputValidator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class JsonFileInputReader implements InputReader {
    private final String filePath;

    public JsonFileInputReader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public City readCity() {
        try {
            List<City> list = loadAllCitiesFromJsonFile(filePath);
            if (list.isEmpty()) {
                return null;
            }
            City city = list.get(0);
            CityValidator.validateCity(city);
            return city;
        } catch (Exception e) {
            System.err.println("JSON input error: " + e.getMessage());
            return null;
        }
    }

    public static List<City> loadAllCitiesFromJsonFile(String filename) throws Exception {
        String jsonText = readFileUtf8(filename);
        if (jsonText.isEmpty()) {
            return Collections.emptyList();
        }
        if (jsonText.startsWith("[")) {
            JSONArray arr = new JSONArray(jsonText);
            List<City> out = new ArrayList<>(arr.length());
            for (int i = 0; i < arr.length(); i++) {
                out.add(parseCity(arr.getJSONObject(i)));
            }
            return out;
        }
        return Collections.singletonList(parseCity(new JSONObject(jsonText)));
    }

    private static String readFileUtf8(String filename) throws Exception {
        File file = new File(filename);
        if (!file.exists()) {
            throw new IllegalArgumentException("JSON file not found: " + filename);
        }
        if (!file.canRead()) {
            throw new IllegalArgumentException("Cannot read JSON file: " + filename);
        }
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            char[] buf = new char[1024];
            int len;
            while ((len = reader.read(buf)) != -1) {
                sb.append(buf, 0, len);
            }
        }
        return sb.toString();
    }

    private static City parseCity(JSONObject json) throws Exception {
        long idRaw = parseLongField(json, "id");
        InputValidator.validateId(idRaw);
        Long id = idRaw;
        String name = InputValidator.validateName(json.getString("name"));

        JSONObject coord = json.getJSONObject("coordinates");
        float x = (float) parseDoubleField(coord, "x");
        double y = parseDoubleField(coord, "y");
        InputValidator.validateCoordinates(x, y);
        Coordinates coordinates = new Coordinates(x, y);

        double areaRaw = parseDoubleField(json, "area");
        InputValidator.validateArea(areaRaw);
        int populationRaw = (int) parseLongField(json, "population");
        InputValidator.validatePopulation(populationRaw);

        int meters = (int) parseLongField(json, "metersAboveSeaLevel");
        Climate climate = InputValidator.validateEnum(json.optString("climate", null), Climate.class, "climate", false);
        Government government = InputValidator.validateEnum(json.optString("government", null), Government.class, "government", true);
        StandardOfLiving sol = InputValidator.validateEnum(json.optString("standardOfLiving", null), StandardOfLiving.class, "standardOfLiving", false);

        Human governor = null;
        if (json.has("governor") && !json.isNull("governor")) {
            JSONObject gov = json.getJSONObject("governor");
            Date birthday = InputValidator.validateBirthday(gov.optString("birthday", null));
            governor = new Human(birthday);
        }

        Date creationDate = new Date();
        return new City(id, name, coordinates, creationDate, areaRaw, populationRaw, meters, climate, government, sol, governor, null, null);
    }

    private static long parseLongField(JSONObject json, String key) {
        Object raw = json.get(key);
        if (raw instanceof Number) {
            return ((Number) raw).longValue();
        }
        return Long.parseLong(String.valueOf(raw));
    }

    private static double parseDoubleField(JSONObject json, String key) {
        Object raw = json.get(key);
        if (raw instanceof Number) {
            return ((Number) raw).doubleValue();
        }
        return Double.parseDouble(String.valueOf(raw));
    }
}

