package az.digirella.assignment.currency.service;

import az.digirella.assignment.currency.client.CbarClient;
import az.digirella.assignment.currency.exception.NoDataFoundException;
import az.digirella.assignment.currency.model.dto.CollectResultDto;
import az.digirella.assignment.currency.model.dto.CurrencyConversionDto;
import az.digirella.assignment.currency.model.entity.CurrencyConversion;
import az.digirella.assignment.currency.model.mapper.CurrencyMapper;
import az.digirella.assignment.currency.repository.CurrencyConversionHolderRepository;
import az.digirella.assignment.currency.repository.CurrencyConversionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

/**
 * @author Ulphat
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CollectorService {

    private final CurrencyConversionHolderRepository holderRepository;
    private final CurrencyConversionRepository conversionRepository;
    private final CbarClient client;
    private final CurrencyMapper mapper;

    public CollectResultDto collect(LocalDate localDate) {
        return Optional.of(localDate)
                       .filter(date -> !holderRepository.existsByDate(date))
                       .map(this::collectInternal)
                       .orElseGet(CollectResultDto::exists);
    }

    private CollectResultDto collectInternal(LocalDate localDate) {
        return Optional.of(localDate)
                       .map(client::collect)
                       .filter(dto -> dto.getDate().equals(localDate))
                       .map(mapper::toEntity)
                       .map(holderRepository::save)
                       .map(o -> CollectResultDto.success())
                       .orElseGet(CollectResultDto::notFound);
    }

    public void delete(LocalDate date) {
        if (holderRepository.existsByDate(date))
            holderRepository.deleteByDate(date);
        else throw new NoDataFoundException();
    }

    public Page<CurrencyConversionDto> list(Specification<CurrencyConversion> spec, Pageable pageable) {
        return conversionRepository.findAll(spec, pageable)
                                   .map(mapper::toDto);
    }
}
