package com.ryanair.interconnectingflights.utils.flights_schedules.schedule_flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Class that is used to map the answer from the service of get available routes
 * Object inside DaysAnswer
 *
 * @author tiago.simoes
 * @since 0.0.1
 */
@Data
@NoArgsConstructor
public class FlightsAnswer implements Serializable {

    private static final long serialVersionUID = 190456794402900394L;

    @JsonProperty
    private String number;

    @JsonProperty
    private String departureTime;

    @JsonProperty
    private String arrivalTime;
}
