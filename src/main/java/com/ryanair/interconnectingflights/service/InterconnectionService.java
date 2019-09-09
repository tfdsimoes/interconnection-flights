package com.ryanair.interconnectingflights.service;

import com.ryanair.interconnectingflights.service.dto.interconnection.InterconnectionAnswerDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface to interconnectionService
 *
 * @author tiago.simoes
 * @since 0.0.1
 */
public interface InterconnectionService {

    /**
     * Interface to calculate the interconnection flights
     *
     * @param departure local of departure
     * @param arrival local of arrival
     * @param departureDateTime datetime of departure
     * @param arrivalDateTime datetime of arrival
     * @return list of possible flights
     */
    public List<InterconnectionAnswerDTO> getInterconnectionFlights(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime);
}
