package com.concrete.poletime.seasonticket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private Date validFrom;
    @Column(nullable = false, name = "valid_to")
    private Date validTo;
    @Column(nullable = false)
    private int amount;
    @Column(nullable = false, name = "seller_id")
    private Long sellerId;
}