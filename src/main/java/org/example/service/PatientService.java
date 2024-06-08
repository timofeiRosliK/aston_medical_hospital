package org.example.service;

import org.example.model.Patient;

public interface PatientService {
    void savePatient(Patient patient);

    Patient getPatientById(int id);

    Patient updatePatientById(int id, int doctorId, int diagnosisId, Patient patient);

    void removePatient(int id);
}
