package org.example.service.impl;

import org.example.dao.DoctorDao;
import org.example.exceptions.DoctorNotFoundException;
import org.example.model.Doctor;
import org.example.service.DoctorService;

import java.util.Optional;

public class DoctorServiceImpl implements DoctorService {
    private final DoctorDao doctorDao;

    public DoctorServiceImpl(DoctorDao doctorDao) {
        this.doctorDao = doctorDao;
    }

    @Override
    public void saveDoctor(Doctor doctor) {
        doctorDao.saveDoctor(doctor);
    }

    @Override
    public Doctor getDoctorById(int id) {
        return Optional.of(doctorDao.getDoctorById(id))
                .orElseThrow(() ->new DoctorNotFoundException("Doctor is not found"));
    }

    @Override
    public Doctor updateDoctorById(int id, Doctor doctor) {
        Doctor doctorFromDb = doctorDao.getDoctorById(id);

        if (doctorFromDb == null){
            throw new DoctorNotFoundException("Doctor is not found");
        }

        return doctorDao.updateDoctorById(id, doctor);
    }

    @Override
    public void removeDoctor(int id) {
        Doctor doctorFromDb = doctorDao.getDoctorById(id);

        if (doctorFromDb == null){
            throw new DoctorNotFoundException("Doctor is not found");
        }

        doctorDao.removeDoctor(id);
    }
}
