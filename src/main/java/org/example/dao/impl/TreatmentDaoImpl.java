package org.example.dao.impl;

import org.example.config.DataSource;
import org.example.dao.TreatmentDao;
import org.example.model.Treatment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TreatmentDaoImpl implements TreatmentDao {

    private static final Logger LOGGER = Logger.getLogger(TreatmentDao.class.getName());

    private static final String SQL_ERROR = "An inappropriate SQL syntax";
    private static final String INSERT_TREATMENT = "INSERT INTO treatments (treatment_name) VALUES (?)";
    private static final String GET_TREATMENT = "SELECT * FROM treatments WHERE treatment_id = ?";
    private static final String UPDATE_TREATMENT = "UPDATE treatments SET treatment_name = ? WHERE treatment_id = ?";
    private static final String DELETE_TREATMENT = "DELETE FROM treatments WHERE treatment_id = ?";
    private static final String GET_TREATMENT_BY_DIAGNOSIS = """
            SELECT t.treatment_id, t.treatment_name FROM treatments t 
            JOIN diagnoses_treatments dt ON dt.treatment_id = t.treatment_id
            WHERE dt.diagnosis_id = ? 
            """;

    @Override
    public int saveTreatment(Treatment treatment) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(INSERT_TREATMENT, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, treatment.getName());
            pst.execute();

            ResultSet generatedKeys = pst.getGeneratedKeys();
            return generatedKeys.next() ? generatedKeys.getInt(1) : -1;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
        return -1;
    }

    @Override
    public Treatment getTreatmentById(int id) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(GET_TREATMENT)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                return null;
            }
            int treatmentId = rs.getInt("treatment_id");
            String treatmentName = rs.getString("treatment_name");
            return new Treatment(treatmentId, treatmentName);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
        return null;
    }

    @Override
    public Treatment updateTreatmentById(int id, Treatment treatment) {
        String treatmentName = treatment.getName();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(UPDATE_TREATMENT)) {
            pst.setString(1, treatmentName);
            pst.setInt(2, id);
            pst.execute();
            return new Treatment(id, treatmentName);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
        return null;
    }

    @Override
    public void removeTreatment(int id) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(DELETE_TREATMENT)) {
            pst.setInt(1, id);
            pst.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
    }

    public List<Treatment> getAllTreatmentByDiagnosisId(int diagnosisId) {
        List<Treatment> treatments = new ArrayList<>();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(GET_TREATMENT_BY_DIAGNOSIS)) {
            pst.setInt(1, diagnosisId);

            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                int treatmentId = resultSet.getInt("t.treatment_id");
                String treatmentName = resultSet.getString("t.treatment_name");
                treatments.add(new Treatment(treatmentId, treatmentName));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
        return treatments;
    }

}

