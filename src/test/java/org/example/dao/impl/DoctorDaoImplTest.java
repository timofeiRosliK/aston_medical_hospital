package org.example.dao.impl;

import org.example.config.DataSource;
import org.example.model.Doctor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
public class DoctorDaoImplTest {
    @Container
    private static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withExposedPorts(3306);

    private static DoctorDaoImpl doctorDao;

    @BeforeAll
    public static void setUp() throws SQLException {
        mysqlContainer.start();
        DataSource.init(mysqlContainer.getJdbcUrl(), mysqlContainer.getUsername(), mysqlContainer.getPassword());
        doctorDao = new DoctorDaoImpl();
        createTables();
    }

    @BeforeEach
    public void cleanUpTables() {
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM doctors");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void tearDown() {
        mysqlContainer.stop();
    }

    private static void createTables() {
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE doctors ("
                    + "doctor_id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "first_name VARCHAR(50), "
                    + "last_name VARCHAR(50), "
                    + "specialization VARCHAR(50))");
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSaveDoctor() {
        Doctor doctor = new Doctor(0, "John", "Doe", "Cardiology");

        int savedId = doctorDao.saveDoctor(doctor);
        Doctor savedDoctor = doctorDao.getDoctorById(savedId);

        assertNotNull(savedDoctor);
        assertEquals("John", savedDoctor.getFirstName());
        assertEquals("Doe", savedDoctor.getLastName());
        assertEquals("Cardiology", savedDoctor.getSpecialization());
    }

    @Test
    void testGetDoctorById() {
        Doctor doctor = new Doctor(0, "Jane", "Smith", "Neurology");

        int savedId = doctorDao.saveDoctor(doctor);
        Doctor retrievedDoctor = doctorDao.getDoctorById(savedId);

        assertNotNull(retrievedDoctor);
        assertEquals("Jane", retrievedDoctor.getFirstName());
        assertEquals("Smith", retrievedDoctor.getLastName());
        assertEquals("Neurology", retrievedDoctor.getSpecialization());
    }

    @Test
    void testUpdateDoctorById() {
        Doctor doctor = new Doctor(0, "Jake", "Wilson", "Orthopedics");

        int savedId = doctorDao.saveDoctor(doctor);
        Doctor updatedDoctor = new Doctor(savedId, "Jake", "Wilson", "Pediatrics");
        doctorDao.updateDoctorById(savedId, updatedDoctor);

        Doctor retrievedDoctor = doctorDao.getDoctorById(savedId);

        assertNotNull(retrievedDoctor);
        assertEquals("Jake", retrievedDoctor.getFirstName());
        assertEquals("Wilson", retrievedDoctor.getLastName());
        assertEquals("Pediatrics", retrievedDoctor.getSpecialization());
    }

    @Test
    void testRemoveDoctor() {
        Doctor doctor = new Doctor(0, "Mike", "Johnson", "Dermatology");

        doctorDao.saveDoctor(doctor);
        doctorDao.removeDoctor(4);
        Doctor retrievedDoctor = doctorDao.getDoctorById(4);

        assertNull(retrievedDoctor);
    }
}