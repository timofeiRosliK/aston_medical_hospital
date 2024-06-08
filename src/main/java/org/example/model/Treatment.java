package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Treatment {
    private int treatmentId;
    private String name;
    private List<Diagnosis> diagnoses;

    public Treatment(){

    }

    public Treatment(String name) {
        this.treatmentId = treatmentId;
        this.name = name;
    }

    public Treatment(int treatmentId, String name) {
        this.treatmentId = treatmentId;
        this.name = name;
    }

    public Treatment(int treatmentId, String name, List<Diagnosis> diagnoses) {
        this.treatmentId = treatmentId;
        this.name = name;
        this.diagnoses = new ArrayList<>();
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

    public List<Diagnosis> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(List<Diagnosis> diagnoses) {
        this.diagnoses = diagnoses;
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "treatmentId=" + treatmentId +
                ", name='" + name + '\'' +
                ", diagnoses=" + diagnoses +
                '}';
    }
}