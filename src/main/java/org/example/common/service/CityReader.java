package org.example.common.service;

import org.example.Application;
import org.example.data.*;
import org.example.validate.CoordinatesValidator;
import org.example.validate.InputValidator;

import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import static org.example.validate.InputValidator.*;

/**
 * Сервис для ввода и валидации данных города в интерактивном и скриптовом режимах.
 * Автоматически генерирует {@code id} и {@code creationDate}, обрабатывает ошибки ввода.
 * @see City
 * @see InputValidator
 */

public class CityReader {

    public static Scanner scanner = new Scanner(System.in);

    private static int scriptDepth = 0;

    public static void setScanner(Scanner scanner) {
        CityReader.scanner = scanner;
    }

    public static void enterScript() {
        scriptDepth++;
    }

    public static void leaveScript() {
        if (scriptDepth > 0) {
            scriptDepth--;
        }
    }

    public static boolean isScriptMode() {
        return scriptDepth > 0;
    }

    public static City readCity() throws IllegalArgumentException {
        Scanner scanner = CityReader.scanner;
        if (isScriptMode()) {
            return readCityFromScript(scanner);
        }
        final int MAX_ATTEMPTS = 100;
        int attempts = 0;
        City city = new City();

        while(attempts < MAX_ATTEMPTS) {
            System.out.println("Entering city data (attempt " + (attempts + 1) + ") ===");
            try {
                city.setName(readName(scanner));
                break;
            }
            catch(IllegalArgumentException e) {
                System.err.println("Error validation: " + e.getMessage());
                System.out.println("Try to enter the data again.\n");
                attempts++;
            }
        }

        while(attempts < MAX_ATTEMPTS) {
            System.out.println("Enter the coordinates:");
            try {
                float x = readCoordinateX(scanner);
                double y = readCoordinateY(scanner);
                Coordinates coordinates = new Coordinates(x, y);
                CoordinatesValidator.validateCoordinates(coordinates);
                city.setCoordinates(coordinates);
                break;
            }
            catch(IllegalArgumentException e) {
                System.err.println("Error validation: " + e.getMessage());
                System.out.println("Try to enter the data again.\n");
                attempts++;
            }
        }

        while(attempts < MAX_ATTEMPTS) {
            try {
                double area = readDoubleArea(scanner);
                InputValidator.validateArea(area);
                city.setArea(area);
                break;
            }
            catch(IllegalArgumentException e) {
                System.err.println("Error validation: " + e.getMessage());
                System.out.println("Try to enter the data again.\n");
                attempts++;
            }
        }

        while(attempts < MAX_ATTEMPTS) {
            try{
                int population = readIntPollution(scanner);
                InputValidator.validatePopulation(population);
                city.setPopulation(population);
                break;
            }
            catch(IllegalArgumentException e) {
                System.err.println("Error validation: " + e.getMessage());
                System.out.println("Try to enter the data again.\n");
                attempts++;
            }
        }

        while(attempts < MAX_ATTEMPTS){
            try{
                Climate climate = readEnumClimate(scanner);
                city.setClimate(climate);
                break;
            }
            catch(IllegalArgumentException e) {
                System.err.println("Error validation: " + e.getMessage());
                System.out.println("Try to enter the data again.\n");
                attempts++;
            }
        }

        while(attempts < MAX_ATTEMPTS){
            try{
                int metersAboveSeaLevel = readSeaLevel(scanner);
                city.setMetersAboveSeaLevel(metersAboveSeaLevel);
                break;
            }
            catch(IllegalArgumentException e) {
                System.err.println("Error validation: " + e.getMessage());
                System.out.println("Try to enter the data again.\n");
                attempts++;
            }
        }

        while(attempts < MAX_ATTEMPTS){
            try {
                Government government = readEnumGovernment(scanner);
                city.setGovernment(government);
                break;
            }
            catch(IllegalArgumentException e) {
                System.err.println("Error validation: " + e.getMessage());
                System.out.println("Try to enter the data again.\n");
                attempts++;
            }

        }

        while(attempts < MAX_ATTEMPTS){
            try{
                StandardOfLiving standardOfLiving = readEnumStandard(scanner);
                city.setStandardOfLiving(standardOfLiving);
                break;
            }
            catch(IllegalArgumentException e) {
                System.err.println("Error validation: " + e.getMessage());
                System.out.println("Try to enter the data again.\n");
                attempts++;
            }
        }

        while(attempts < MAX_ATTEMPTS){
            try{
                Human governor = null;
                if (readYesNo(scanner)) {
                    int date = 0;
                    while(date < MAX_ATTEMPTS){
                        try{
                            Date birthday = readDate(scanner);
                            governor = new Human(birthday);
                            break;
                        }
                        catch(IllegalArgumentException e) {
                            System.err.println("Error validation: " + e.getMessage());
                            System.out.println("Try to enter the date again.\n");
                            date++;
                        }
                    }

                    if (governor == null) {
                        throw new IllegalArgumentException("Too many failed attempts for birthday");
                    }
                }

                city.setGovernor(governor);
                break;
            }
            catch(IllegalArgumentException e) {
                System.err.println("Error validation: " + e.getMessage());
                System.out.println("Try to enter the data again.\n");
                attempts++;
            }
        }
        // creationDate генерируется автоматически — локальное время системы в момент создания объекта
        city.setCreationDate(new Date());

        long id = Application.getNextId();
        city.setId(id);

        if(attempts == MAX_ATTEMPTS) {
            System.out.println("You've used a lot of input attempts, repeat after 10 minutes.");
            Runtime.getRuntime().exit(0);
        }

        System.out.println("The data is accepted!");
        return city;
    }


