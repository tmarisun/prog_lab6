package org.example.server;

import org.example.HelpFormatter;
import org.example.data.City;
import org.example.data.StandardOfLiving;
import org.example.net.protocol.CommandRequest;
import org.example.net.protocol.CommandResponse;
import org.example.net.protocol.CommandType;
import org.example.server.cmdd.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerCommandProcessor {
    private final ServerCollectionService service;
    private final Map<CommandType, ServerCommandHandler> handlers = new HashMap<>();

    public ServerCommandProcessor(ServerCollectionService service) {
        this.service = service;
        registerHandlers();
    }

    private void registerHandlers() {
        handlers.put(CommandType.HELP,
                new SimpleServerCommand(CommandType.HELP, s -> HelpFormatter.serverHelpMessage()));

        handlers.put(CommandType.INFO,
                new SimpleServerCommand(CommandType.INFO, ServerCollectionService::info));

        handlers.put(CommandType.CLEAR,
                new SimpleServerCommand(CommandType.CLEAR, s -> { s.clear(); return "Collection cleared"; }));

        handlers.put(CommandType.SORT,
                new SimpleServerCommand(CommandType.SORT, s -> { s.sortNatural(); return "Sorted"; }));

        handlers.put(CommandType.ADD, new AddCommand());
        handlers.put(CommandType.ADD_IF_MAX, new AddIfMaxCommand());
        handlers.put(CommandType.UPDATE, new UpdateCommand());
        handlers.put(CommandType.INSERT_AT, new InsertAtCommand());
        handlers.put(CommandType.REMOVE_BY_ID, new RemoveByIdCommand());

        handlers.put(CommandType.SHOW, new ShowCommand());
        handlers.put(CommandType.FILTER_BY_GOVERNOR, new FilterByGovernorCommand());
        handlers.put(CommandType.COUNT_LESS_THAN_STANDARD_OF_LIVING, new CountLessThanCommand());
        handlers.put(CommandType.PRINT_FIELD_ASCENDING_STANDARD_OF_LIVING, new PrintFieldAscendingCommand());

        handlers.put(CommandType.EXIT, (s, r) -> CommandResponse.ok("Client closed"));
        handlers.put(CommandType.SERVER_SAVE, (s, r) -> CommandResponse.fail("Use console for save"));
    }

    public CommandResponse process(CommandRequest req) {
        try {
            CommandType type = req.getType();
            if (type == null) {
                return CommandResponse.fail("Empty command type");
            }

            ServerCommandHandler handler = handlers.get(type);
            if (handler == null) {
                return CommandResponse.fail("Unsupported command: " + type);
            }

            return handler.execute(service, req);

        } catch (Exception e) {
            return CommandResponse.fail("Server internal error: " + e.getMessage());
        }
    }
    /*public ServerCommandProcessor(ServerCollectionService service) {
        this.service = service;
    }

    public CommandResponse process(CommandRequest req) {
        try {
            CommandType type = req.getType();
            if (type == null) return CommandResponse.fail("Empty command");

            switch (type) {
                case HELP:
                    return CommandResponse.ok(HelpFormatter.serverHelpMessage());
                case INFO:
                    return CommandResponse.ok(service.info());
                case SHOW: {
                    CommandResponse r = CommandResponse.ok("");
                    r.setCities(service.getSortedByName());
                    return r;
                }
                case ADD: {
                    if (req.getCity() == null) {
                        return CommandResponse.fail("City payload is required");
                    }
                    City added = service.add(req.getCity());
                    return CommandResponse.ok("Added city with id " + added.getId());
                }
                case ADD_IF_MAX: {
                    if (req.getCity() == null) {
                        return CommandResponse.fail("City payload is required");
                    }
                    boolean added = service.addIfMax(req.getCity());
                    if (added) {
                        return CommandResponse.ok("Added");
                    }
                    return CommandResponse.fail("Not added: value is not max");
                }
                case UPDATE:
                    if (req.getCity() == null) {
                        return CommandResponse.fail("City payload is required");
                    }
                    boolean updated = service.update(req.getId(), req.getCity());
                    if (updated) {
                        return CommandResponse.ok("Updated");
                    }
                    return CommandResponse.fail("City not found");
                case INSERT_AT:
                    if (req.getCity() == null) {
                        return CommandResponse.fail("City payload is required");
                    }
                    boolean inserted = service.insertAt(req.getIndex(), req.getCity());
                    if (inserted) {
                        return CommandResponse.ok("Inserted");
                    }
                    return CommandResponse.fail("Invalid index");
                case REMOVE_BY_ID:
                    boolean removed = service.removeById(req.getId());
                    if (removed) {
                        return CommandResponse.ok("Removed");
                    }
                    return CommandResponse.fail("City not found");
                case CLEAR:
                    service.clear();
                    return CommandResponse.ok("Collection cleared");
                case SORT:
                    service.sortNatural();
                    return CommandResponse.ok("Sorted");
                case COUNT_LESS_THAN_STANDARD_OF_LIVING: {
                    StandardOfLiving val = StandardOfLiving.valueOf(req.getArg().toUpperCase());
                    long cnt = service.countLessThan(val);
                    return CommandResponse.ok("Count: " + cnt);
                }
                case FILTER_BY_GOVERNOR: {
                    List<City> list = service.filterByGovernor(req.getArg());
                    CommandResponse r = CommandResponse.ok("Filtered");
                    list.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
                    r.setCities(list);
                    return r;
                }
                case PRINT_FIELD_ASCENDING_STANDARD_OF_LIVING: {
                    List<StandardOfLiving> values = service.getStandardsAscending();
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < values.size(); i++) {
                        if (i > 0) {
                            builder.append(", ");
                        }
                        builder.append(values.get(i).name());
                    }
                    String msg = builder.toString();
                    return CommandResponse.ok(msg);
                }
                case SERVER_SAVE:
                    return CommandResponse.fail("Command is server-only");
                case EXIT:
                    return CommandResponse.ok("Client closed");
                default:
                    return CommandResponse.fail("Unsupported command");
            }
        } catch (Exception e) {
            return CommandResponse.fail("Server error: " + e.getMessage());
        }
    }*/

    public CommandResponse processServerConsoleCommand(String line) {
        if (line == null || line.length() == 0) {
            return null;
        }
        String command = line.toLowerCase();
        try {
            if ("save".equals(command)) {
                service.save();
                return CommandResponse.ok("Collection saved");
            }
            if ("help".equals(command)) {
                return CommandResponse.ok("Server commands: save, help");
            }
            return CommandResponse.fail("Unknown server command");
        } catch (Exception e) {
            return CommandResponse.fail("Server command error: " + e.getMessage());
        }
    }
}
