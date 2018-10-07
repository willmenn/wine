package io.wine.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@ResponseStatus(NO_CONTENT)
public class DoNotHaveStockException extends RuntimeException {
}
