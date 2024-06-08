package org.example.mapper;

import org.example.dto.TreatmentCreateDto;
import org.example.dto.TreatmentResponseDto;
import org.example.dto.TreatmentUpdateDto;
import org.example.model.Treatment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = DiagnosisMapper.class)
public interface TreatmentMapper {
    TreatmentMapper INSTANCE = Mappers.getMapper(TreatmentMapper.class);

    Treatment toTreatment(TreatmentCreateDto dto);

    Treatment toTreatment(TreatmentUpdateDto dto);

    TreatmentResponseDto toTreatmentResponseDto(Treatment treatment);

    List<TreatmentResponseDto> toTreatmentDtoListResponse(List<Treatment> treatments);
}
