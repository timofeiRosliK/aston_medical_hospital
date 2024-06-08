package org.example.exceptions;

public class TreatmentNotFoundException extends RuntimeException{
    public TreatmentNotFoundException() {
        super();
    }

    public TreatmentNotFoundException(String message) {
        super(message);
    }
}
