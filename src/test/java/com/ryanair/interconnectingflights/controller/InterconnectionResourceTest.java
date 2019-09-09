package com.ryanair.interconnectingflights.controller;

import com.ryanair.interconnectingflights.ApplicationConfig;
import com.ryanair.interconnectingflights.service.InterconnectionService;
import com.ryanair.interconnectingflights.service.dto.interconnection.InterconnectionAnswerDTO;
import com.ryanair.interconnectingflights.web.rest.InterconnectionResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class with unit tests to the controller of InterconnectionResource
 *
 * @author tiago.simoes
 */
@Import({ApplicationConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(InterconnectionResource.class)
@ContextConfiguration
public class InterconnectionResourceTest {

    @Autowired
    private InterconnectionService interconnectionService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {

    }

    /**
     * Missing parameters test
     *
     * @throws Exception
     */
    @Test
    public void getInterconnectionsMissingFields() throws Exception{
        // Given

        // When

        // Then
        mockMvc.perform(get("/flights/interconnections"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    /**
     * Test to check the validation of the dates
     *
     * @throws Exception
     */
    @Test
    public void getInterconnectionsBadDates() throws Exception{
        // Given

        // When

        // Then
        mockMvc.perform(get("/flights/interconnections?departure=DUB&arrival=WRO&departureDateTime={0}&arrivalDateTime={1}", LocalDateTime.now().plusHours(-1).format(DateTimeFormatter.ISO_DATE_TIME), LocalDateTime.now().plusDays(-1).format(DateTimeFormatter.ISO_DATE_TIME)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    /**
     * Test that runs everything ok
     *
     * @throws Exception
     */
    @Test
    public void getInterconnectionsSuccess() throws Exception {
        // Given
        InterconnectionAnswerDTO interconnectionAnswerDTO = InterconnectionAnswerDTO.builder()
                .stops(1)
                .legs(new ArrayList<>())
                .build();

        List<InterconnectionAnswerDTO> answer = Collections.singletonList(interconnectionAnswerDTO);


        // When
        when(interconnectionService.getInterconnectionFlights(any(), any(), any(), any())).thenReturn(answer);

        // Then
        mockMvc.perform(get("/flights/interconnections?departure=DUB&arrival=WRO&departureDateTime={0}&arrivalDateTime={1}", LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ISO_DATE_TIME), LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ISO_DATE_TIME)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].stops").value(interconnectionAnswerDTO.getStops()));
    }
}
