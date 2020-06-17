package com.concrete.poletime.email;

import com.concrete.poletime.exceptions.ConfirmationException;
import com.concrete.poletime.exceptions.RecordNotFoundException;
import com.concrete.poletime.user.PoleUser;

public interface ConfirmationTokenService {
    ConfirmationToken setConfirmationTokenToUser(PoleUser user) throws ConfirmationException;
    ConfirmationToken loadConfirmationToken(String confirmationToken) throws RecordNotFoundException;
    ConfirmationToken getConfirmationTokenToResend(PoleUser user) throws ConfirmationException, RecordNotFoundException;
}
