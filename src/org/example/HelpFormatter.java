package org.example;

import org.example.commands.Command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class HelpFormatter {

    private HelpFormatter() {
    }

    public static String serverHelpMessage() {
        String[][] rows = {
                {"register login password", "create a new account (password stored as SHA-384)"},
                {"login user password", "set credentials sent with every request (client only)"},
                {"help", "show help for available commands"},
                {"info", "show information about collection"},
                {"show", "show all elements sorted by name"},
                {"add", "add city from console or JSON file"},
                {"update id", "update city by id"},
                {"remove_by_id id", "remove city by id"},
                {"clear", "remove all cities you own (others remain)"},
                {"insert_at index", "insert city at index"},
                {"add_if_max", "add city if it is max"},
                {"sort", "sort collection in natural order"},
                {"count_less_than_standard_of_living value", "count elements by standardOfLiving"},
                {"filter_by_governor text", "filter elements by governor text"},
                {"print_field_ascending_standard_of_living", "print standardOfLiving values ascending"},
                {"execute_script file", "execute commands from script file"},
                {"exit", "close client application"}
        };
        return formatRows(rows);
    }

    public static void printStandalone(Map<String, Command> commands) {
        List<Map.Entry<String, Command>> list = new ArrayList<>(commands.entrySet());
        list.sort(Comparator.comparing(Map.Entry::getKey));

        int maxNameLen = 0;
        for (Map.Entry<String, Command> e : list) {
            String name = e.getValue().getName();
            if (name.length() > maxNameLen) {
                maxNameLen = name.length();
            }
        }

        System.out.println();
        System.out.println("Available commands:");
        System.out.println(repeat('-', maxNameLen + 50));
        for (Map.Entry<String, Command> e : list) {
            System.out.printf("%-" + maxNameLen + "s  |  %s%n",
                    e.getValue().getName(),
                    e.getValue().getDescription());
        }
        System.out.println(repeat('-', maxNameLen + 50));
        System.out.println();
    }

    private static String formatRows(String[][] rows) {
        int maxNameLen = 0;
        for (String[] row : rows) {
            if (row[0].length() > maxNameLen) {
                maxNameLen = row[0].length();
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Available commands:\n");
        sb.append(repeat('-', maxNameLen + 50)).append('\n');
        for (String[] row : rows) {
            sb.append(String.format("%-" + maxNameLen + "s  |  %s%n", row[0], row[1]));
        }
        sb.append(repeat('-', maxNameLen + 50));
        return sb.toString();
    }

    private static String repeat(char c, int count) {
        if (count <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
