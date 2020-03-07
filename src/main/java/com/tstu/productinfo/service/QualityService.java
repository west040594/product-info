package com.tstu.productinfo.service;

import com.tstu.commons.model.enums.QualityType;
import com.tstu.productinfo.model.Quality;

import java.util.Optional;

public interface QualityService {
    /**
     * Сохранения качества продукта(плюсы/минусы) в бд
     * Алгоритм:
     * 1. Проверить существует ли такое качество уже в базе
     * 2. Если качество не существует - то сохранить в базе и вернуть его
     * 3. Если качество существует - вернуть не сохранять и вернуть Optional.empty
     * @param quality Новое качество продукта
     * @return Сохраненное качество продукта
     */
    Optional<Quality> create(Quality quality);

    /**
     * Поиск качества в базе по его наименованию и типу(плюс/минус)
     * @param name  Качество продукта
     * @param qualityType Типо качества(плюс/минус)
     * @return Найденное качество
     */
    Optional<Quality> find(String name, QualityType qualityType);
}