    private static City readCityFromScript(Scanner scanner) throws IllegalArgumentException {
        City city = new City();
        try {
            String nameLine = nextLineRequired(scanner);
            city.setName(InputValidator.validateName(nameLine));

            String xLine = nextLineRequired(scanner);
            float x = InputValidator.validateX(Float.parseFloat(xLine.trim()));
            String yLine = nextLineRequired(scanner);
            double y = InputValidator.validateY(Double.parseDouble(yLine.trim()));
            Coordinates coordinates = new Coordinates(x, y);
            CoordinatesValidator.validateCoordinates(coordinates);
            city.setCoordinates(coordinates);

            String areaLine = nextLineRequired(scanner);
            double area = Double.parseDouble(areaLine.trim());
            InputValidator.validateArea(area);
            city.setArea(area);

            String popLine = nextLineRequired(scanner);
            int population = Integer.parseInt(popLine.trim());
            InputValidator.validatePopulation(population);
            city.setPopulation(population);

            String climateLine = nextLineRequired(scanner);
            city.setClimate(InputValidator.validateEnum(
                    climateLine.isEmpty() ? null : climateLine,
                    Climate.class, "climate", false));

            String seaLine = nextLineRequired(scanner);
            city.setMetersAboveSeaLevel(Integer.parseInt(seaLine.trim()));

            String govLine = nextLineRequired(scanner);
            city.setGovernment(InputValidator.validateEnum(
                    govLine, Government.class, "government", true));

            String solLine = nextLineRequired(scanner);
            city.setStandardOfLiving(InputValidator.validateEnum(
                    solLine.isEmpty() ? null : solLine,
                    StandardOfLiving.class, "StandardOfLiving", false));

            String ynLine = nextLineRequired(scanner);
            Human governor = null;
            if (isYes(ynLine)) {
                String birthdayLine = nextLineRequired(scanner);
                governor = new Human(InputValidator.validateBirthday(birthdayLine));
            }
            city.setGovernor(governor);

            city.setCreationDate(new Date());
            long id = Application.getNextId();
            city.setId(id);
            return city;
        } catch (NumberFormatException e) {
            System.err.println("Script parse error: " + e.getMessage());
            throw new IllegalArgumentException("Invalid number in script");
        } catch (IllegalArgumentException e) {
            System.err.println("Script validation: " + e.getMessage());
            throw e;
        }
    }

