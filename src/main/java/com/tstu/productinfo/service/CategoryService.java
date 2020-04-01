package com.tstu.productinfo.service;

import com.tstu.productinfo.model.Category;

import java.util.List;

public interface CategoryService {
    /**
     * Найти все категории в базе данных
     * @return Коллекция категорий
     */
    List<Category> findAll();

    /**
     * Найти категорию по наименованию
     * @param name Наименование категория
     * @return Найденная категория
     */
    Category findByName(String name);

    /**
     * Найти категорию по псевдониму
     * @param alias Псевдоним категория
     * @return Найденная категория
     */
    Category findByAlias(String alias);

    /**
     * Найти категорию в базе по его id
     * @param id Id категории
     * @return Найденная категория
     */
    Category findById(Long id);


    /**
     * Добавление новой категории в базу данных
     * @param product Новая категория
     * @return Сохраненная категория
     */
    Category create(Category product);


    /**
     * Сохранение изменений категории в бд
     * @param product Изменная категория
     * @return Сохраннный изменная категория
     */
    Category update(Category product);

    /**
     * Удалить категорию в бд по его id
     * @param id Id продукта
     */
    void deleteById(Long id);
}
