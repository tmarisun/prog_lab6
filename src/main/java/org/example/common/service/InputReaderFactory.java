package org.example.common.service;

import java.util.Scanner;

/**
 * Фабрика для создания источников ввода ({@link InputReader}).
 * Автоматически выбирает файловый или консольный режим в зависимости от наличия пути к файлу.
 */

public final class InputReaderFactory {

    private InputReaderFactory() {
    }

    /**
     * Если путь к файлу задан, возвращает JSON-ридер, иначе консольный ридер.
     */
    public static InputReader createReader(String filePath, Scanner scanner) {
        if (filePath != null && !filePath.trim().isEmpty()) {
            return new JsonFileInputReader(filePath.trim());
        }
        return new ConsoleInputReader(scanner);
    }
}

