package org.example.model;

import java.util.List;

public class Diagnosis {
    private int diagnosisId;
    private String name;
    private List<Treatment> treatments;

    public Diagnosis() {

    }

    public Diagnosis(String name) {
        this.name = name;
    }

    public Diagnosis(int diagnosisId, String name) {
        this.diagnosisId = diagnosisId;
        this.name = name;
    }

    public Diagnosis(int diagnosisId, String name, List<Treatment> treatments) {
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

    public List<Treatment> getTreatmentList() {
        return treatments;
    }

    public void setTreatmentList(List<Treatment> treatments) {
        this.treatments = treatments;
    }

    @Override
    public String toString() {
        return "Diagnosis{" +
                "diagnosisId=" + diagnosisId +
                ", name='" + name + '\'' +
                ", treatments=" + treatments +
                '}';
    }
}
