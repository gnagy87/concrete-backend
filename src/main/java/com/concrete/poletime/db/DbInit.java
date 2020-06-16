package com.concrete.poletime.db;

import com.concrete.poletime.seasonticket.SeasonTicket;
import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.user.PoleUserRepository;
import com.concrete.poletime.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Service
public class DbInit implements CommandLineRunner {
    private PoleUserRepository poleUserRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DbInit(PoleUserRepository poleUserRepo, PasswordEncoder passwordEncoder) {
        this.poleUserRepo = poleUserRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        PoleUser admin = new PoleUser(
                "admin@concrete.hu",
                "admin",
                "admin",
                passwordEncoder.encode("123456"));
        admin.setEnabled(true);
        admin.setRole(Role.ADMIN);
        poleUserRepo.save(admin);

        PoleUser trainer = new PoleUser(
                "trainer@concrete.hu",
                "trainer",
                "trainer",
                passwordEncoder.encode("123456"));
        trainer.setEnabled(true);
        trainer.setRole(Role.TRAINER);
        poleUserRepo.save(trainer);

        for (int i = 0; i < 20; i++) {
            PoleUser guest = new PoleUser(
                    "pole_user" + (i + 1) + "@concrete.hu",
                    "firstName" + (i + 1),
                    "lastName" + (i + 1),
                    passwordEncoder.encode("123456"));
            guest.setEnabled(true);
            SeasonTicket seasonTicket = new SeasonTicket();
            seasonTicket.setAmount(20);
            seasonTicket.setSellerId(admin.getId());
            Date date = new Date();
            seasonTicket.setValidFrom(date);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 30);
            date = c.getTime();
            seasonTicket.setValidTo(date);
            Set<SeasonTicket> seasonTickets = guest.getSeasonTickets();
            seasonTickets.add(seasonTicket);
            guest.setSeasonTickets(seasonTickets);
            seasonTicket.setPoleUser(guest);
            poleUserRepo.save(guest);
        }
    }
}
