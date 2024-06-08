package org.example.dto;

public class DiagnosisTreatmentResponseDto {
    private String diagnosisName;
    private String treatmentName;

    public DiagnosisTreatmentResponseDto() {
    }

    public DiagnosisTreatmentResponseDto(String diagnosisName, String treatmentName) {
        this.diagnosisName = diagnosisName;
        this.treatmentName = treatmentName;
    }

    public String getDiagnosisName() {
        return diagnosisName;
    }

    public void setDiagnosisName(String diagnosisName) {
        this.diagnosisName = diagnosisName;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }
}
