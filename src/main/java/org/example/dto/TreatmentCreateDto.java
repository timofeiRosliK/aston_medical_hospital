package org.example.dto;

public class TreatmentCreateDto {
    private String name;

    public TreatmentCreateDto() {
    }

    public TreatmentCreateDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
