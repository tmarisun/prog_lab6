package org.example.server.cmdd;

import org.example.net.protocol.CommandRequest;
import org.example.net.protocol.CommandResponse;
import org.example.server.ServerCollectionService;

public class RemoveByIdCommand implements ServerCommandHandler {
    @Override
    public CommandResponse execute(ServerCollectionService service, CommandRequest request) {
        Long id = request.getId();
        if (id == null) {
            return CommandResponse.fail("ID is required for removal");
        }

        boolean removed = service.removeById(id);
        if (removed) {
            return CommandResponse.ok("Removed city with ID: " + id);
        }
        return CommandResponse.fail("City not found with ID: " + id);
    }
}