package org.example.common.commands;

import org.example.Application;
import org.example.data.City;
import org.example.data.StandardOfLiving;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class PrintFieldAscendingStandardOfLiving implements Command {

    public PrintFieldAscendingStandardOfLiving(Application app) {
    }

    @Override
    public String getName() {
        return "print_field_ascending_standard_of_living";
    }

    @Override
    public String getDescription() {
        return "Print standardOfLiving values in ascending order";
    }

    @Override
    public void execute(String[] args) {
        Stack<City> stack = Application.getCityStack();

        List<StandardOfLiving> values = new ArrayList<>();
        for (City city : stack) {
            if (city.getStandardOfLiving() != null) {
                values.add(city.getStandardOfLiving());
            }
        }
        values.sort(new Comparator<StandardOfLiving>() {
            @Override
            public int compare(StandardOfLiving a, StandardOfLiving b) {
                return Integer.compare(a.getRank(), b.getRank());
            }
        });

        if (values.isEmpty()) {
            System.out.println("No standard of living values available.");
            return;
        }

        System.out.println("Standard of living values in ascending order:");
        for (StandardOfLiving sol : values) {
            System.out.println(sol);
        }
    }
}