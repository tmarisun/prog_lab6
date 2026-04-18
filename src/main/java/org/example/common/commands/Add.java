// common/commands/AddCommand.java
package org.example.common.commands;

import org.example.common.data.City;

import common.Response;

import java.util.Collection;

public class Add implements Command {

    private final City city;  // ✅ Готовый, валидированный объект от клиента

    // Конструктор вызывается ТОЛЬКО на клиенте после валидации
    public Add(City city) {
        this.city = city;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "Добавить новый город в коллекцию";
    }

    @Override
    public Response execute(Collection<City> collection) {
        try {
            // ✅ 1. Проверка на дубликат ID (через Stream API, как требует ТЗ)
            boolean idExists = collection.stream()
                    .anyMatch(c -> c.getId().equals(city.getId()));

            if (idExists) {
                return new Response(false, "ID " + city.getId() + " уже существует", null);
            }

            // ✅ 2. Автогенерация ID (если нужно — сервер решает)
            // city.setId(generateNextId(collection)); // опционально

            // ✅ 3. Добавление в коллекцию
            collection.add(city);

            // ✅ 4. Возврат ответа (без println!)
            return new Response(true, "Город добавлен: " + city.getId(), null);

        } catch (Exception e) {
            return new Response(false, "Ошибка при добавлении: " + e.getMessage(), null);
        }
    }
}