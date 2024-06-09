package org.example.service.impl;

import org.example.dao.DiagnosisDao;
import org.example.dao.DoctorDao;
import org.example.dao.PatientDao;
import org.example.exceptions.PatientNotFoundException;
import org.example.model.Patient;
import org.example.service.PatientService;

import java.util.Optional;

public class PatientServiceImpl implements PatientService {
    private final PatientDao patientDao;

    public PatientServiceImpl(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    @Override
    public void savePatient(Patient patient) {
        patientDao.savePatient(patient);
    }

    @Override
    public Patient getPatientById(int id) {
        return Optional.of(patientDao.getPatientById(id))
                .orElseThrow(() -> new PatientNotFoundException("Patient is not found"));
    }

    @Override
    public Patient updatePatientById(int id, int doctorId, int diagnosisID, Patient patient) {
        Patient patientFromDb = getPatientById(id);

        if (patientFromDb == null) {
            throw new PatientNotFoundException("Patient is not found");
        }

        return patientDao.updatePatientById(id, doctorId, diagnosisID, patient);
    }

    @Override
    public void removePatient(int id) {
        Patient patientFromDb = getPatientById(id);

        if (patientFromDb == null) {
            throw new PatientNotFoundException("Patient is not found");
        }

        patientDao.removePatient(id);
    }
}
