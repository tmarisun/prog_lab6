package org.example.server.cmdd;

import org.example.data.City;
import org.example.net.protocol.CommandRequest;
import org.example.net.protocol.CommandResponse;
import org.example.server.ServerCollectionService;
import org.example.server.cmdd.ServerCommandHandler;

public class InsertAtCommand implements ServerCommandHandler {
    @Override
    public CommandResponse execute(ServerCollectionService service, CommandRequest request) {
        City city = request.getCity();
        Integer index = request.getIndex();

        if (index == null || city == null) {
            return CommandResponse.fail("Index and City payload are required");
        }

        boolean inserted = service.insertAt(index, city);
        if (inserted) {
            return CommandResponse.ok("Inserted");
        }
        return CommandResponse.fail("Invalid index or insertion failed");
    }
}