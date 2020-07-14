package com.concrete.poletime.seasonticket;

import com.concrete.poletime.user.PoleUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "season_tickets")
public class SeasonTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "valid_from")
    private LocalDate validFrom;
    @Column(nullable = false, name = "valid_to")
    private LocalDate validTo;
    @Column(nullable = false)
    private int amount;
    @Column(nullable = false)
    private int used;
    @Column(nullable = false, name = "seller_id")
    private Long sellerId;
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private PoleUser poleUser;

    public SeasonTicket(LocalDate validFrom, LocalDate validTo, int amount, Long sellerId, PoleUser poleUser) {
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.amount = amount;
        this.used = 0;
        this.sellerId = sellerId;
        this.poleUser = poleUser;
    }
}
