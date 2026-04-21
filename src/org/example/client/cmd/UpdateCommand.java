package org.example.client.cmd;

import org.example.data.City;
import org.example.net.protocol.CommandRequest;
import org.example.net.protocol.CommandType;

public class UpdateCommand implements ClientCommand {
    @Override
    public void execute(String arg, CommandRequest request) throws Exception {
        if (arg == null || arg.trim().isEmpty()) {
            throw new IllegalArgumentException("ID [filePath] is required");
        }

        String[] parts = arg.split("\\s+", 2);
        request.setType(CommandType.UPDATE);
        request.setId(Long.parseLong(parts[0].trim()));

        String filePath = parts.length > 1 ? parts[1] : null;
        City city = CityInputHelper.readCity(filePath);

        if (city == null) {
            throw new IllegalArgumentException("City input cancelled or invalid");
        }
        request.setCity(city);
    }
}