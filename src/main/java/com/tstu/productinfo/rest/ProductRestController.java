package com.tstu.productinfo.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.tstu.commons.dto.http.request.View;
import com.tstu.commons.dto.http.response.productinfo.ProductResponse;
import com.tstu.productinfo.dto.ValidatedProductDataRequest;
import com.tstu.productinfo.exception.ProductInfoExceptionMessage;
import com.tstu.productinfo.mapping.ProductMapper;
import com.tstu.productinfo.model.Product;
import com.tstu.productinfo.service.CategoryService;
import com.tstu.productinfo.service.ParseRequestService;
import com.tstu.productinfo.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductRestController {

    private final ProductMapper productMapper;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ParseRequestService parseRequestService;

    /**
     * Добавление(создание) нового продукта в базу
     * @param productDataRequest Структура запроса на добавление продукта
     * @return Структура ответа нового продукта
     */
    @PostMapping("/create")
    @ApiOperation(value = "${api.swagger.product.create}", response = ProductResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductInfoExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 422, message = ProductInfoExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<?> createNewProduct(@JsonView(View.PRODUCT_INFO.class) @RequestBody @Valid ValidatedProductDataRequest productDataRequest) {
        Product product = productService.create(productMapper.productDataRequestToProduct(productDataRequest, categoryService));
        return ResponseEntity.ok(productMapper.productToProductResponse(product));
    }


    /**
     * Получение конкретного продукта по его id
     * @param id Id продукта
     * @return Структура ответа с найденным продуктом
     */
    @ApiOperation(value = "${api.swagger.product.id}", response = ProductResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductInfoExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 404, message = ProductInfoExceptionMessage.PRODUCT_NOT_FOUND_MSG),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        ProductResponse productResponse = productMapper.productToProductResponse(productService.findById(id));
        return ResponseEntity.ok(productResponse);
    }


    /**
     * Получение конкретного продукта по его наименованию
     * @param name Наименование продукта
     * @return Структура ответа с найденным продуктом
     */
    @ApiOperation(value = "${api.swagger.product.name}", response = ProductResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductInfoExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 404, message = ProductInfoExceptionMessage.PRODUCT_NOT_FOUND_MSG),
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getProductByName(@PathVariable("name") String name) {
        ProductResponse productResponse = productMapper.productToProductResponse(productService.findByName(name));
        return ResponseEntity.ok(productResponse);
    }

    /**
     * Получение продуктов по их категории
     * @param categoryName Категория продуктов
     * @return Структура ответа с найденными продуктами
     */
    @ApiOperation(value = "${api.swagger.product.category.name}", response = ProductResponse.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductInfoExceptionMessage.UNEXPECTED_ERROR_MSG),
    })
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<?> getProductsByCategoryName(@PathVariable("categoryName") String categoryName) {
        List<ProductResponse> productsByCategory = productService.findByCategoryName(categoryName).stream()
                .map(productMapper::productToProductResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productsByCategory);
    }

    /**
     * Получение продуктов по псевдониму категории
     * @param categoryAlias Псевдоим категории продуктов
     * @return Структура ответа с найденными продуктами
     */
    @ApiOperation(value = "${api.swagger.product.category.alias}", response = ProductResponse.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductInfoExceptionMessage.UNEXPECTED_ERROR_MSG),
    })
    @GetMapping("/category/alias/{categoryAlias}")
    public ResponseEntity<?> getProductsByCategoryAlias(@PathVariable("categoryAlias") String categoryAlias) {
        List<ProductResponse> productsByCategoryAlias = productService.findByCategoryAlias(categoryAlias).stream()
                .map(productMapper::productToProductResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productsByCategoryAlias);
    }

    /**
     * Получение всех возможных продуктов
     * @return Структура ответа со списком продуктов
     */
    @ApiOperation(value = "${api.swagger.product.all}", response = ProductResponse.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductInfoExceptionMessage.UNEXPECTED_ERROR_MSG),
    })
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<ProductResponse> products = productService.findAll().stream()
                .map(productMapper::productToProductResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    /**
     * Получение списка продуктов по их наименованию
     * @param productNames Список наименований продуктов
     * @return Стуктура ответа со найденными продуктами
     */
    @ApiOperation(value = "${api.swagger.product.name-list}", response = ProductResponse.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductInfoExceptionMessage.UNEXPECTED_ERROR_MSG),
    })
    @PostMapping("/predict")
    public ResponseEntity<?> getAllProductsByPredict(@RequestBody List<String> productNames) {
        List<ProductResponse> products = productService.findAllByNames(productNames).stream()
                .map(productMapper::productToProductResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    /**
     * Отправить запрос на парсинг отзывов в системе заново
     * @param productId Id продукта
     * @return Структура ответа с информацией, что запрос был отправлен
     */
    @ApiOperation(value = "${api.swagger.product.parse}", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductInfoExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 404, message = ProductInfoExceptionMessage.PRODUCT_NOT_FOUND_MSG),
    })
    @PostMapping("/parse")
    public ResponseEntity<?> sendDomParserRequest(@RequestBody Long productId) {
        Product product = productService.findById(productId);
        parseRequestService.execute(product);
        log.info("Запрос в review-dom-parser по продукту - {} отправлен", product.getName());
        return new ResponseEntity<String>("Запрос в review-dom-parser отправлен", HttpStatus.ACCEPTED);
    }

    /**
     * Запрос на удаление записи о продукте
     * @param id Id продукта
     * @return Структура ответа с информацией, что запрос на удаление был произведен
     */
    @ApiOperation(value = "${api.swagger.product.delete}", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductInfoExceptionMessage.UNEXPECTED_ERROR_MSG),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteById(id);
        return ResponseEntity.ok(String.format("Продукт c id - %s успешно удален", id));
    }

}
