package org.example.mapper;

import org.example.dto.PatientCreateDto;
import org.example.dto.PatientResponseDto;
import org.example.dto.PatientUpdateDto;
import org.example.model.Diagnosis;
import org.example.model.Doctor;
import org.example.model.Gender;
import org.example.model.Patient;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PatientMapperTest {

    private final PatientMapper patientMapper = Mappers.getMapper(PatientMapper.class);

    @Test
    void testToPatientFromCreateDto() {
        PatientCreateDto dto = new PatientCreateDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setGender(Gender.MAN);

        Patient patient = patientMapper.toPatient(dto);

        assertNotNull(patient);
        assertEquals(dto.getFirstName(), patient.getFirstName());
        assertEquals(dto.getLastName(), patient.getLastName());
        assertEquals(dto.getGender(), patient.getGender());
    }

    @Test
    void testToPatientFromUpdateDto() {
        PatientUpdateDto dto = new PatientUpdateDto();
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setGender(Gender.WOMAN);
        dto.setDoctorId(1);
        dto.setDiagnosisId(1);

        Doctor doctor = new Doctor();
        doctor.setId(1);
        doctor.setFirstName("Alice");
        doctor.setLastName("Smith");
        doctor.setSpecialization("Dermatology");

        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDiagnosisId(1);
        diagnosis.setName("Asthma");

        Patient patient = patientMapper.toPatient(dto);

        assertNotNull(patient);
        assertEquals(dto.getFirstName(), patient.getFirstName());
        assertEquals(dto.getLastName(), patient.getLastName());
        assertEquals(dto.getGender(), patient.getGender());
    }

    @Test
    void testToPatientResponseDto() {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setGender(Gender.MAN);

        Doctor doctor = new Doctor();
        doctor.setId(1);
        doctor.setFirstName("Alice");
        doctor.setLastName("Smith");
        doctor.setSpecialization("Dermatology");

        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDiagnosisId(1);
        diagnosis.setName("Asthma");

        patient.setDoctor(doctor);
        patient.setDiagnosis(diagnosis);

        PatientResponseDto responseDto = patientMapper.toPatientResponseDto(patient);

        assertNotNull(responseDto);
        assertEquals(patient.getId(), responseDto.getId());
        assertEquals(patient.getFirstName(), responseDto.getFirstName());
        assertEquals(patient.getLastName(), responseDto.getLastName());
        assertEquals(patient.getGender(), responseDto.getGender());
        assertNotNull(responseDto.getDoctor());
        assertEquals(patient.getDoctor().getId(), responseDto.getDoctor().getId());
        assertEquals(patient.getDoctor().getFirstName(), responseDto.getDoctor().getFirstName());
        assertEquals(patient.getDoctor().getLastName(), responseDto.getDoctor().getLastName());
        assertEquals(patient.getDoctor().getSpecialization(), responseDto.getDoctor().getSpecialization());
        assertNotNull(responseDto.getDiagnosis());
        assertEquals(patient.getDiagnosis().getDiagnosisId(), responseDto.getDiagnosis().getDiagnosisId());
        assertEquals(patient.getDiagnosis().getName(), responseDto.getDiagnosis().getName());
    }
}
