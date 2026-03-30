package com.toyproject.movie.core.service.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyproject.movie.api.dto.outbox.OutboxEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OutboxConsumerService {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "movie-cdc.public.outbox")
    public void consumeOutbox(String message) {
        log.info("#### Received CDC Message: {}", message);
        try {

            OutboxEventDto event = objectMapper.readValue(message, OutboxEventDto.class);
            // Insert(c) 혹은 Update(u)인 경우 'after' 데이터를 처리
            if ("c".equals(event.getOp()) || "u".equals(event.getOp())) {
                JsonNode afterData = event.getAfter();

                String aggregateType = afterData.get("aggregate_type").asText();
                String payloadJson = afterData.get("payload").asText();

                log.info("Processing Event - Type: {}, Payload: {}", aggregateType, payloadJson);

                // 비즈니스 로직 실행
                processBusinessLogic(aggregateType, payloadJson);
            }
        } catch (Exception e) {
            log.error("Error parsing CDC message", e);
        }
    }

    private void processBusinessLogic(String aggregateType, String payloadJson) {
    }
}
