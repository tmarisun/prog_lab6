package org.example.client.cmd;

import org.example.data.City;
import org.example.net.protocol.CommandRequest;
import org.example.net.protocol.CommandType;

public class AddIfMaxCommand implements ClientCommand {
    @Override
    public void execute(String arg, CommandRequest request) throws Exception {
        request.setType(CommandType.ADD_IF_MAX);

        City city = CityInputHelper.readCity(arg);
        if (city == null) {
            throw new IllegalArgumentException("City input cancelled or invalid");
        }

        request.setCity(city);
    }
}