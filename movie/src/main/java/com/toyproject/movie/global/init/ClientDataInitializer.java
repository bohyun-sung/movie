package com.toyproject.movie.global.init;

import com.toyproject.movie.core.domain.client.Client;
import com.toyproject.movie.core.repository.client.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@RequiredArgsConstructor
@Component
public class ClientDataInitializer implements CommandLineRunner {

    private final ClientRepository clientRepository;

    @Override
    public void run(String... args) throws Exception {
        if (clientRepository.count() > 90) {
            return;
        }
        List<Client> clients = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            String email = "test" + i + "@test.com";
            String password = "123!";
            String phone = "010-0000-0000";

            Client client = Client.of(email, password, phone, false);
            clients.add(client);
        }
        if (!clients.isEmpty()) {
            clientRepository.saveAll(clients);
        }
    }
}
