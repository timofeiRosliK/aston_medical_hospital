package org.example.dto;

public class TreatmentResponseDto {
    private int treatmentId;
    private String name;

    public TreatmentResponseDto(int treatmentId, String name) {
        this.treatmentId = treatmentId;
        this.name = name;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(int treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
