package com.antevorta.model;

import com.antevorta.exception.serverexception.EmptyArbitraryStoreNameException;
import com.antevorta.model.converter.OnlineStoreCredentialsConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Entity
@Table(name = "online_stores")
public class OnlineStoreDetails {
    @Getter
    @EmbeddedId
    private Id id = new Id();

    @Getter
    @Column(nullable = false)
    private String arbitraryStoreName;

    @Getter
    @Setter
    private OnlineStoreType type;

    @Getter
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Convert(converter = OnlineStoreCredentialsConverter.class)
    @Column(columnDefinition = "TEXT")
    private OnlineStoreCredentials credentials;

    @JsonIgnore
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "USER_ID",
            updatable = false, nullable = false
    )
    private User user;

    @SneakyThrows
    public void setArbitraryStoreName(String arbitraryStoreName) {
        if (arbitraryStoreName == null || arbitraryStoreName.isEmpty()) {
            throw new EmptyArbitraryStoreNameException("Arbitrary store name shouldn't be empty");
        }

        this.arbitraryStoreName = arbitraryStoreName;
    }

    public void generateId(Long count) {
        this.id.setOrdinal(count + 1);
        this.id.setEmail(this.user.getEmail());
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Id implements Serializable {
        private Long ordinal;
        @Column(name = "USER_EMAIL")
        private String email;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Id id1 = (Id) o;

            if (!Objects.equals(email, id1.email))
                return false;
            return Objects.equals(ordinal, id1.ordinal);
        }

        @Override
        public int hashCode() {
            int result = email != null ? email.hashCode() : 0;
            result = 31 * result + (ordinal != null ? ordinal.hashCode() : 0);
            return result;
        }
    }
}
