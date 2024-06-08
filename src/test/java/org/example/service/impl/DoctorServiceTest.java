package org.example.service.impl;

import org.example.dao.DoctorDao;
import org.example.exceptions.DoctorNotFoundException;
import org.example.model.Doctor;
import org.example.service.impl.DoctorServiceImpl;
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

public class DoctorServiceTest {
    private Doctor doctor;

    @Mock
    private DoctorDao doctorDao;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setup() {
        doctor = new Doctor(1, "Galina", "Ivanova", "oncologist");
    }

    @Test
    void save_WhenSaveDoctorInDb_ShouldDoctorSaved() {
        doctorService.saveDoctor(doctor);

        verify(doctorDao, times(1)).saveDoctor(doctor);
    }

    @Test
    void get_WhenGetDoctorById_ShouldDoctorGot() {
        when(doctorDao.getDoctorById(doctor.getId())).thenReturn(doctor);

        Doctor result = doctorService.getDoctorById(doctor.getId());

        Assertions.assertEquals(doctor, result);
    }

    @Test
    void get_WhenGetDoctor_ShouldThrowDoctorNotFoundException() {
        Doctor manualTherapist = new Doctor(2, "Valya", "Sanko", "manual therapist");

        when(doctorDao.getDoctorById(manualTherapist.getId())).thenThrow(DoctorNotFoundException.class);

        Assertions.assertThrows(DoctorNotFoundException.class, () ->
                doctorService.getDoctorById(manualTherapist.getId()));
    }

    @Test
    void update_WhenUpdateDoctor_ShouldDoctorUpdate() {
        Doctor updatedDoctor = new Doctor(1, "Denis", "Yastrem","allergist");

        when(doctorDao.getDoctorById(doctor.getId())).thenReturn(doctor);
        when(doctorDao.updateDoctorById(doctor.getId(), updatedDoctor)).thenReturn(updatedDoctor);

        Doctor result = doctorService.updateDoctorById(doctor.getId(), updatedDoctor);
        Assertions.assertEquals(updatedDoctor, result);
    }


    @Test
    void delete_WhenDeleteDoctor_ShouldDoctorBeDeleted() {
        when(doctorDao.getDoctorById(doctor.getId())).thenReturn(doctor);

        doctorService.removeDoctor(doctor.getId());

        verify(doctorDao, times(1)).removeDoctor(doctor.getId());
    }

    @Test
    void delete_WhenDeleteDoctor_ShouldThrowDoctorNotFoundException() {
        when(doctorDao.getDoctorById(doctor.getId())).thenThrow(DoctorNotFoundException.class);

        Assertions.assertThrows(DoctorNotFoundException.class,
                () -> doctorService.removeDoctor(doctor.getId()));

        verify(doctorDao, never()).removeDoctor(doctor.getId());
    }


}
