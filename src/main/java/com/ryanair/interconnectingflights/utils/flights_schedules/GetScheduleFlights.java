package com.ryanair.interconnectingflights.utils.flights_schedules;

import com.ryanair.interconnectingflights.utils.flights_schedules.schedule_flights.ScheduleFlightsAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * Class that handles the process to get the flights schedules
 *
 * @author tiago.simoes
 * @since 0.0.1
 */
@Component
public class GetScheduleFlights {

    private final Logger log = LoggerFactory.getLogger(GetScheduleFlights.class);

    @Value("${ryanair.schedules.flights.url}")
    private String url;

    /**
     * Function to retrieve the schedules flight between airports
     *
     * @param departure departure location
     * @param arrival arrival location
     * @param year year of the flight
     * @param month month of the flight
     * @return list of schedule flights
     */
    public ScheduleFlightsAnswer getScheduleFlights(String departure, String arrival, Integer year, Integer month) {
        // log.info("Get schedules of airport from {} to {} with year {} and month {}", departure, arrival, year, month);

        RestTemplate restTemplate = new RestTemplate();
        String finalUrl = url + "/" + departure + "/" + arrival + "/years/" + year + "/months/" + month;

        ResponseEntity<ScheduleFlightsAnswer> response;
        try {
            response = restTemplate.getForEntity(finalUrl, ScheduleFlightsAnswer.class);
        } catch (HttpClientErrorException exception) {
            // Some times there is resource not found, airports connection not found, these way the program will progress ignoring the ones with problem
            log.info("Get schedules of airport from {} to {} with year {} and month {} problem getting resource", departure, arrival, year, month);
            ScheduleFlightsAnswer scheduleFlightsAnswer = new ScheduleFlightsAnswer();
            scheduleFlightsAnswer.setMonth(0);
            scheduleFlightsAnswer.setDays(new ArrayList<>());
            return  scheduleFlightsAnswer;
        }


        if(response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException("Error getting routes available");
        }

        return response.getBody();
    }
}
