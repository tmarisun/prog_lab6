package org.example.common;

import org.example.common.data.City;
import org.example.common.data.StandardOfLiving;
import org.example.server.validate.CityValidator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Управление коллекцией городов на сервере.
 * <p>
 * Инкапсулирует хранение, валидацию и потокобезопасный доступ к данным.
 * Все методы возвращают копии или неизменяемые представления,
 * чтобы команды не могли модифицировать коллекцию напрямую.
 * </p>
 */
public class CollectionManager {

    // === КОНФИГУРАЦИЯ ===

    /** Хранилище данных: используем ArrayList для гибкости фильтрации */
    private final List<City> collection;

    /** Lock для потокобезопасности: множественные читатели / один писатель */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true); // fair lock

    /** Путь к файлу данных (для save/load) */
    private String dataFilePath;

    /** Дата инициализации коллекции (для команды info) */
    private final LocalDateTime initializationDate;

    /** Счетчик для авто-генерации ID (монотонно растущий) */
    private long nextIdCounter;


    // === КОНСТРУКТОРЫ ===

    /** Пустая коллекция (для тестов или нового сервера) */
    public CollectionManager() {
        this.collection = new ArrayList<>();
        this.initializationDate = LocalDateTime.now();
        this.nextIdCounter = 1L;
    }

    /** Коллекция с начальными данными (при загрузке из файла) */
    public CollectionManager(List<City> initialCollection) {
        this.collection = new ArrayList<>();
        this.initializationDate = LocalDateTime.now();

        if (initialCollection != null && !initialCollection.isEmpty()) {
            // Валидация и добавление с обновлением nextIdCounter
            for (City city : initialCollection) {
                try {
                    CityValidator.validateCity(city); // ваша валидация
                    this.collection.add(city);
                    if (city.getId() != null && city.getId() >= nextIdCounter) {
                        this.nextIdCounter = city.getId() + 1;
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("⚠️ Пропущен невалидный город: " + e.getMessage());
                }
            }
        }
    }


    // === ЧТЕНИЕ: множественный доступ (read lock) ===

    /**
     * Возвращает копию коллекции для безопасного перебора.
     */
    public List<City> getAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(collection); // защитная копия
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Находит город по ID.
     * @return Optional.empty() если не найдено
     */
    public Optional<City> getById(Long id) {
        if (id == null) return Optional.empty();

        lock.readLock().lock();
        try {
            return collection.stream()
                    .filter(city -> id.equals(city.getId()))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Проверяет, существует ли город с таким ID.
     */
    public boolean existsById(Long id) {
        if (id == null) return false;

        lock.readLock().lock();
        try {
            return collection.stream().anyMatch(city -> id.equals(city.getId()));
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Возвращает следующий свободный ID (монотонно растущий).
     */
    public long getNextId() {
        lock.readLock().lock();
        try {
            return nextIdCounter;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Количество элементов в коллекции.
     */
    public long size() {
        lock.readLock().lock();
        try {
            return collection.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Информация о коллекции для команды 'info'.
     */
    public String getInfo() {
        lock.readLock().lock();
        try {
            return String.format(
                    "Тип коллекции: ArrayList<City>%n" +
                            "Дата инициализации: %s%n" +
                            "Количество элементов: %d",
                    initializationDate,
                    collection.size()
            );
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Максимальный элемент по натуральному порядку (compareTo).
     * Используется для команды add_if_max.
     */
    public Optional<City> getMax() {
        lock.readLock().lock();
        try {
            return collection.stream().max(City::compareTo);
        } finally {
            lock.readLock().unlock();
        }
    }


    // === ЗАПИСЬ: исключительный доступ (write lock) ===

    /**
     * Добавляет город в коллекцию.
     * @return true если добавлен, false если город с таким ID уже существует
     */
    public boolean add(City city) {
        if (city == null) return false;

        lock.writeLock().lock();
        try {
            // Валидация
            CityValidator.validateCity(city);

            // Проверка на дубликат ID
            if (existsByIdInternal(city.getId())) {
                return false;
            }

            // Авто-генерация ID если не задан
            if (city.getId() == null || city.getId() <= 0) {
                city.setId(nextIdCounter++);
            } else {
                // Обновляем счетчик, если добавляемый ID больше текущего
                if (city.getId() >= nextIdCounter) {
                    nextIdCounter = city.getId() + 1;
                }
            }

            // Авто-дата создания
            if (city.getCreationDate() == null) {
                city.setCreationDate(new Date());
            }

            collection.add(city);
            return true;

        } catch (IllegalArgumentException e) {
            // Валидация не прошла — не добавляем
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Обновляет город по ID.
     * @return true если обновлён, false если не найден
     */
    public boolean update(Long id, City updatedCity) {
        if (id == null || updatedCity == null