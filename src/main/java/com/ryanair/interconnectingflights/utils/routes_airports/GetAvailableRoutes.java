package com.ryanair.interconnectingflights.utils.routes_airports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class that handles the request to endpoint to get available routes
 *
 * @author tiago.simoes
 * @since 0.0.1
 */
@Component
public class GetAvailableRoutes {

    private final Logger log = LoggerFactory.getLogger(GetAvailableRoutes.class);

    @Value("${ryanair.routes.available.url}")
    private String url;

    @Value("${ryanair.operator.code}")
    private String operatorCode;

    /**
     * Function to get available routes from ryanair
     *
     * @param departure local of departure
     * @param arrival local of arrival
     * @return list of routes filtered
     */
    public List<AvailableRouteAnswer> getRoutesAvailable(String departure, String arrival) {
        log.info("Get available routes from ryanair");

        List<AvailableRouteAnswer> availableRouteAnswers = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AvailableRouteAnswer[]> response = restTemplate.getForEntity(url, AvailableRouteAnswer[].class);

        if(response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException("Error getting routes available");
        }

        if(Optional.ofNullable(response.getBody()).isPresent()){
            availableRouteAnswers =  Arrays.stream(response.getBody())
                    .filter(route -> !Optional.ofNullable(route.getConnectionAirport()).isPresent() && route.getOperator().equals(operatorCode) &&
                            (route.getAirportFrom().equals(departure) || route.getAirportTo().equals(departure) || route.getAirportTo().equals(arrival)))
                    .collect(Collectors.toList());
        }

        return availableRouteAnswers;
    }
}
