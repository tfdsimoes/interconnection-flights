package com.ryanair.interconnectingflights.utils.flights_schedules.schedule_flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Class that is used to map the answer from the service of get available routes
 * Object inside ScheduleFlightsAnswer
 *
 * @author tiago.simoes
 * @since 0.0.1
 */
@Data
@NoArgsConstructor
public class DaysAnswer implements Serializable {

    private static final long serialVersionUID = 1137207817679375815L;
    @JsonProperty
    private Integer day;

    @JsonProperty
    private List<FlightsAnswer> flights;
}
