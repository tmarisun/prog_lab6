package org.example.common.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.data.City;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Stack;

/**
 * Утилитный класс для загрузки коллекции {@link City} из JSON-файла.
 * Читает файл в кодировке UTF-8, использует Jackson с поддержкой {@code java.time}.
 * @see City
 */
public class JsonFileLoader {

    public static Stack<City> loadCollection(String filename) throws IOException {

        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("File : " + filename + " not found. Please check the file path.");
        }
        if (!file.canRead()) {
            throw new IOException("Нет прав на чтение файла: " + filename);
        }

        // Читаем текст файла в память
        StringBuilder content = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            char[] buffer = new char[1024];
            int len;
            while ((len = reader.read(buffer)) != -1) {
                content.append(buffer, 0, len);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        //mapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd"));
        return mapper.readValue(content.toString(), new TypeReference<Stack<City>>() {});
    }

}