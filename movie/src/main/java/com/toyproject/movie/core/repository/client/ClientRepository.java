package com.toyproject.movie.core.repository.client;

import com.toyproject.movie.core.domain.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
