package com.ryanair.interconnectingflights.service.dto.interconnection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents the answer for interconnection flights
 *
 * @author tiago.simoes
 * @since 0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterconnectionAnswerDTO {

    @JsonProperty
    private Integer stops;

    @JsonProperty
    private List<InterconnectionLegDTO> legs = new ArrayList<>();
}


