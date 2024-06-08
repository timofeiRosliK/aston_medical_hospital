package org.example.dto;

public class DiagnosisTreatmentCreateDto {
    private int diagnosisId;
    private int treatmentId;

    public DiagnosisTreatmentCreateDto() {
    }

    public DiagnosisTreatmentCreateDto(int diagnosisId, int treatmentId) {
        this.diagnosisId = diagnosisId;
        this.treatmentId = treatmentId;
    }

    public int getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(int diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(int treatmentId) {
        this.treatmentId = treatmentId;
    }
}
