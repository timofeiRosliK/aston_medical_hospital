package org.example.dto;

public class TreatmentUpdateDto {
    private String name;

    public TreatmentUpdateDto() {
    }

    public TreatmentUpdateDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
