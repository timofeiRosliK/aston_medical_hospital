package org.example.dao.impl;

import org.example.config.DataSource;
import org.example.model.Diagnosis;
import org.example.model.Doctor;
import org.example.model.Gender;
import org.example.model.Patient;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
class PatientDaoImplTest {
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
            statement.execute("CREATE TABLE patients ("
                    + "patient_id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "first_name VARCHAR(50), "
                    + "last_name VARCHAR(50), "
                    + "gender VARCHAR(10), "
                    + "doctor_id INT, "
                    + "diagnosis_id INT)");

            statement.execute("CREATE TABLE doctors ("
                    + "doctor_id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "first_name VARCHAR(50), "
                    + "last_name VARCHAR(50), "
                    + "specialization VARCHAR(50))");

            statement.execute("CREATE TABLE diagnoses ("
                    + "diagnosis_id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "diagnosis_name VARCHAR(50))");
        }
    }

    @BeforeEach
    public void cleanUpTables() {
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM patients");
            statement.execute("DELETE FROM doctors");
            statement.execute("DELETE FROM diagnoses");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void tearDown() {
        mysqlContainer.stop();
    }

    @Test
    void testSavePatient() {
        PatientDaoImpl patientDao = new PatientDaoImpl();
        Patient patient = new Patient(0, "John", "Doe", Gender.MAN);

        int id = patientDao.savePatient(patient);
        Patient savedPatient = patientDao.getPatientById(id);

        assertNotNull(savedPatient);
        assertEquals("John", savedPatient.getFirstName());
        assertEquals("Doe", savedPatient.getLastName());
        assertEquals(Gender.MAN, savedPatient.getGender());
    }

    @Test
    void testGetPatientById() {
        PatientDaoImpl patientDao = new PatientDaoImpl();
        Patient patient = new Patient(0, "Jane", "Smith", Gender.WOMAN);

        int id = patientDao.savePatient(patient);
        Patient retrievedPatient = patientDao.getPatientById(id);

        assertNotNull(retrievedPatient);
        assertEquals("Jane", retrievedPatient.getFirstName());
        assertEquals("Smith", retrievedPatient.getLastName());
        assertEquals(Gender.WOMAN, retrievedPatient.getGender());
    }

    @Test
    void testUpdatePatientById() {
        PatientDaoImpl patientDao = new PatientDaoImpl();
        Patient patient = new Patient(0, "Jake", "Wilson", Gender.MAN);

        int id = patientDao.savePatient(patient);
        Patient updatedPatient = new Patient("Jake", "Wilson", Gender.MAN);
        patientDao.updatePatientById(id, 0, 0, updatedPatient);

        Patient retrievedPatient = patientDao.getPatientById(id);

        assertNotNull(retrievedPatient);
        assertEquals("Jake", retrievedPatient.getFirstName());
        assertEquals("Wilson", retrievedPatient.getLastName());
        assertEquals(Gender.MAN, retrievedPatient.getGender());
    }

    @Test
    void testRemovePatient() {
        PatientDaoImpl patientDao = new PatientDaoImpl();
        Patient patient = new Patient(0, "Mike", "Johnson", Gender.WOMAN);

        int id = patientDao.savePatient(patient);
        patientDao.removePatient(id);
        Patient retrievedPatient = patientDao.getPatientById(id);

        assertNull(retrievedPatient);
    }
}