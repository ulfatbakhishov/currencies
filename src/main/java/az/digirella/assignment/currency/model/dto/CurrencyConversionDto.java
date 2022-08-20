package az.digirella.assignment.currency.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Ulphat
 */
@Getter
@Setter
public class CurrencyConversionDto {

    private LocalDate date;
    private String type;
    private String code;
    private String nominal;
    private String name;
    private BigDecimal value;
}
