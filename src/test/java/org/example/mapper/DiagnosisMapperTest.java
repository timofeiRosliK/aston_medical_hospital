package org.example.mapper;

import org.example.dto.DiagnosisCreateDto;
import org.example.dto.DiagnosisResponseDto;
import org.example.dto.DiagnosisUpdateDto;
import org.example.model.Diagnosis;
import org.example.model.Treatment;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DiagnosisMapperTest {

    private final DiagnosisMapper diagnosisMapper = Mappers.getMapper(DiagnosisMapper.class);

    @Test
    void testToDiagnosisFromCreateDto() {
        DiagnosisCreateDto dto = new DiagnosisCreateDto();
        dto.setName("Flu");

        Diagnosis diagnosis = diagnosisMapper.toDiagnosis(dto);

        assertNotNull(diagnosis);
        assertEquals(dto.getName(), diagnosis.getName());
    }

    @Test
    void testToDiagnosisFromUpdateDto() {
        DiagnosisUpdateDto dto = new DiagnosisUpdateDto();
        dto.setName("Cold");

        Diagnosis diagnosis = diagnosisMapper.toDiagnosis(dto);

        assertNotNull(diagnosis);
        assertEquals(dto.getName(), diagnosis.getName());
    }

    @Test
    void testToDiagnosisResponseDto() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDiagnosisId(1);
        diagnosis.setName("Asthma");

        Treatment treatment1 = new Treatment();
        treatment1.setTreatmentId(1);
        treatment1.setName("Inhaler");

        Treatment treatment2 = new Treatment();
        treatment2.setTreatmentId(2);
        treatment2.setName("Steroids");

        diagnosis.setTreatmentList(Arrays.asList(treatment1, treatment2));

        DiagnosisResponseDto responseDto = diagnosisMapper.toDiagnosisResponseDto(diagnosis);

        assertNotNull(responseDto);
        assertEquals(diagnosis.getDiagnosisId(), responseDto.getDiagnosisId());
        assertEquals(diagnosis.getName(), responseDto.getName());
    }
}
