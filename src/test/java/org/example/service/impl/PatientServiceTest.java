package org.example.service.impl;

import org.example.dao.PatientDao;
import org.example.exceptions.PatientNotFoundException;
import org.example.model.Diagnosis;
import org.example.model.Doctor;
import org.example.model.Gender;
import org.example.model.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PatientServiceTest {
    private Patient patient;
    private Doctor doctor;
    private Diagnosis diagnosis;
    @Mock
    private PatientDao patientDao;

    @InjectMocks
    private PatientServiceImpl patientService;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setup() {
        patient = new Patient(1, "Ivan", "Ivanov", Gender.MAN);
        doctor = new Doctor(1,"Katya", "Ivanova", "oncologist");
        diagnosis = new Diagnosis(1,"cancer");
    }

    @Test
    void save_WhenSavePatientInDb_ShouldPatientSaved() {
        patientService.savePatient(patient);

        verify(patientDao, times(1)).savePatient(patient);
    }

    @Test
    void get_WhenGetPatientById_ShouldPatientGot() {
        when(patientDao.getPatientById(patient.getId())).thenReturn(patient);

        Patient result = patientService.getPatientById(patient.getId());

        Assertions.assertEquals(patient, result);
    }

    @Test
    void get_WhenGetPatient_ShouldThrowPatientNotFoundException() {
        Patient patient = new Patient(2, "Nina", "Doroshko", Gender.WOMAN);

        when(patientDao.getPatientById(patient.getId())).thenThrow(PatientNotFoundException.class);

        Assertions.assertThrows(PatientNotFoundException.class, () ->
                patientService.getPatientById(patient.getId()));
    }

    @Test
    void update_WhenUpdatePatient_ShouldPatientUpdate() {
        Patient updatedPatient = new Patient(1, "Masha", "Ivanova", Gender.MAN);

        when(patientDao.getPatientById(patient.getId())).thenReturn(patient);
        when(patientDao.updatePatientById(patient.getId(), doctor.getId(), diagnosis.getDiagnosisId(), updatedPatient)).
                thenReturn(updatedPatient);

        Patient result = patientService.updatePatientById(patient.getId(), doctor.getId(), doctor.getId(), updatedPatient);
        Assertions.assertEquals(updatedPatient, result);
    }


    @Test
    void delete_WhenDeletePatient_ShouldPatientBeDeleted() {
        when(patientDao.getPatientById(patient.getId())).thenReturn(patient);

        patientService.removePatient(patient.getId());

        verify(patientDao, times(1)).removePatient(patient.getId());
    }

    @Test
    void delete_WhenDeletePatient_ShouldThrowPatientNotFoundException() {
        when(patientDao.getPatientById(patient.getId())).thenThrow(PatientNotFoundException.class);

        Assertions.assertThrows(PatientNotFoundException.class,
                () -> patientService.removePatient(patient.getId()));

        verify(patientDao, never()).removePatient(patient.getId());
    }
}
