package az.digirella.assignment.currency.model.mapper;

import az.digirella.assignment.currency.model.dto.Valute;
import az.digirella.assignment.currency.model.dto.ValType;
import az.digirella.assignment.currency.model.dto.CbarCurrencyConversionsDto;
import az.digirella.assignment.currency.model.dto.CurrencyConversionDto;
import az.digirella.assignment.currency.model.entity.CurrencyConversion;
import az.digirella.assignment.currency.model.entity.CurrencyConversionHolder;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Ulphat
 */
@Mapper
public interface CurrencyMapper {
    @Mapping(target = "id", ignore = true)
    CurrencyConversionHolder toEntity(CbarCurrencyConversionsDto dto);

    @AfterMapping
    default void afterEntity(@MappingTarget CurrencyConversionHolder entity,
                             CbarCurrencyConversionsDto dto) {
        if (entity == null) return;
        Optional.ofNullable(dto)
                .map(CbarCurrencyConversionsDto::getValTypes)
                .map(dtos -> getCurrencyConversionList(entity, dtos))
                .ifPresent(entity::setCurrencyConversions);
    }

    private List<CurrencyConversion> getCurrencyConversionList(CurrencyConversionHolder entity,
                                                               ValType[] dtos) {
//        List<CurrencyConversion> list = new LinkedList<>();
//        for (CbarCurrencyConversionsByTypeDto dto : dtos) {
//            List<CbarCurrencyConversionDto> currencyList = dto.getCurrencyList();
//            for (CbarCurrencyConversionDto conversion : currencyList) {
//                CurrencyConversion currencyConversion = toEntity(entity, dto.getType(), conversion);
//                list.add(currencyConversion);
//            }
//        }
//        return list;
        return Arrays.stream(dtos)
                          .flatMap(dto -> {
                       return Optional.of(dto)
                                      .stream()
                                      .map(ValType::getValutes)
                                      .flatMap(Arrays::stream)
                                      .map(conversion -> toEntity(entity, dto.getType(), conversion));
                   })
                          .collect(Collectors.toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "holder", source = "entity")
    @Mapping(target = "name", source = "dto.name")
    CurrencyConversion toEntity(CurrencyConversionHolder entity, String type, Valute dto);

    @Mapping(target = "date", source = "holder.date")
    CurrencyConversionDto toDto(CurrencyConversion entity);
}
