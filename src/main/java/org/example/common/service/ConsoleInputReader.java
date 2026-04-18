package org.example.common.service;

import org.example.data.City;

import java.util.Scanner;

/**
 * Ридер для пошагового ввода City из консоли.
 * Использует переданный Scanner и не создаёт/закрывает его.
 */
public class ConsoleInputReader implements InputReader {
    private final Scanner scanner;

    public ConsoleInputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public City readCity() {
        try {
            CityReader.setScanner(scanner);
            return CityReader.readCity();
        } catch (Exception e) {
            System.err.println("Console input error: " + e.getMessage());
            return null;
        }
    }
}

