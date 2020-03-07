package com.tstu.productinfo.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.tstu.commons.dto.http.request.View;
import com.tstu.commons.dto.http.response.productinfo.CategoryResponse;
import com.tstu.productinfo.dto.ValidatedCategoryDataRequest;
import com.tstu.productinfo.exception.ProductInfoExceptionMessage;
import com.tstu.productinfo.mapping.CategoryMapper;
import com.tstu.productinfo.model.Category;
import com.tstu.productinfo.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryRestController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    /**
     * Добавление(создание) новой категории в базу
     * @param categoryDataRequest Структура запроса на добавление категории
     * @return Структура ответа новой категории
     */
    @PostMapping("/create")
    @ApiOperation(value = "${CategoryRestController.create}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductInfoExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 422, message = ProductInfoExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<?> createNewCategory(@JsonView(View.PRODUCT_INFO.class) @RequestBody @Valid ValidatedCategoryDataRequest categoryDataRequest) {
        Category category = categoryService.create(categoryMapper.categoryDataRequestToCategory(categoryDataRequest));
        return ResponseEntity.ok(categoryMapper.categoryToCategoryResponse(category));
    }


    /**
     * Получение конкретногй категории по его id
     * @param id Id категории
     * @return Структура ответа с найденной категорией
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        CategoryResponse categoryResponse = categoryMapper.categoryToCategoryResponse(categoryService.findById(id));
        return ResponseEntity.ok(categoryResponse);
    }

    /**
     * Получение конкретногй категории по её наименованию
     * @param name Наименование категории
     * @return Структура ответа с найденной категорией
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getCategoryByName(@PathVariable String name) {
        CategoryResponse categoryResponse = categoryMapper.categoryToCategoryResponse(categoryService.findByName(name));
        return ResponseEntity.ok(categoryResponse);
    }

    /**
     * Получение всех возможных категорий
     * @return Структура ответа со списком категорий
     */
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<CategoryResponse> categories = categoryService.findAll().stream()
                .map(categoryMapper::categoryToCategoryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }


    /**
     * Запрос на удаление категории
     * @param id Id категории
     * @return Структура ответа с информацией, что запрос на удаление был произведен
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok("Запись удалена");
    }
}
