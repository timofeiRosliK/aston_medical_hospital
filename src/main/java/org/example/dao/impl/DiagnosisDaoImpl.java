package org.example.dao.impl;

import org.example.config.DataSource;
import org.example.dao.DiagnosisDao;
import org.example.model.Diagnosis;
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

public class DiagnosisDaoImpl implements DiagnosisDao {
    private static final Logger LOGGER = Logger.getLogger(DiagnosisDaoImpl.class.getName());
    private static final String SQL_ERROR = "An inappropriate SQL syntax";
    private static final String INSERT_DIAGNOSIS = "INSERT INTO diagnoses (diagnosis_name) VALUES (?)";
    private static final String INSERT_DIAGNOSIS_TREATMENT = """
            INSERT INTO diagnoses_treatments(diagnosis_id, treatment_id) VALUES(?, ?)
            """;
    private static final String GET_DIAGNOSIS = "SELECT * FROM diagnoses WHERE diagnosis_id = ?";
    private static final String UPDATE_DIAGNOSIS = "UPDATE diagnoses SET diagnosis_name = ? WHERE diagnosis_id = ?";
    private static final String DELETE_DIAGNOSIS = "DELETE FROM diagnoses WHERE diagnosis_id = ?";
    private static final String DELETE_TREATMENTS_IN_DIAGNOSES = """
            DELETE FROM diagnoses_treatments  WHERE treatment_id = ? AND diagnosis_id = ? 
            """;
    private static final String GET_ALL_DIAGNOSES = "SELECT * FROM diagnoses";
    private static final String GET_DIAGNOSES_BY_TREATMENT = """ 
            SELECT di.diagnosis_id, di.diagnosis_name 
            FROM diagnoses di JOIN diagnoses_treatments dt ON di.diagnosis_id = dt.diagnosis_id JOIN treatments t
            ON dt.treatment_id = t.treatment_id WHERE t.treatment_id = ? 
            """;

    @Override
    public int saveDiagnosis(Diagnosis diagnosis) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(INSERT_DIAGNOSIS, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, diagnosis.getName());
            pst.execute();

            ResultSet generatedKeys = pst.getGeneratedKeys();
            generatedKeys.next();
            return generatedKeys.getInt(1);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
        return -1;
    }

    @Override
    public Diagnosis getDiagnosisById(int id) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(GET_DIAGNOSIS)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                return null;
            }

            int diagnosisId = rs.getInt("diagnosis_id");
            String diagnosisName = rs.getString("diagnosis_name");
            return new Diagnosis(diagnosisId, diagnosisName);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
        return null;
    }

    @Override
    public Diagnosis updateDiagnosisById(int id, Diagnosis diagnosis) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(UPDATE_DIAGNOSIS)) {
            pst.setString(1, diagnosis.getName());
            pst.setInt(2, id);
            pst.execute();

            return new Diagnosis(id, diagnosis.getName());
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
        return null;
    }

    @Override
    public void removeDiagnosis(int id) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(DELETE_DIAGNOSIS)) {
            pst.setInt(1, id);
            pst.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
    }

    @Override
    public List<Diagnosis> getAllDiagnoses() {
        List<Diagnosis> diagnoses = new ArrayList<>();

        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(GET_ALL_DIAGNOSES);

            while (rs.next()) {
                int diagnosisId = rs.getInt("Diagnosis_id");
                String diagnosisName = rs.getString("Diagnosis_name");
                diagnoses.add(new Diagnosis(diagnosisId, diagnosisName));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
        return diagnoses;
    }


    public Diagnosis getDiagnosisWithTreatment(Treatment treatment) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(GET_DIAGNOSES_BY_TREATMENT)) {
            pst.setInt(1, treatment.getTreatmentId());

            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                return null;
            }

            int id = rs.getInt("diagnosis_id");
            String name = rs.getString("diagnosis_name");
            return new Diagnosis(id, name);

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
        return null;
    }

    @Override
    public void addDiagnosisToTreatment(int diagnosisId, int treatmentId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(INSERT_DIAGNOSIS_TREATMENT)) {
            pst.setInt(1, diagnosisId);
            pst.setInt(2, treatmentId);

            pst.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
    }

    @Override
    public void deleteTreatmentsWithDiagnosis(int treatmentId, int diagnosesId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(DELETE_TREATMENTS_IN_DIAGNOSES)) {
            pst.setInt(1, treatmentId);
            pst.setInt(2, diagnosesId);
            pst.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, SQL_ERROR, e);
        }
    }
}
