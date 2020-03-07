package com.tstu.productinfo.service;

import com.tstu.productinfo.model.Category;
import com.tstu.productinfo.model.Product;

import java.util.List;

public interface ProductService {
    /**
     * Найти все продукты в базе данных
     * @return Коллекция продуктов
     */
    List<Product> findAll();

    /**
     * Найти все продукты в базе по их названиям
     * @param names Коллекция названий продуктов
     * @return Коллекция продуктов соотвествующая их названиям
     */
    List<Product> findAllByNames(List<String> names);

    /**
     * Найти продукт в базе по его id
     * @param id Id продукта
     * @return Найденный продукт
     */
    Product findById(Long id);

    /**
     * Найти продукты в базе по их категории
     * @param categoryName Наименование категории продукта
     * @return Найденные продукты
     */
    List<Product> findByCategoryName(String categoryName);

    /**
     * Добавление нового продукта в базу данных
     * Алгоритм:
     * 1. Установить флаги - Продукт Активен. Продукт Не заполнен
     * 2. Сохранить новый продукт в бд
     * 3. Отправить данные запросы в review-dom-parser для обновления информации о продукте {@link ParseRequestService#execute(Product)}
     * @param product Новый продукт
     * @return Сохраненный продукт
     */
    Product create(Product product);


    /**
     * Сохранение изменений продукта в бд
     * @param product Изменный продукт
     * @return Сохраннный изменный продукт
     */
    Product update(Product product);

    /**
     * Удалить продукт в бд по его id
     * @param id Id продукта
     */
    void deleteById(Long id);
}
