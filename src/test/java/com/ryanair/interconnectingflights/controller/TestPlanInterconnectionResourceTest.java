package com.ryanair.interconnectingflights.controller;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the endpoints in AdsResource
 *
 * @author tiago.simoes
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestPlanInterconnectionResourceTest {

    private static Logger log = LoggerFactory.getLogger(TestPlanInterconnectionResourceTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting( Description description ) {
            log.info("Running the integration Test --> " + description.getMethodName());
        }
    };

    /**
     * Missing all the fields
     */
    @Test
    public void getInterconnectionsMissingFields() {
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/flights/interconnections", HttpMethod.GET, null, new ParameterizedTypeReference<Void>() {});

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    /**
     * The dates are before the current one
     */
    @Test
    public void getInterconnectionDatesBeforeNow() {
        String urlFinal = String.format("/flights/interconnections?departureDateTime=%s&arrivalDateTime=%s", LocalDateTime.now().plusHours(-1).format(DateTimeFormatter.ISO_DATE_TIME), LocalDateTime.now().plusDays(-1).format(DateTimeFormatter.ISO_DATE_TIME));

        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + urlFinal, HttpMethod.GET, null, new ParameterizedTypeReference<Void>() {});

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    /**
     * Departure time after arrival time
     */
    @Test
    public void getInterconnectionArrivalBeforeDeparture() {
        String urlFinal = String.format("/flights/interconnections?departureDateTime=%s&arrivalDateTime=%s", LocalDateTime.now().plusDays(3).format(DateTimeFormatter.ISO_DATE_TIME), LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ISO_DATE_TIME));

        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + urlFinal, HttpMethod.GET, null, new ParameterizedTypeReference<Void>() {});

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
