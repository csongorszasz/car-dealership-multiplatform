package edu.bbte.idde.scim2304.spring.controlleradvice;

import edu.bbte.idde.scim2304.spring.service.exceptions.FailedCreateException;
import edu.bbte.idde.scim2304.spring.service.exceptions.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestControllerAdvice
@Slf4j
public class ValidationErrorHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final Stream<String> handleConstraintViolation(ConstraintViolationException e) {
        log.debug("Constraint violation", e);
        return e.getConstraintViolations().stream()
                .map(it -> it.getPropertyPath().toString() + " " + it.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final Stream<String> handleMethodArgumentsNotValidException(MethodArgumentNotValidException e) {
        log.warn("Invalid arguments", e);
        return e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final Stream<String> handleNotFoundException(NotFoundException e) {
        return Stream.of("Entity not found");
    }

    @ExceptionHandler(FailedCreateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Stream<String> handleFailedCreateException(FailedCreateException e) {
        return Stream.of("Failed to create entity");
    }
}
