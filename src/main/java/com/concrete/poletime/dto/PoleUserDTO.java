package com.concrete.poletime.dto;

import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.utils.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PoleUserDTO {
    private Long id;
    private String email;
    private String firsName;
    private String lastName;
    private Role role;
    private SeasonTicketDTO currentSeasonTicket;

    public PoleUserDTO(PoleUser poleUser) {
        this.id = poleUser.getId();
        this.email = poleUser.getEmail();
        this.firsName = poleUser.getFirstName();
        this.lastName = poleUser.getLastName();
        this.role = poleUser.getRole();
        this.currentSeasonTicket = new SeasonTicketDTO(poleUser);
    }
}
