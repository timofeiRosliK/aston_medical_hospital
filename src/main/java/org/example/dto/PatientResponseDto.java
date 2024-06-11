package org.example.dto;

import org.example.model.Gender;

public class PatientResponseDto {
    private int id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private DoctorResponseDto doctor;
    private DiagnosisResponseDto diagnosis;

    public PatientResponseDto(){

    }

    public PatientResponseDto(int id, String firstName, String lastName, Gender gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public PatientResponseDto(int id, String firstName, String lastName, Gender gender, DoctorResponseDto doctor, DiagnosisResponseDto diagnosis) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.doctor = doctor;
        this.diagnosis = diagnosis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public DoctorResponseDto getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorResponseDto doctor) {
        this.doctor = doctor;
    }

    public DiagnosisResponseDto getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(DiagnosisResponseDto diagnosis) {
        this.diagnosis = diagnosis;
    }
}
