package com.toyproject.movie.api.dto.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OutboxEventDto {
    private JsonNode before;
    private JsonNode after;
    private JsonNode source;
    private String op; // c: create, u: update, d: delete
    private Long ts_ms;
}