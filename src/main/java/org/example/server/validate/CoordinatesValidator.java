package org.example.server.validate;


import org.example.common.data.Coordinates;

;
public class CoordinatesValidator {
    public static void validateCoordinates(Coordinates coordinates) throws IllegalArgumentException {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        InputValidator.validateCoordinates(coordinates.getX(), coordinates.getY());
    }
}