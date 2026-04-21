package org.example.commands;

import org.example.Application;
import org.example.data.City;
import org.example.data.StandardOfLiving;
import java.util.Scanner;
import java.util.Stack;

public class CountLessThanStandardOfLiving implements Command {

    public CountLessThanStandardOfLiving(Application app) {
    }

    @Override
    public String getName() {
        return "count_less_than_standard_of_living";
    }

    @Override
    public String getDescription() {
        return "Count elements with standardOfLiving less than the specified value";
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: count_less_than_standard_of_living <value>");
            return;
        }

        try {
            StandardOfLiving x = StandardOfLiving.valueOf(args[1].toUpperCase());
            Stack<City> stack = Application.getCityStack();
            int count = 0;

            for (City city : stack) {
                StandardOfLiving sol = city.getStandardOfLiving();
                if (sol != null && sol.getRank() < x.getRank()) {
                    count++;
                }
            }

            System.out.println("Cities with standard of living below " + x + ": " + count);

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid StandardOfLiving value. Valid values: HIGH, MEDIUM, LOW, ULTRA_LOW, NIGHTMARE");
        }
    }
}