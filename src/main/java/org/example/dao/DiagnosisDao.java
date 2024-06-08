package org.example.dao;

import org.example.model.Diagnosis;
import org.example.model.Treatment;

import java.util.List;

public interface DiagnosisDao {
    int saveDiagnosis(Diagnosis diagnosis);

    Diagnosis getDiagnosisById(int id);

    Diagnosis updateDiagnosisById(int id, Diagnosis diagnosis);

    void removeDiagnosis(int id);

    void addDiagnosisToTreatment(int diagnosisId, int treatmentId);

    List<Diagnosis> getAllDiagnoses();

    Diagnosis getDiagnosisWithTreatment(Treatment treatment);

    void deleteTreatmentsWithDiagnosis(int treatmentId, int diagnosesId);
}
