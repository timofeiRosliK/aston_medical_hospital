package org.example.dto;

public class DiagnosisUpdateDto {
    private String name;

    public DiagnosisUpdateDto() {
    }

    public DiagnosisUpdateDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
