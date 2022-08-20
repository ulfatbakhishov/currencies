package az.digirella.assignment.currency.controllers;

import az.digirella.assignment.currency.model.dto.CollectResultDto;
import az.digirella.assignment.currency.model.dto.CurrencyConversionDto;
import az.digirella.assignment.currency.model.entity.CurrencyConversion;
import az.digirella.assignment.currency.service.CollectorService;
import az.digirella.assignment.currency.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.EqualIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * @author Ulphat
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/currencies")
public class CurrencyController {

    private final CollectorService service;

    @GetMapping("/{date}.json")
    @PreAuthorize("hasAuthority(\"TOKEN\")")
    public CollectResultDto collect(@PathVariable @DateTimeFormat(pattern = AppConstants.PATTERN_DATE) LocalDate date) {
        return service.collect(date);
    }

    @DeleteMapping("/{date}.json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority(\"TOKEN\")")
    public void delete(@PathVariable @DateTimeFormat(pattern = AppConstants.PATTERN_DATE) LocalDate date) {
        service.delete(date);
    }

    @GetMapping
    @PreAuthorize("hasAuthority(\"BASIC\")")
    public Page<CurrencyConversionDto> list(
            @And({@Spec(path = "holder.date", params = "date", config = AppConstants.PATTERN_DATE, spec = Equal.class),
                  @Spec(path = "code", spec = EqualIgnoreCase.class)}) Specification<CurrencyConversion> spec,
            Pageable pageable) {
        return service.list(spec, pageable);
    }
}
