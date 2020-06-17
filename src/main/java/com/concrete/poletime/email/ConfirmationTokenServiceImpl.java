package com.concrete.poletime.email;

import com.concrete.poletime.exceptions.ConfirmationException;
import com.concrete.poletime.exceptions.RecordNotFoundException;
import com.concrete.poletime.user.PoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    public ConfirmationToken setConfirmationTokenToUser(PoleUser user) throws ConfirmationException {
        if (user.isEnabled()) throw new ConfirmationException("User has already been verified");
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken;
    }

    @Override
    public ConfirmationToken loadConfirmationToken(String confirmationToken) throws RecordNotFoundException {
        return confirmationTokenRepository.findByConfirmationToken(confirmationToken)
                .orElseThrow(() -> new RecordNotFoundException("Confirmation token was not found!"));
    }

    @Override
    public ConfirmationToken getConfirmationTokenToResend(PoleUser user) throws ConfirmationException, RecordNotFoundException {
        if (user.isEnabled()) throw new ConfirmationException("User has already been verified");
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByPoleUser(user.getId())
                .orElseThrow(() -> new RecordNotFoundException("ConfirmationToken does not exist!"));
        return confirmationToken;
    }
}
