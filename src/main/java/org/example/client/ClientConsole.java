package org.example.client;

import org.example.client.service.CityReader;
import org.example.common.data.City;
import org.example.common.network.Request;
import org.example.common.network.Response;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Клиентская консоль: читает ввод пользователя, строит Request,
 * отправляет через Sender и выводит ответ.
 * <p>
 * НЕ содержит логики команд — только взаимодействие с пользователем.
 */
public class ClientConsole {

    private final Sender sender;
    private final Scanner scanner; // вспомогательный класс для ввода City

    public ClientConsole(Sender sender, Scanner scanner) {
        this.sender = sender;
        this.scanner = scanner;
    }

    /**
     * Главный цикл обработки команд.
     * @param inStream поток ввода (System.in или файл для execute_script)
     * @param fileFlag true если выполняется скрипт (подавляет лишние ошибки)
     */
    public void runApp(InputStreamReader inStream, boolean fileFlag) {
        Scanner scanner = new Scanner(inStream);

        while (scanner.hasNextLine()) {
            try {
                // 1. Чтение команды
                String line = CityReader.scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                // 2. Парсинг: имя команды + аргументы
                String[] parts = line.split("\\s+", 2);
                String commandName = parts[0].toLowerCase();
                String[] arguments = parts.length > 1 ? parts[1].split("\\s+") : new String[0];

                // 3. Подготовка payload (только для команд, требующих объект)
                Object payload = null;
                if (List.of("add", "add_if_max", "update").contains(commandName)) {
                    payload = inputHelper.readCity(scanner, fileFlag);
                    if (payload == null) {
                        System.err.println("Ошибка ввода данных города. Команда отменена.");
                        continue;
                    }
                }

                // 4. Сборка Request
                Request request = new Request(commandName, arguments, payload);

                // 5. Отправка и получение ответа
                Response response = sender.sendRequest(request);
                if (response == null) {
                    System.err.println("Сервер не ответил. Проверьте соединение.");
                    continue;
                }

                // 6. Вывод результата
                System.out.println(response.getMessage());

                // Если в ответе есть данные (список городов, один город и т.д.)
                if (response.getData() != null) {
                    printData(response.getData());
                }

                // 7. Обработка специальных команд
                if ("exit".equalsIgnoreCase(commandName)) {
                    System.out.println("Соединение закрыто.");
                    break;
                }

                if ("execute_script".equalsIgnoreCase(commandName) && response.isSuccess()) {
                    // Скрипт уже выполнен на сервере, клиенту ничего делать не нужно
                    // Если же логика требует локального выполнения — раскомментируйте:
                    /*
                    if (arguments.length > 0) {
                        String filename = arguments[0];
                        try (InputStreamReader fileStream = new FileReader(filename)) {
                            runApp(fileStream, true); // рекурсивный запуск
                        }
                    }
                    */
                }

            } catch (Exception e) {
                if (!fileFlag) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
                // В режиме скрипта ошибки могут быть ожидаемыми — не спамим консоль
            }
        }
    }

    /**
     * Вывод данных из Response (списки, объекты).
     */
    @SuppressWarnings("unchecked")
    private void printData(Object data) {
        if (data instanceof City city) {
            System.out.println("📦 Объект: " + city);
        } else if (data instanceof List<?> list) {
            if (list.isEmpty()) {
                System.out.println("  (пусто)");
            } else {
                list.forEach(item -> System.out.println("  • " + item));
            }
        } else if (data instanceof String str) {
            System.out.println(str);
        } else {
            System.out.println("📦 Данные: " + data);
        }
    }
}