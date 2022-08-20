package az.digirella.assignment.currency.model.dto;

import az.digirella.assignment.currency.utils.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Ulphat
 */
@Getter
@Setter
@JacksonXmlRootElement(localName = "ValCurs")
public class CbarCurrencyConversionsDto {

    @JacksonXmlProperty(localName = "Date", isAttribute = true)
    @JsonFormat(pattern = AppConstants.PATTERN_DATE)
    private LocalDate date;

    @JacksonXmlProperty(localName = "Name", isAttribute = true)
    private String name;

    @JacksonXmlProperty(localName = "Description", isAttribute = true)
    private String description;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "ValType")
    private ValType[] valTypes;
}
