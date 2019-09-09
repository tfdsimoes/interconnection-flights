package com.ryanair.interconnectingflights;

import com.ryanair.interconnectingflights.service.InterconnectionService;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class ApplicationConfig {

    @Bean
    public InterconnectionService interconnectionService() {
        return mock(InterconnectionService.class);
    }
}
