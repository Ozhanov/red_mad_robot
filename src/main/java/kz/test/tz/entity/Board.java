package kz.test.tz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@Table(name = "boards")
@Getter
@Setter
public class Board extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "min_price", nullable = false)
    private Long minPrice;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image")
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "closed_date_time")
    private LocalDateTime closedDateTime;

    @Column(name = "client_id")
    private Long clientId;

    public enum Status {
        ACTIVE, REMOVED_PUBLICATION
    }
}
