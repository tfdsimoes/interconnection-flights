package com.ryanair.interconnectingflights.web.rest;

import com.ryanair.interconnectingflights.service.InterconnectionService;
import com.ryanair.interconnectingflights.service.dto.interconnection.InterconnectionAnswerDTO;
import com.ryanair.interconnectingflights.web.rest.errors.BadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class with the controller of interconnection flights
 *
 * @author tiago.simoes
 * @since 0.0.1
 */
@RestController
@RequestMapping("/flights")
public class InterconnectionResource {

    private final Logger log = LoggerFactory.getLogger(InterconnectionResource.class);

    private final InterconnectionService interconnectionService;

    public InterconnectionResource(
            InterconnectionService interconnectionService
    ) {
        this.interconnectionService = interconnectionService;
    }

    /**
     * Controller for get possible flights from point a to b
     *
     * @param departure local of departure
     * @param arrival local of arrival
     * @param departureDateTime departure time
     * @param arrivalDateTime arrival time
     * @return list with the possible flights
     * @throws BadRequest
     */
    @GetMapping(value = "/interconnections", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InterconnectionAnswerDTO>> getInterconnections(
            @RequestParam(value = "departure") String departure,
            @RequestParam(value = "arrival") String arrival,
            @RequestParam(value = "departureDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDateTime,
            @RequestParam(value = "arrivalDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalDateTime
    ) throws BadRequest {
        log.info("Request to get interconnections flight");

        if (departureDateTime.isAfter(arrivalDateTime)) {
            throw new BadRequest("Departure time must be before of arrival time");
        }

        if (departureDateTime.isBefore(LocalDateTime.now()) || arrivalDateTime.isBefore(LocalDateTime.now())) {
            throw new BadRequest("Departure time and arrival time must be in the future");
        }

        return ResponseEntity.ok(interconnectionService.getInterconnectionFlights(departure, arrival, departureDateTime, arrivalDateTime));
    }
}
