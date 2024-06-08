package org.example.exceptions;

public class DiagnosisNotFoundException extends RuntimeException{
    public DiagnosisNotFoundException() {
        super();
    }

    public DiagnosisNotFoundException(String message) {
        super(message);
    }
}
