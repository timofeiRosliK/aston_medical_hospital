package org.example.dao;

import org.example.model.Doctor;

public interface DoctorDao {
    int saveDoctor(Doctor doctor);

    Doctor getDoctorById(int id);

    Doctor updateDoctorById(int id, Doctor doctor);

    void removeDoctor(int id);
}
