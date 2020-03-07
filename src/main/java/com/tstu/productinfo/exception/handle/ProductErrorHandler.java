package com.tstu.productinfo.exception.handle;

import com.tstu.commons.dto.http.response.error.ErrorField;
import com.tstu.commons.dto.http.response.error.ErrorResponse;
import com.tstu.commons.dto.http.response.error.ErrorValidationResponse;
import com.tstu.commons.exception.handle.PrsErrorHandler;
import com.tstu.productinfo.exception.ProductInfoErrors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ProductErrorHandler extends PrsErrorHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .id(UUID.randomUUID().toString())
                .time(LocalDateTime.now())
                .code(ProductInfoErrors.JSON_NOT_READABLE.name())
                .message(ex.getMessage())
                .displayMessage(ProductInfoErrors.JSON_NOT_READABLE.getErrorDescription())
                .techInfo(null)
                .build();
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldError = ex.getBindingResult().getFieldErrors();
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        List<String> allErrorsMessages = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

        //Получаем список ошибок которые относятся к валидации полей
        List<ErrorField> errorFields = fieldError.stream().map((fe) -> new ErrorField(fe.getField(), fe.getDefaultMessage())).collect(Collectors.toList());
        //Формируем все другие ошибки которые не соотвествуют уже существующим ошибкам полей
        List<String> anotherErrors = allErrorsMessages.stream().filter(error -> errorFields.stream().map(ErrorField::getMessage).noneMatch(field -> field.equals(error))).collect(Collectors.toList());
        //Добавляем остальне ошибки в общий список ошибок полей
        errorFields.addAll(anotherErrors.stream().map(error -> new ErrorField(null, error)).collect(Collectors.toList()));

        ErrorValidationResponse errorValidationResponse = new ErrorValidationResponse(errorFields);
        return this.handleExceptionInternal(ex, errorValidationResponse, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
}
