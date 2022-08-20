package az.digirella.assignment.currency.repository;

import az.digirella.assignment.currency.model.entity.CurrencyConversion;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

/**
 * @author Ulphat
 */
public interface CurrencyConversionRepository extends JpaRepositoryImplementation<CurrencyConversion, Long> {
}
