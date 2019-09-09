package com.ryanair.interconnectingflights.service;

import com.ryanair.interconnectingflights.service.dto.interconnection.InterconnectionAnswerDTO;
import com.ryanair.interconnectingflights.service.dto.interconnection.InterconnectionLegDTO;
import com.ryanair.interconnectingflights.utils.flights_schedules.GetScheduleFlights;
import com.ryanair.interconnectingflights.utils.flights_schedules.schedule_flights.DaysAnswer;
import com.ryanair.interconnectingflights.utils.flights_schedules.schedule_flights.FlightsAnswer;
import com.ryanair.interconnectingflights.utils.routes_airports.AvailableRouteAnswer;
import com.ryanair.interconnectingflights.utils.routes_airports.GetAvailableRoutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class with services for the resource interconnection flights
 *
 * @author tiago.simoes
 * @since 0.0.1
 */
@Service
public class InterconnectionServiceImpl implements InterconnectionService {
    private final Logger log = LoggerFactory.getLogger(InterconnectionServiceImpl.class);

    @Value("${wait.time.interconnection.flights}")
    private Integer waitTimeInterconnection;

    private final GetAvailableRoutes getAvailableRoutes;

    private final GetScheduleFlights getScheduleFlights;

    public InterconnectionServiceImpl(
            GetAvailableRoutes getAvailableRoutes,
            GetScheduleFlights getScheduleFlights
    ) {
        this.getAvailableRoutes = getAvailableRoutes;
        this.getScheduleFlights = getScheduleFlights;
    }

    /**
     * Service to calculate the interconnection flights
     *
     * @param departure local of departure
     * @param arrival local of arrival
     * @param departureDateTime datetime of departure
     * @param arrivalDateTime datetime of arrival
     * @return list of possible flights
     */
    public List<InterconnectionAnswerDTO> getInterconnectionFlights(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        log.info("Get interconnection flights");

        List<AvailableRouteAnswer> routes = getAvailableRoutes.getRoutesAvailable(departure, arrival);

        List<List<String>> routesAvailable = buildRoutes(routes, departure, arrival);

        return buildSchedulesAnswer(departure, arrival, routesAvailable, departureDateTime, arrivalDateTime);
    }

    /**
     * Function that will build direct and indirect routes
     *
     * @param routes list of flights available
     * @param departure departure location
     * @param arrival arrival location
     * @return list with all routes and airports
     */
    private List<List<String>> buildRoutes(List<AvailableRouteAnswer> routes, String departure, String arrival) {

        // Get direct routes (if available)
        List<AvailableRouteAnswer> directRoutes = routes.stream()
                .filter(route -> route.getAirportFrom().equals(departure) && route.getAirportTo().equals(arrival))
                .collect(Collectors.toList());

        List<List<String>> directRoutesAirport = directRoutes.stream()
                .map(route -> {
                    List<String> newRoute = new ArrayList<>();
                    newRoute.add(route.getAirportFrom());
                    newRoute.add(route.getAirportTo());
                    return newRoute;
                }).collect(Collectors.toList());

        routes.removeAll(directRoutes);

        // Get indirect routes with 1 stop
        List<AvailableRouteAnswer> routesDeparture = routes.stream().filter(route -> route.getAirportFrom().equals(departure)).collect(Collectors.toList());
        List<AvailableRouteAnswer> routesArrival = routes.stream().filter(route -> route.getAirportTo().equals(arrival)).collect(Collectors.toList());

        List<String> listConnectionDepartureAirports = routesDeparture.stream().map(AvailableRouteAnswer::getAirportTo).collect(Collectors.toList());
        List<String> listConnectionArrivalAirports = routesArrival.stream().map(AvailableRouteAnswer::getAirportFrom).collect(Collectors.toList());

        List<String> listConnectionAirport = listConnectionDepartureAirports.stream().filter(listConnectionArrivalAirports::contains).collect(Collectors.toList());

        routesDeparture = routesDeparture.stream().filter(route -> listConnectionAirport.contains(route.getAirportTo())).collect(Collectors.toList());
        routesArrival = routesArrival.stream().filter(route -> listConnectionAirport.contains(route.getAirportFrom())).collect(Collectors.toList());

        List<List<String>> indirectRoutesAirport = new ArrayList<>();
        for(AvailableRouteAnswer routeDeparture : routesDeparture) {
            List<String> connection = new ArrayList<>();
            for(AvailableRouteAnswer routeArrival : routesArrival) {
                if (routeDeparture.getAirportTo().equals(routeArrival.getAirportFrom())) {
                    connection.add(routeDeparture.getAirportFrom());
                    connection.add(routeDeparture.getAirportTo());
                    connection.add(routeArrival.getAirportTo());
                }
            }
            indirectRoutesAirport.add(connection);
        }

        return Stream.concat(directRoutesAirport.stream(), indirectRoutesAirport.stream()).collect(Collectors.toList());
    }

