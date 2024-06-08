package org.example.dao;

import org.example.model.Patient;

public interface PatientDao {
    int savePatient(Patient patient);

    Patient getPatientById(int id);

    Patient updatePatientById(int id, int doctorId, int diagnosisId, Patient patient);

    void removePatient(int id);
}
