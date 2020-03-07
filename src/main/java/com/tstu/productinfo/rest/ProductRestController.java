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
    @ApiOperation(value = "${ProductRestController.create}")
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
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        ProductResponse productResponse = productMapper.productToProductResponse(productService.findById(id));
        return ResponseEntity.ok(productResponse);
    }

    /**
     * Получение продуктов по их категории
     * @param categoryName Категория продуктов
     * @return Структура ответа с найденными продуктами
     */
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<?> getProductByCategoryName(@PathVariable("categoryName") String categoryName) {
        List<ProductResponse> productsByCategory = productService.findByCategoryName(categoryName).stream()
                .map(productMapper::productToProductResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productsByCategory);
    }

    /**
     * Получение всех возможных продуктов
     * @return Структура ответа со списком продуктов
     */
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
    @PostMapping("/parse")
    public ResponseEntity<?> sendDomParserRequest(@RequestBody Long productId) {
        Product product = productService.findById(productId);
        parseRequestService.execute(product);
        log.info("Запрос в review-dom-parser по продукту - {} отправлен", product.getName());
        return new ResponseEntity<String>("Запрос отправлен", HttpStatus.ACCEPTED);
    }

    /**
     * Запрос на удаление записи о продукте
     * @param id Id продукта
     * @return Структура ответа с информацией, что запрос на удаление был произведен
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteById(id);
        return ResponseEntity.ok("Запись удалена");
    }

}
