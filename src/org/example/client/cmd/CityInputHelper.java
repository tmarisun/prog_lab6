package org.example.client.cmd;

import org.example.data.City;
import org.example.service.CityReader;
import org.example.service.JsonFileInputReader;

public class CityInputHelper {
    public static City readCity(String filePath) throws Exception {
        if (filePath != null && !filePath.isEmpty()) {
            return new JsonFileInputReader(filePath).readCity();
        }
        return CityReader.readCity();
    }
}