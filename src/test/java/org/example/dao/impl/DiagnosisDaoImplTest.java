package org.example.dao.impl;

import org.example.config.DataSource;
import org.example.dao.impl.DiagnosisDaoImpl;
import org.example.model.Diagnosis;
import org.example.model.Treatment;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class DiagnosisDaoImplTest {

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withExposedPorts(3306);

    private static DiagnosisDaoImpl diagnosisDao;

    @BeforeAll
    public static void setUp() {
        mysqlContainer.start();
        DataSource.init(mysqlContainer.getJdbcUrl(), mysqlContainer.getUsername(), mysqlContainer.getPassword());
        diagnosisDao = new DiagnosisDaoImpl();
        createTables();
    }

    @AfterAll
    public static void tearDown() {
        mysqlContainer.stop();
    }

    @BeforeEach
    public void cleanUpTables() {
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM diagnoses");
            statement.execute("DELETE FROM treatments");
            statement.execute("DELETE FROM diagnoses_treatments");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void createTables() {
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE diagnoses (" +
                    "diagnosis_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "diagnosis_name VARCHAR(255) NOT NULL" +
                    ")");
            statement.execute("CREATE TABLE treatments (" +
                    "treatment_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "treatment_name VARCHAR(255) NOT NULL" +
                    ")");
            statement.execute("CREATE TABLE diagnoses_treatments (" +
                    "diagnosis_id INT, " +
                    "treatment_id INT, " +
                    "FOREIGN KEY (diagnosis_id) REFERENCES diagnoses(diagnosis_id), " +
                    "FOREIGN KEY (treatment_id) REFERENCES treatments(treatment_id)" +
                    ")");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSaveDiagnosis() {
        Diagnosis diagnosis = new Diagnosis(0, "Flu");
        diagnosisDao.saveDiagnosis(diagnosis);
        List<Diagnosis> allDiagnoses = diagnosisDao.getAllDiagnoses();
        assertNotNull(allDiagnoses);
        assertEquals(allDiagnoses.size(), 1);
    }

    @Test
    void testGetDiagnosisById() {
        Diagnosis diagnosis = new Diagnosis(1, "Cold");
        int savedId = diagnosisDao.saveDiagnosis(diagnosis);
        Diagnosis retrievedDiagnosis = diagnosisDao.getDiagnosisById(savedId);
        assertNotNull(retrievedDiagnosis);
        assertEquals("Cold", retrievedDiagnosis.getName());
    }

    @Test
    void testUpdateDiagnosisById() {
        Diagnosis diagnosis = new Diagnosis(0, "Headache");
        int savedId = diagnosisDao.saveDiagnosis(diagnosis);
        diagnosis.setName("Migraine");
        diagnosisDao.updateDiagnosisById(savedId, diagnosis);
        Diagnosis updatedDiagnosis = diagnosisDao.getDiagnosisById(savedId);
        assertNotNull(updatedDiagnosis);
        assertEquals("Migraine", updatedDiagnosis.getName());
    }

    @Test
    void testRemoveDiagnosis() {
        Diagnosis diagnosis = new Diagnosis(0, "Back Pain");
        diagnosisDao.saveDiagnosis(diagnosis);
        diagnosisDao.removeDiagnosis(diagnosis.getDiagnosisId());
        Diagnosis removedDiagnosis = diagnosisDao.getDiagnosisById(diagnosis.getDiagnosisId());
        assertNull(removedDiagnosis);
    }

    @Test
    void testGetAllDiagnoses() {
        Diagnosis diagnosis1 = new Diagnosis(0, "Asthma");
        Diagnosis diagnosis2 = new Diagnosis(0, "Diabetes");
        diagnosisDao.saveDiagnosis(diagnosis1);
        diagnosisDao.saveDiagnosis(diagnosis2);
        List<Diagnosis> diagnoses = diagnosisDao.getAllDiagnoses();
        assertEquals(2, diagnoses.size());
    }

    @Test
    void testAddDiagnosisToTreatment() {
        int treatmentId = 0;
        Treatment treatment = new Treatment(treatmentId, "Physiotherapy");
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement("INSERT INTO treatments (treatment_name) VALUES ('Physiotherapy')",
        Statement.RETURN_GENERATED_KEYS)) {
            statement.execute();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            treatmentId = generatedKeys.getInt(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Diagnosis diagnosis = new Diagnosis(0, "Arthritis");
        int savedId = diagnosisDao.saveDiagnosis(diagnosis);
        diagnosisDao.addDiagnosisToTreatment(savedId, treatmentId);

        treatment.setTreatmentId(treatmentId);

        Diagnosis diagnosisWithTreatment = diagnosisDao.getDiagnosisWithTreatment(treatment);
        assertNotNull(diagnosisWithTreatment);
        assertEquals("Arthritis", diagnosisWithTreatment.getName());
    }

    @Test
    void testDeleteTreatmentsWithDiagnosis() {
        Treatment treatment = new Treatment(0, "Chemotherapy");
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement("INSERT INTO treatments (treatment_name) VALUES ('Chemotherapy')",
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.execute();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            treatment.setTreatmentId(generatedKeys.getInt(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Diagnosis diagnosis = new Diagnosis(0, "Cancer");
        int savedDiagnosisId = diagnosisDao.saveDiagnosis(diagnosis);
        diagnosisDao.addDiagnosisToTreatment(savedDiagnosisId, treatment.getTreatmentId());

        diagnosisDao.deleteTreatmentsWithDiagnosis(treatment.getTreatmentId(),savedDiagnosisId);
        Diagnosis diagnosisWithTreatment = diagnosisDao.getDiagnosisWithTreatment(treatment);
        assertNull(diagnosisWithTreatment);
    }
}
