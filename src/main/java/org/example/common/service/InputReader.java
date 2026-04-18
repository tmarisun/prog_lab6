package org.example.common.service;

import org.example.data.City;

/**
 * Командам не важно, откуда пришли данные: консоль или файл.
 */
public interface InputReader {

    /**
     * Считывает и возвращает объект City.
     * При любой ошибке возвращает null.
     *
     */
    City readCity();
}

