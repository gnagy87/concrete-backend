package com.concrete.poletime.user;

import com.concrete.poletime.seasonticket.SeasonTicket;
import com.concrete.poletime.training.Training;
import com.concrete.poletime.utils.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class PoleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false, name = "first_name")
    private String firstName;
    @Column(nullable = false, name = "last_name")
    private String lastName;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "is_enabled")
    private boolean isEnabled;
    @Column(name = "is_guest")
    private boolean isGuest;
    @Basic(optional = false)
    @Column(name = "created_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poleUser", fetch = FetchType.LAZY)
    private Set<SeasonTicket> seasonTickets;
    @ManyToMany
    @JoinTable(
            name = "users_trainings",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id")
    )
    private Set<Training> trainings;
}
