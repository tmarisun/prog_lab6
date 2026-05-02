package org.example.server.cmdd;

import org.example.net.protocol.CommandRequest;
import org.example.net.protocol.CommandResponse;
import org.example.server.ServerCollectionService;

public class SortCommand implements ServerCommandHandler {

    @Override
    public CommandResponse execute(ServerCollectionService service, CommandRequest request) {
        try {
            service.sortNatural();
            return CommandResponse.ok("Sorted");
        } catch (Exception e) {
            return CommandResponse.fail("Sort error: " + e.getMessage());
        }
    }
}
