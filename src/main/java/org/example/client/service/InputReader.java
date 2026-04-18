package org.example.client.service;


import org.example.common.data.City;

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

