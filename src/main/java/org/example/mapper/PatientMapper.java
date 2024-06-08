package org.example.mapper;

import org.example.dto.PatientCreateDto;
import org.example.dto.PatientResponseDto;
import org.example.dto.PatientUpdateDto;
import org.example.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DoctorMapper.class, DiagnosisMapper.class})
public interface PatientMapper {
    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);

    Patient toPatient(PatientCreateDto dto);

    Patient toPatient(PatientUpdateDto dto);

    PatientResponseDto toPatientResponseDto(Patient patient);
}
