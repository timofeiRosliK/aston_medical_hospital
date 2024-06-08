package org.example.dao.impl;

import org.example.config.DataSource;
import org.example.dao.DoctorDao;
import org.example.model.Doctor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoctorDaoImpl implements DoctorDao {
    private static final Logger LOGGER = Logger.getLogger(DoctorDaoImpl.class.getName());
    private static final String SQL_ERROR = "An inappropriate SQL syntax";
    private static final String INSERT_DOCTOR = "INSERT INTO doctors (first_name, last_name, specialization) VALUES (?,?,?)";
    private static final String GET_DOCTOR = "SELECT * FROM doctors WHERE doctor_id = ?";
    private static final String UPDATE_DOCTOR = """
            UPDATE doctors SET first_name = ?, last_name = ?, specialization = ? WHERE doctor_id = ?
        """;
    private static final String DELETE_DOCTOR = "DELETE FROM doctors WHERE doctor_id = ?";

    @Override
    public int saveDoctor(Doctor doctor) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(INSERT_DOCTOR, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, doctor.getFirstName());
            pst.setString(2, doctor.getLastName());
            pst.setString(3, doctor.getSpecialization());
            pst.execute();

            ResultSet generatedKeys = pst.getGeneratedKeys();
            return generatedKeys.next() ? generatedKeys.getInt(1) : -1;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
        return -1;
    }

    @Override
    public Doctor getDoctorById(int id) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(GET_DOCTOR)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (!rs.next()) {
                return null;
            }

            int doctorId = rs.getInt("doctor_id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String specialization = rs.getString("specialization");
            return new Doctor(doctorId, firstName, lastName, specialization);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
        return null;
    }

    @Override
    public Doctor updateDoctorById(int id, Doctor doctor) {
        String firstName = doctor.getFirstName();
        String lastName = doctor.getLastName();
        String specialization = doctor.getSpecialization();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(UPDATE_DOCTOR)) {
            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, specialization);
            pst.setInt(4, id);
            pst.execute();
            return new Doctor(id, firstName, lastName, specialization);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
        return null;
    }

    @Override
    public void removeDoctor(int id) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(DELETE_DOCTOR)) {
            pst.setInt(1, id);
            pst.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
    }
}