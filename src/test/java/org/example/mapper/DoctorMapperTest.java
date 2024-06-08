package org.example.mapper;

import org.example.dto.DoctorCreateDto;
import org.example.dto.DoctorResponseDto;
import org.example.dto.DoctorUpdateDto;
import org.example.model.Doctor;
import org.example.model.Patient;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DoctorMapperTest {

    private final DoctorMapper doctorMapper = Mappers.getMapper(DoctorMapper.class);

    @Test
    void testToDoctorFromCreateDto() {
        DoctorCreateDto dto = new DoctorCreateDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setSpecialization("Cardiology");

        Doctor doctor = doctorMapper.toDoctor(dto);

        assertNotNull(doctor);
        assertEquals(dto.getFirstName(), doctor.getFirstName());
        assertEquals(dto.getLastName(), doctor.getLastName());
        assertEquals(dto.getSpecialization(), doctor.getSpecialization());
    }

    @Test
    void testToDoctorFromUpdateDto() {
        DoctorUpdateDto dto = new DoctorUpdateDto();
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setSpecialization("Neurology");

        Doctor doctor = doctorMapper.toDoctor(dto);

        assertNotNull(doctor);
        assertEquals(dto.getFirstName(), doctor.getFirstName());
        assertEquals(dto.getLastName(), doctor.getLastName());
        assertEquals(dto.getSpecialization(), doctor.getSpecialization());
    }

    @Test
    void testToDoctorResponseDto() {
        Doctor doctor = new Doctor();
        doctor.setId(1);
        doctor.setFirstName("Alice");
        doctor.setLastName("Smith");
        doctor.setSpecialization("Dermatology");

        Patient patient1 = new Patient();
        patient1.setId(1);
        patient1.setFirstName("Patient One");

        Patient patient2 = new Patient();
        patient2.setId(2);
        patient2.setFirstName("Patient Two");

        doctor.setPatients(Arrays.asList(patient1, patient2));

        DoctorResponseDto responseDto = doctorMapper.toDoctorResponseDto(doctor);

        assertNotNull(responseDto);
        assertEquals(doctor.getId(), responseDto.getId());
        assertEquals(doctor.getFirstName(), responseDto.getFirstName());
        assertEquals(doctor.getLastName(), responseDto.getLastName());
        assertEquals(doctor.getSpecialization(), responseDto.getSpecialization());
    }
}
