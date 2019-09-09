package com.ryanair.interconnectingflights.service.dto.interconnection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


/**
 * Class that represents an object inside the answer for interconnection flights
 *
 * @author tiago.simoes
 * @since 0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterconnectionLegDTO {

    @JsonProperty
    private String departureAirport;

    @JsonProperty
    private String arrivalAirport;

    @JsonProperty
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime departureDateTime;

    @JsonProperty
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime arrivalDateTime;
}
