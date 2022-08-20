package az.digirella.assignment.currency.exception;

import az.digirella.assignment.currency.model.dto.CollectResultDto;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Ulphat
 */
@RestControllerAdvice
public class FeignExceptionHandler {

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.OK)
    public CollectResultDto handleFeignException(FeignException e) {
        if (e.status() == HttpStatus.FOUND.value()) {
            return CollectResultDto.notFound();
        }
        return CollectResultDto.externalException(e.getMessage());
    }
}
