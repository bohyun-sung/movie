package com.toyproject.movie.core.service.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyproject.movie.core.domain.outbox.Outbox;
import com.toyproject.movie.core.repository.outbox.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.MANDATORY) // 반드시 기존 트랜잭션 내에서 실행
    public void saveEvent(String type, Long id, String eventName, Object payloadDto) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(payloadDto);
            Outbox outbox = Outbox.of(type, id, eventName, jsonPayload);
            outboxRepository.save(outbox);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("이벤트 직렬화 실패: " + eventName, e);
        }
    }
}
