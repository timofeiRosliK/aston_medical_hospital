package org.example.dao.impl;

import org.example.config.DataSource;
import org.example.model.Treatment;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class TreatmentDaoImplTest {
    @Container
    private static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.33")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private static Connection connection;

    @BeforeAll
    public static void setUp() throws SQLException {
        mysqlContainer.start();
        DataSource.init(mysqlContainer.getJdbcUrl(), mysqlContainer.getUsername(), mysqlContainer.getPassword());
        connection = DataSource.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE treatments ("
                    + "treatment_id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "treatment_name VARCHAR(50))");

            statement.execute("CREATE TABLE diagnoses ("
                    + "diagnosis_id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "diagnosis_name VARCHAR(50))");

            statement.execute("CREATE TABLE diagnoses_treatments ("
                    + "diagnosis_id INT, "
                    + "treatment_id INT, "
                    + "PRIMARY KEY (diagnosis_id, treatment_id))");
        }
    }

    @BeforeEach
    public void cleanUpTables() {
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM treatments");
            statement.execute("DELETE FROM diagnoses");
            statement.execute("DELETE FROM diagnoses_treatments");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void tearDown() {
        mysqlContainer.stop();
    }

    @Test
    void testSaveTreatment() {
        TreatmentDaoImpl treatmentDao = new TreatmentDaoImpl();
        Treatment treatment = new Treatment("Physical Therapy");

        int id = treatmentDao.saveTreatment(treatment);
        Treatment savedTreatment = treatmentDao.getTreatmentById(id);

        assertNotNull(savedTreatment);
        assertEquals("Physical Therapy", savedTreatment.getName());
    }

    @Test
    void testGetTreatmentById() {
        TreatmentDaoImpl treatmentDao = new TreatmentDaoImpl();
        Treatment treatment = new Treatment("Chemotherapy");

        int id = treatmentDao.saveTreatment(treatment);
        Treatment retrievedTreatment = treatmentDao.getTreatmentById(id);

        assertNotNull(retrievedTreatment);
        assertEquals("Chemotherapy", retrievedTreatment.getName());
    }

    @Test
    void testUpdateTreatmentById() {
        TreatmentDaoImpl treatmentDao = new TreatmentDaoImpl();
        Treatment treatment = new Treatment("Radiation Therapy");

        int id = treatmentDao.saveTreatment(treatment);
        Treatment updatedTreatment = new Treatment(id, "Updated Radiation Therapy");
        treatmentDao.updateTreatmentById(id, updatedTreatment);

        Treatment retrievedTreatment = treatmentDao.getTreatmentById(id);

        assertNotNull(retrievedTreatment);
        assertEquals("Updated Radiation Therapy", retrievedTreatment.getName());
    }

    @Test
    void testRemoveTreatment() {
        TreatmentDaoImpl treatmentDao = new TreatmentDaoImpl();
        Treatment treatment = new Treatment("Immunotherapy");

        int id = treatmentDao.saveTreatment(treatment);
        treatmentDao.removeTreatment(id);
        Treatment retrievedTreatment = treatmentDao.getTreatmentById(id);

        assertNull(retrievedTreatment);
    }

    @Test
    void testGetAllTreatmentByDiagnosisId() {
        TreatmentDaoImpl treatmentDao = new TreatmentDaoImpl();

        try (Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO diagnoses (diagnosis_name) VALUES ('Cancer')");
        } catch (SQLException e) {
            fail("Failed to insert diagnosis");
        }

        int diagnosisId = 1;

        Treatment treatment1 = new Treatment("Chemotherapy");
        Treatment treatment2 = new Treatment("Radiation Therapy");
        int treatmentId1 = treatmentDao.saveTreatment(treatment1);
        int treatmentId2 = treatmentDao.saveTreatment(treatment2);

        try (var statement = connection.createStatement()) {
            statement.execute("INSERT INTO diagnoses_treatments (diagnosis_id, treatment_id) VALUES (" + diagnosisId + ", " + treatmentId1 + ")");
            statement.execute("INSERT INTO diagnoses_treatments (diagnosis_id, treatment_id) VALUES (" + diagnosisId + ", " + treatmentId2 + ")");
        } catch (SQLException e) {
            fail("Failed to link treatments with diagnosis");
        }

        List<Treatment> treatments = treatmentDao.getAllTreatmentByDiagnosisId(diagnosisId);

        assertNotNull(treatments);
        assertEquals(2, treatments.size());
        assertEquals("Chemotherapy", treatments.get(0).getName());
        assertEquals("Radiation Therapy", treatments.get(1).getName());
    }

}