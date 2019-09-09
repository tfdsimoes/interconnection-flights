package com.ryanair.interconnectingflights.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class that handles the bad requests to the service
 *
 * @author tiago.simoes
 * @since 0.0.1
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequest extends Exception {

    public BadRequest(String message) {
        super(message);
    }
}
