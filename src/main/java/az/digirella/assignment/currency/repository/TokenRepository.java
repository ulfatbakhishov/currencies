package az.digirella.assignment.currency.repository;

import az.digirella.assignment.currency.model.entity.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Ulphat
 */
public interface TokenRepository extends CrudRepository<Token, Long> {

    Optional<Token> findByValue(String value);

    boolean existsByValue(String value);
}
