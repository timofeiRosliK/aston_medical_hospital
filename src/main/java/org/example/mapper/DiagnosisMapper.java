package org.example.mapper;

import org.example.dto.DiagnosisCreateDto;
import org.example.dto.DiagnosisResponseDto;
import org.example.dto.DiagnosisUpdateDto;
import org.example.model.Diagnosis;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = TreatmentMapper.class)
public interface DiagnosisMapper {
    DiagnosisMapper INSTANCE = Mappers.getMapper(DiagnosisMapper.class);

    Diagnosis toDiagnosis(DiagnosisCreateDto dto);

    Diagnosis toDiagnosis(DiagnosisUpdateDto dto);

    DiagnosisResponseDto toDiagnosisResponseDto(Diagnosis diagnosis);
}
