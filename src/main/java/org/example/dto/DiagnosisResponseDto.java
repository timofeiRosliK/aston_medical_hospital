package org.example.dto;

import java.util.List;

public class DiagnosisResponseDto {
    private int diagnosisId;
    private String name;
    private List<TreatmentResponseDto> treatments;

    public DiagnosisResponseDto() {
    }

    public DiagnosisResponseDto(int diagnosisId, String name, List<TreatmentResponseDto> treatments) {
        this.diagnosisId = diagnosisId;
        this.name = name;
        this.treatments = treatments;
    }

    public int getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(int diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreatmentResponseDto> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<TreatmentResponseDto> treatments) {
        this.treatments = treatments;
    }
}
