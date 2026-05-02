package org.example.client;

import org.example.Application;
import org.example.commands.Command;
import org.example.manager.ManagerCommands;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Check {

    List<String> ListCommands = new ArrayList<String>();
    private Application application;
    ManagerCommands manager = new ManagerCommands(application);

    public void createList() {
        Map<String, Command> commands = manager.getCommands();

        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            ListCommands.add(entry.getKey());
        }
    }

    private double PercentageOfMatch(String Command, String CheckString){
        int e = 0;
        for(int i = 0;i < min(Command.length(), CheckString.length()); i++){
            if(Command.charAt(i) == CheckString.charAt(i)) e++;
        }
        int l = max(Command.length(), CheckString.length()) / 2 - 1;
        int counter = 0;

        for(int i = 1; i <= CheckString.length(); i++){
            for(int j = max(1, i - l); j <= min(Command.length(), i + l); j++){
                if(Command.charAt(j - 1) == CheckString.charAt(i - 1)) counter++;
            }
        }
        double matches =  counter + e, t = (double) counter / 2;
        double dist = 0;
        if(matches != e){
            dist = (matches / Command.length() + matches / CheckString.length() + (matches - t) / matches) / 3;
        }

        return dist;
    }
    

    public String checkInput(String CheckString){
        createList();
        double MaxElement = -1;
        String Answer = "";
        for(String Command : ListCommands){
            double Element = PercentageOfMatch(Command, CheckString);
            if(Element > MaxElement){
                MaxElement = Element;
                Answer = Command;
            }

        }
        String e = "";
        if(MaxElement == 0){
            e = "Unknown command. Are you sober?";
        }
        else{
            e = "Unknown command, maybe you wanted : " + Answer;
        }
        return e;
    }
}
