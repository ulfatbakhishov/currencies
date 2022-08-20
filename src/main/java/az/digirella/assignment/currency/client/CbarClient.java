package az.digirella.assignment.currency.client;

import az.digirella.assignment.currency.model.dto.CbarCurrencyConversionsDto;
import az.digirella.assignment.currency.utils.AppConstants;
import feign.Logger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

/**
 * @author Ulphat
 */
@FeignClient(name = "cbar", url = "${feign.client.config.cbar.url}", configuration = CbarClient.Config.class)
public interface CbarClient {


    @GetMapping(value = "/{date}.xml", produces = MediaType.APPLICATION_XML_VALUE)
    CbarCurrencyConversionsDto collect(
            @PathVariable @DateTimeFormat(pattern = AppConstants.PATTERN_DATE) LocalDate date);

    @Configuration
    class Config {

        @Bean
        Logger.Level partsBaseLoggerLevel() {
            return Logger.Level.FULL;
        }
    }
}
