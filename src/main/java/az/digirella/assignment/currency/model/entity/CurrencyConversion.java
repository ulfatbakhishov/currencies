package az.digirella.assignment.currency.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Ulphat
 */
@Getter
@Setter
@Entity
@Table(name = "CURRENCY_TYPES")
public class CurrencyConversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String code;

    private String nominal;

    private String name;

    private BigDecimal value;
    @ManyToOne(optional = false)
    @JoinColumn(name = "currency_conversion_holder")
    private CurrencyConversionHolder holder;

}
