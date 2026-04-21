package org.example.server.cmdd;

import org.example.data.City;
import org.example.net.protocol.CommandRequest;
import org.example.net.protocol.CommandResponse;
import org.example.server.ServerCollectionService;

public class UpdateCommand implements ServerCommandHandler {
    @Override
    public CommandResponse execute(ServerCollectionService service, CommandRequest request) {
        City city = request.getCity();
        Long id = request.getId();

        if (id == null || city == null) {
            return CommandResponse.fail("ID and City payload are required");
        }

        boolean updated = service.update(id, city);
        if (updated) {
            return CommandResponse.ok("Updated");
        }
        return CommandResponse.fail("City not found with ID: " + id);
    }
}