    private static String nextLineRequired(Scanner scanner) throws IllegalArgumentException {
        if (!scanner.hasNextLine()) {
            throw new IllegalArgumentException("Unexpected end of script file");
        }
        return scanner.nextLine();
    }

    private static boolean isYes(String line) {
        String s = line.trim();
        return s.equalsIgnoreCase("y") || s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("да");
    }

    private static String readName(Scanner scanner) throws IllegalArgumentException {
        System.out.print("Enter the name of the city: ");
        String input = scanner.nextLine();
        return validateName(input);
    }

    private static float readCoordinateX(Scanner scanner) throws IllegalArgumentException {
        System.out.print("  X: ");
        if(scanner.hasNextFloat()){
            return validateX(scanner.nextFloat());
        }
        else{
            scanner.next();
            throw new IllegalArgumentException("Type Error!");
        }
    }

    private static int readSeaLevel(Scanner scanner) throws IllegalArgumentException {
        System.out.print("Enter the height above sea level: ");

        if(scanner.hasNextInt()){
            int readSeaLevel = scanner.nextInt();
            scanner.nextLine();
            return  readSeaLevel;
        }
        else{
            scanner.next();
            throw new IllegalArgumentException("Type Error!");
        }
    }

    private static double readCoordinateY(Scanner scanner) throws IllegalArgumentException {
        System.out.print("  Y: ");
        if(scanner.hasNextDouble()){
            return validateY(scanner.nextDouble());

        }
        else{
            scanner.next();
            throw new IllegalArgumentException("Type Error!");
        }
    }

    private static Integer readIntPollution(Scanner scanner) throws IllegalArgumentException {
        System.out.print("Enter the city's population: ");
        if(scanner.hasNextInt()){
            Integer population = scanner.nextInt();
            scanner.nextLine();
            validatePopulation(population);
            return population;
        }
        else{
            scanner.next();
            throw new IllegalArgumentException("Type Error!");
        }
    }

    private static double readDoubleArea(Scanner scanner) throws IllegalArgumentException {
        System.out.println("Enter the area of the city: ");
        if(scanner.hasNextDouble()){
            double ar = scanner.nextDouble();
            InputValidator.validateArea(ar);
            return ar;
        }
        else{
            scanner.next();
            throw new IllegalArgumentException("Type Error!");
        }
    }

    private static Climate readEnumClimate(Scanner scanner) throws IllegalArgumentException {
        System.out.println("Available values: " + Arrays.toString(Climate.values()));
        System.out.print("Select the climate (Enter to skip): ");
        String climateInput = scanner.nextLine().trim();

        return InputValidator.validateEnum(
                climateInput.isEmpty() ? null : climateInput,
                Climate.class,
                "climate",
                false
        );
    }

    private static Government readEnumGovernment(Scanner scanner) throws IllegalArgumentException {
        System.out.println("Available values: " + Arrays.toString(Government.values()));
        System.out.println("Choose a form of government: ");
        String govInput = scanner.nextLine().trim();

        return InputValidator.validateEnum(
                govInput,
                Government.class,
                "government",
                true
        );
    }

    private static StandardOfLiving readEnumStandard(Scanner scanner) throws IllegalArgumentException {
        System.out.println("Available values: " + Arrays.toString(StandardOfLiving.values()));
        System.out.print("Select the standard of living (Enter to skip): ");
        String input = scanner.nextLine().trim();

        return InputValidator.validateEnum(
                input.isEmpty() ? null : input,
                StandardOfLiving.class,
                "StandardOfLiving",
                false
        );

    }

    private static boolean readYesNo(Scanner scanner) {
        System.out.print("Add a governor? (y/n): ");
        String input = scanner.nextLine().trim();
        return input.equalsIgnoreCase("y")
                || input.equalsIgnoreCase("yes");
    }

    private static Date readDate(Scanner scanner) throws IllegalArgumentException {
        System.out.print("Enter your birthday (yyyy-MM-ddTHH:MM:SS): ");
        return InputValidator.validateBirthday(scanner.nextLine().trim());
    }


}

