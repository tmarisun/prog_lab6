package org.example.server.cmdd;

import org.example.data.City;
import org.example.net.protocol.CommandRequest;
import org.example.net.protocol.CommandResponse;
import org.example.server.ServerCollectionService;

public class AddIfMaxCommand implements ServerCommandHandler {
    @Override
    public CommandResponse execute(ServerCollectionService service, CommandRequest request) {
        City city = request.getCity();
        if (city == null) {
            return CommandResponse.fail("City payload is required");
        }

        boolean added = service.addIfMax(city);
        if (added) {
            return CommandResponse.ok("Added (value is max)");
        }
        return CommandResponse.fail("Not added: value is not greater than max");
    }
}