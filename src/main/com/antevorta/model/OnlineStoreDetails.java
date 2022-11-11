package com.antevorta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "online_stores")
public class OnlineStoreDetails {
    @EmbeddedId
    private Id id = new Id();
    @Column(nullable = false)
    private String arbitraryStoreName;
    private OnlineStoreType type;
    private String storeName;
    private String accessKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "USER_EMAIL",
            insertable = false, updatable = false, nullable = false
    )
    private User user;

    public OnlineStoreDetails(Id id, String arbitraryStoreName, OnlineStoreType type) {
        this.id = id;
        this.arbitraryStoreName = arbitraryStoreName;
        this.type = type;
    }

    public void generateId(Long count) {
        this.id.setOrdinal(count + 1);
        this.id.setEmail(this.user.getEmail());
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
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
