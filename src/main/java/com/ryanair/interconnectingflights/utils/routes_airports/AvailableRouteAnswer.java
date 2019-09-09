package com.ryanair.interconnectingflights.utils.routes_airports;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Class that is used to map the answer from the service of get available routes
 *
 * @author tiago.simoes
 * @since 0.0.1
 */
@Data
@NoArgsConstructor
public class AvailableRouteAnswer implements Serializable {

    private static final long serialVersionUID = 1264657656080516596L;

    @JsonProperty
    private String airportFrom;

    @JsonProperty
    private String airportTo;

    @JsonProperty
    private String connectionAirport;

    @JsonProperty
    private Boolean newRoute;

    @JsonProperty
    private Boolean seasonalRoute;

    @JsonProperty
    private String operator;

    @JsonProperty
    private String group;
}
