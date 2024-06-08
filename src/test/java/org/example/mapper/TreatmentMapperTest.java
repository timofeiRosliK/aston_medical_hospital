package org.example.mapper;

import org.example.dto.TreatmentCreateDto;
import org.example.dto.TreatmentResponseDto;
import org.example.dto.TreatmentUpdateDto;
import org.example.model.Treatment;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TreatmentMapperTest {

    private final TreatmentMapper treatmentMapper = Mappers.getMapper(TreatmentMapper.class);

    @Test
    void testToTreatmentFromCreateDto() {
        TreatmentCreateDto dto = new TreatmentCreateDto();
        dto.setName("Chemotherapy");

        Treatment treatment = treatmentMapper.toTreatment(dto);

        assertNotNull(treatment);
        assertEquals(dto.getName(), treatment.getName());
    }

    @Test
    void testToTreatmentFromUpdateDto() {
        TreatmentUpdateDto dto = new TreatmentUpdateDto();
        dto.setName("Radiation Therapy");

        Treatment treatment = treatmentMapper.toTreatment(dto);

        assertNotNull(treatment);
        assertEquals(dto.getName(), treatment.getName());
    }

    @Test
    void testToTreatmentResponseDto() {
        Treatment treatment = new Treatment();
        treatment.setTreatmentId(1);
        treatment.setName("Physical Therapy");

        TreatmentResponseDto responseDto = treatmentMapper.toTreatmentResponseDto(treatment);

        assertNotNull(responseDto);
        assertEquals(treatment.getTreatmentId(), responseDto.getTreatmentId());
        assertEquals(treatment.getName(), responseDto.getName());
    }

    @Test
    void testToTreatmentDtoListResponse() {
        Treatment treatment1 = new Treatment();
        treatment1.setTreatmentId(1);
        treatment1.setName("Physical Therapy");

        Treatment treatment2 = new Treatment();
        treatment2.setTreatmentId(2);
        treatment2.setName("Occupational Therapy");

        List<Treatment> treatments = Arrays.asList(treatment1, treatment2);

        List<TreatmentResponseDto> responseDtos = treatmentMapper.toTreatmentDtoListResponse(treatments);

        assertNotNull(responseDtos);
        assertEquals(2, responseDtos.size());

        assertEquals(treatment1.getTreatmentId(), responseDtos.get(0).getTreatmentId());
        assertEquals(treatment1.getName(), responseDtos.get(0).getName());

        assertEquals(treatment2.getTreatmentId(), responseDtos.get(1).getTreatmentId());
        assertEquals(treatment2.getName(), responseDtos.get(1).getName());
    }
}
