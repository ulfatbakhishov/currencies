package az.digirella.assignment.currency.repository;

import az.digirella.assignment.currency.model.entity.CurrencyConversionHolder;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

/**
 * @author Ulphat
 */
public interface CurrencyConversionHolderRepository extends CrudRepository<CurrencyConversionHolder, Long> {

    boolean existsByDate(LocalDate date);

    void deleteByDate(LocalDate date);
}
