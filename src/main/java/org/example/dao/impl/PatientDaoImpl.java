package org.example.dao.impl;

import org.example.config.DataSource;
import org.example.dao.PatientDao;
import org.example.model.Diagnosis;
import org.example.model.Doctor;
import org.example.model.Gender;
import org.example.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientDaoImpl implements PatientDao {
    private Logger LOGGER = Logger.getLogger(PatientDaoImpl.class.getName());
    private static final String SQL_ERROR = "An inappropriate SQL syntax";
    private static final String INSERT_PATIENT = "INSERT INTO patients (first_name, last_name, gender) VALUES (?,?,?)";
    private static final String GET_PATIENT = """
            SELECT p.patient_id, p.first_name, p.last_name, p.gender, d.doctor_id, d.first_name, d.last_name, 
            d.specialization, di.diagnosis_name FROM patients p LEFT JOIN doctors d ON p.doctor_id = d.doctor_id 
            LEFT JOIN diagnoses di ON p.diagnosis_id = di.diagnosis_id WHERE p.patient_id = ?
            """;
    private static final String UPDATE_PATIENT = """
            UPDATE patients SET first_name = ?, last_name = ?, gender = ?, 
            doctor_id = ?, diagnosis_id = ? WHERE patient_id = ?
            """;
    private static final String DELETE_PATIENT = "DELETE FROM patients WHERE patient_id = ?";

    @Override
    public int savePatient(Patient patient) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(INSERT_PATIENT, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, patient.getFirstName());
            pst.setString(2, patient.getLastName());
            pst.setString(3, patient.getGender().name());
            pst.execute();

            ResultSet generatedKeys = pst.getGeneratedKeys();
            return generatedKeys.next() ? generatedKeys.getInt(1) : -1;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
        return -1;
    }

    @Override
    public Patient getPatientById(int id) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(GET_PATIENT)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                return null;
            }

            int patientId = rs.getInt("patient_id");
            String firstName = rs.getString("p.first_name");
            String lastName = rs.getString("p.last_name");
            String gender = rs.getString("gender");
            String doctorName = rs.getString("d.first_name");
            String doctorLastName = rs.getString("d.last_name");
            String specialization = rs.getString("d.specialization");
            String diagnosisName = rs.getString("di.diagnosis_name");

            return new Patient(patientId, firstName, lastName, Gender.valueOf(gender),
                    new Doctor(doctorName, doctorLastName, specialization), new Diagnosis(diagnosisName));
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
        return null;
    }

    @Override
    public Patient updatePatientById(int id, int doctorId, int diagnosisId, Patient patient) {
        String firstName = patient.getFirstName();
        String lastName = patient.getLastName();
        String gender = patient.getGender().name();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(UPDATE_PATIENT)) {
            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, gender);
            pst.setInt(4, doctorId);
            pst.setInt(5,diagnosisId);
            pst.setInt(6, id);
            pst.execute();
            return new Patient(firstName, lastName, Gender.valueOf(gender));
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
        return null;
    }

    @Override
    public void removePatient(int id) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(DELETE_PATIENT)) {
            pst.setInt(1, id);
            pst.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
    }
}