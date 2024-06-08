package org.example.service;

import org.example.model.Doctor;

public interface DoctorService {
    void saveDoctor(Doctor doctor);

    Doctor getDoctorById(int id);

    Doctor updateDoctorById(int id, Doctor doctor);

    void removeDoctor(int id);
}
