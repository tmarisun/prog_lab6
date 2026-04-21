package org.example.client.cmd;

import org.example.data.City;
import org.example.net.protocol.CommandRequest;
import org.example.net.protocol.CommandType;

public class InsertAtCommand implements ClientCommand {
    @Override
    public void execute(String arg, CommandRequest request) throws Exception {
        if (arg == null || arg.trim().isEmpty()) {
            throw new IllegalArgumentException("Index [filePath] is required");
        }

        // Разделяем аргументы: первый токен - индекс, остальное - путь к файлу
        String[] parts = arg.split("\\s+", 2);

        request.setType(CommandType.INSERT_AT);

        try {
            request.setIndex(Integer.parseInt(parts[0].trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Index must be an integer");
        }

        String filePath = parts.length > 1 ? parts[1] : null;

        City city = CityInputHelper.readCity(filePath);
        if (city == null) {
            throw new IllegalArgumentException("City input cancelled or invalid");
        }

        request.setCity(city);
    }
}