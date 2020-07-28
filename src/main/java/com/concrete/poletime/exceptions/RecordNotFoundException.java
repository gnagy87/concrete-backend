package com.concrete.poletime.exceptions;

public class RecordNotFoundException extends Exception {
    public RecordNotFoundException(String message) {
        super(message);
    }
    public RecordNotFoundException() {
        super("Record Not Found!");
    }

}
