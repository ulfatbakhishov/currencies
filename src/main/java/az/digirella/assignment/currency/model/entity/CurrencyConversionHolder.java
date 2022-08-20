package az.digirella.assignment.currency.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Ulphat
 */
@Getter
@Setter
@Entity
@Table(name = "CURRENCIES")
public class CurrencyConversionHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String name;
    private String description;

    @OneToMany(mappedBy = "holder", cascade = CascadeType.ALL)
    private List<CurrencyConversion> currencyConversions;

}
