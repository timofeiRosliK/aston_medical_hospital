package org.example.dto;

public class DiagnosisCreateDto {
    private String name;

    public DiagnosisCreateDto() {
    }

    public DiagnosisCreateDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
