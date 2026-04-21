package org.example.server;

import org.example.HelpFormatter;
import org.example.data.City;
import org.example.data.StandardOfLiving;
import org.example.net.protocol.CommandRequest;
import org.example.net.protocol.CommandResponse;
import org.example.net.protocol.CommandType;

import java.util.List;

public class ServerCommandProcessor {
    private final ServerCollectionService service;

    public ServerCommandProcessor(ServerCollectionService service) {
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
    }

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

