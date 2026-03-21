package com.toyproject.movie.core.domain.client;

import com.toyproject.movie.core.domain.base.DefaultTimeStampCreatedAndModifiedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "client", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_client_email",
                columnNames = {"email"}
        )
})
@Entity
public class Client extends DefaultTimeStampCreatedAndModifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_idx", columnDefinition = "BIGINT UNSIGNED")
    private Long clientIdx;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    private Client(String email, String password, String phone, Boolean isDeleted) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.isDeleted = isDeleted;
    }

    public static Client of(String email, String password, String phone, Boolean isDeleted) {
        return new Client(email, password, phone, isDeleted);
    }
}
