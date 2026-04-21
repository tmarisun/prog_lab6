package org.example.client;

import lombok.Getter;
import org.example.data.City;
import org.example.data.StandardOfLiving;
import org.example.net.protocol.CommandRequest;
import org.example.net.protocol.CommandType;
import org.example.service.CityReader;
import org.example.service.JsonFileInputReader;

@Getter
public class ClientCommandParser {
    private String lastError;

    public CommandRequest parse(String line) {
        lastError = null;
        String[] parts = line.split("\\s+", 2);
        if (parts.length == 0 || parts[0].isEmpty()) {
            lastError = "Empty command";
            return null;
        }

        String name = parts[0].toLowerCase();
        String arg = null;
        if (parts.length > 1) {
            arg = parts[1];
        }

        CommandRequest req = new CommandRequest();

        try {
            switch (name) {
                case "help": req.setType(CommandType.HELP); break;
                case "info": req.setType(CommandType.INFO); break;
                case "show": req.setType(CommandType.SHOW); break;
                case "clear": req.setType(CommandType.CLEAR); break;
                case "sort": req.setType(CommandType.SORT); break;
                case "exit": req.setType(CommandType.EXIT); break;
                case "save":
                    lastError = "Command 'save' is not available on client";
                    return null;
                case "remove_by_id":
                    req.setType(CommandType.REMOVE_BY_ID);
                    if (arg == null || arg.isEmpty()) {
                        lastError = "ID is required";
                        return null;
                    }
                    req.setId(Long.parseLong(arg));
                    break;
                case "count_less_than_standard_of_living":
                    req.setType(CommandType.COUNT_LESS_THAN_STANDARD_OF_LIVING);
                    if (arg == null || arg.isEmpty()) {
                        lastError = "Value is required";
                        return null;
                    }
                    req.setArg(StandardOfLiving.valueOf(arg.toUpperCase()).name());
                    break;
                case "filter_by_governor":
                    req.setType(CommandType.FILTER_BY_GOVERNOR);
                    if (arg == null || arg.isEmpty()) {
                        lastError = "Governor text is required";
                        return null;
                    }
                    req.setArg(arg);
                    break;
                case "print_field_ascending_standard_of_living":
                    req.setType(CommandType.PRINT_FIELD_ASCENDING_STANDARD_OF_LIVING);
                    break;
                case "add":
                    req.setType(CommandType.ADD);
                    req.setCity(readCityForCommand(arg));
                    if (req.getCity() == null) {
                        lastError = "City input cancelled or invalid";
                        return null;
                    }
                    break;
                case "add_if_max":
                    req.setType(CommandType.ADD_IF_MAX);
                    req.setCity(readCityForCommand(arg));
                    if (req.getCity() == null) {
                        lastError = "City input cancelled or invalid";
                        return null;
                    }
                    break;
                case "insert_at":
                    req.setType(CommandType.INSERT_AT);
                    if (arg == null || arg.isEmpty()) {
                        lastError = "Index is required";
                        return null;
                    }
                    String[] insertTokens = arg.split("\\s+");
                    req.setIndex(Integer.parseInt(insertTokens[0]));
                    String insertFilePath = null;
                    if (insertTokens.length > 1) {
                        insertFilePath = insertTokens[1];
                    }
                    req.setCity(readCityForCommand(insertFilePath));
                    if (req.getCity() == null) {
                        lastError = "City input cancelled or invalid";
                        return null;
                    }
                    break;
                case "update": {
                    req.setType(CommandType.UPDATE);
                    if (arg == null || arg.isEmpty()) {
                        lastError = "ID is required";
                        return null;
                    }
                    String[] updateTokens = arg.split("\\s+");
                    req.setId(Long.parseLong(updateTokens[0]));
                    String updateFilePath = null;
                    if (updateTokens.length > 1) {
                        updateFilePath = updateTokens[1];
                    }
                    req.setCity(readCityForCommand(updateFilePath));
                    if (req.getCity() == null) {
                        lastError = "City input cancelled or invalid";
                        return null;
                    }
                    break;
                }
                default:
                    return null;
            }
        } catch (Exception e) {
            lastError = e.getMessage();
            return null;
        }
        return req;
    }

    private City readCityForCommand(String filePath) {
        try {
            if (filePath != null && filePath.length() > 0) {
                return new JsonFileInputReader(filePath).readCity();
            }
            return CityReader.readCity();
        } catch (Exception e) {
            return null;
        }
    }
}

