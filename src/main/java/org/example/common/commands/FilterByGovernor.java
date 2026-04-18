package org.example.common.commands;

import org.example.Application;
import org.example.data.City;
import org.example.data.Human;
import org.example.validate.InputValidator;

import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

public class FilterByGovernor implements Command {

    public FilterByGovernor(Application app) {
    }

    @Override
    public String getName() {
        return "filter_by_governor";
    }

    @Override
    public String getDescription() {
        return "Filter elements by governor's birthday";
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: filter_by_governor <birthday (yyyy-MM-ddTHH:mm:ss)>");
            return;
        }

        try {
            Date birthday = InputValidator.validateBirthday(args[1]);
            Stack<City> stack = Application.getCityStack();
            boolean found = false;

            for (City city : stack) {
                Human governor = city.getGovernor();
                if (governor != null && isSameDay(governor.birthday(), birthday)) {
                    //System.out.println(city);
                    System.out.println("governor: " + governor);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No cities found with governor born on " + birthday);
            }

        } catch (Exception e) {
            System.out.println("Invalid date format. Use yyyy-MM-dd'T'HH:mm:ss");
        }

    }

    private boolean isSameDay(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(d1);
        c2.setTime(d2);

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
                c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
    }
}