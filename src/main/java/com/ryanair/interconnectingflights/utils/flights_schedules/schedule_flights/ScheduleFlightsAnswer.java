package com.ryanair.interconnectingflights.utils.flights_schedules.schedule_flights;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Class that is used to map the answer from the service of get flight schedules
 *
 * @author tiago.simoes
 * @since 0.0.1
 */
@Data
@NoArgsConstructor
public class ScheduleFlightsAnswer implements Serializable {

    private static final long serialVersionUID = 3771144204891058012L;

    @JsonProperty
    private Integer month;

    @JsonProperty
    private List<DaysAnswer> days;
}
