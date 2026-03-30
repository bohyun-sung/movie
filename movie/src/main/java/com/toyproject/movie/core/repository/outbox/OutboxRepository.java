package com.toyproject.movie.core.repository.outbox;

import com.toyproject.movie.core.domain.outbox.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {
}