    /**
     * Function that will start the calculation of scheduled flights
     *
     * @param startPlace start place of travel
     * @param finalDestination final destination of travel
     * @param routesAvailable list of routes available
     * @param departureDateTime departure time
     * @param arrivalDateTime max arrival time
     * @return list with all possible schedules
     */
    private List<InterconnectionAnswerDTO> buildSchedulesAnswer(String startPlace, String finalDestination, List<List<String>> routesAvailable, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        List<InterconnectionAnswerDTO> interconnectionAnswerDTOS = new ArrayList<>();

        for(List<String> airportsFlight : routesAvailable) {
            InterconnectionAnswerDTO interconnectionAnswerDTO = new InterconnectionAnswerDTO();
            interconnectionAnswerDTO.setStops(airportsFlight.size() - 2);

            interconnectionAnswerDTOS.addAll(calculateSchedulesFlights(startPlace, finalDestination, airportsFlight, interconnectionAnswerDTO, departureDateTime, arrivalDateTime));
        }

        return interconnectionAnswerDTOS;
    }

    /**
     * Recursive method that will calculate the schedules of all flights given a list of places to pass
     *
     * @param startPlace starting point
     * @param finalDestination final point
     * @param airportsFlights the airports to pass
     * @param interconnectionAnswerDTO the object that pass between recursive method
     * @param departureDateTime departure time
     * @param arrivalDateTime arrival time
     * @return list of flights
     */
    private List<InterconnectionAnswerDTO> calculateSchedulesFlights(String startPlace, String finalDestination, List<String> airportsFlights, InterconnectionAnswerDTO interconnectionAnswerDTO, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        List<InterconnectionAnswerDTO> interconnectionAnswerDTOS = new ArrayList<>();
        long totalMonthsSearch = departureDateTime.withDayOfMonth(1).until(arrivalDateTime.withDayOfMonth(2), ChronoUnit.MONTHS);

        // Navigate all airports of the flight
        for(int i = 0, q = 1; q < airportsFlights.size(); i++, q++) {
            HashMap<String, List<DaysAnswer>> flightsSchedules = new HashMap<>();

            String fromAirport = airportsFlights.get(i);
            String toAirport = airportsFlights.get(q);

            // Get all flights from departure time to arrival time
            for(int auxMonths = 0; auxMonths <= totalMonthsSearch; auxMonths++) {
                int year = departureDateTime.plusMonths(auxMonths).getYear();
                int month = departureDateTime.plusMonths(auxMonths).getMonth().getValue();

                flightsSchedules.put(year + "." + String.format("%02d", month), getScheduleFlights.getScheduleFlights(fromAirport, toAirport, year, month).getDays());
            }

            // Check the flights for the month
            for(Map.Entry<String, List<DaysAnswer>> flightsInMonth : flightsSchedules.entrySet()) {
                String key = flightsInMonth.getKey();
                int year = Integer.parseInt(key.substring(0, key.indexOf(".")));
                int month = Integer.parseInt(key.substring(key.indexOf(".") + 1));

                List<DaysAnswer> flightsDay = flightsInMonth.getValue();

                // Check flights in the day
                for(DaysAnswer daysFlights : flightsDay) {
                    int day = daysFlights.getDay();

                    // Check flights
                    for (FlightsAnswer flight : daysFlights.getFlights()) {
                        String departureTimeString = flight.getDepartureTime();
                        int departureTimeHour = Integer.parseInt(departureTimeString.substring(0, departureTimeString.indexOf(":")));
                        int departureTimeMinute = Integer.parseInt(departureTimeString.substring(departureTimeString.indexOf(":") + 1));

                        String arrivalTimeString = flight.getArrivalTime();
                        int arrivalTimeHour = Integer.parseInt(arrivalTimeString.substring(0, arrivalTimeString.indexOf(":")));
                        int arrivalTimeMinute = Integer.parseInt(arrivalTimeString.substring(arrivalTimeString.indexOf(":") + 1));

                        LocalDateTime departureAirportTime = LocalDateTime.of(year, month, day, departureTimeHour, departureTimeMinute);
                        LocalDateTime arrivalAirportTime = LocalDateTime.of(year, month, day, arrivalTimeHour, arrivalTimeMinute);

                        // Check if is valid route in times
                        if (departureAirportTime.isAfter(departureDateTime) && arrivalAirportTime.isBefore(arrivalDateTime)) {
                            InterconnectionLegDTO interconnectionLegDTO = new InterconnectionLegDTO(fromAirport, toAirport, departureAirportTime, arrivalAirportTime);

                            List<InterconnectionLegDTO> temp = new ArrayList<>(interconnectionAnswerDTO.getLegs());
                            temp.add(interconnectionLegDTO);

                            InterconnectionAnswerDTO interconnectionAnswerDTOAux = new InterconnectionAnswerDTO(interconnectionAnswerDTO.getStops(), new ArrayList<>(temp));

                            // Check if is valid route from start point to final destination
                            if(finalDestination.equals(toAirport) && interconnectionAnswerDTOAux.getLegs().get(0).getDepartureAirport().equals(startPlace)) {
                                interconnectionAnswerDTOS.add(interconnectionAnswerDTOAux);
                            } else {
                                List<String> tempAirports = new ArrayList<>(airportsFlights);
                                tempAirports.remove(0);
                                interconnectionAnswerDTOS.addAll(calculateSchedulesFlights(startPlace, finalDestination, tempAirports, interconnectionAnswerDTOAux, arrivalAirportTime.plusHours(2L), arrivalDateTime));
                            }
                        }
                    }
                }
            }
        }

        return interconnectionAnswerDTOS;
    }
}
