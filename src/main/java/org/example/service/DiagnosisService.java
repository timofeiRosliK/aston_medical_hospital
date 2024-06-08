package org.example.service;

import org.example.model.Diagnosis;

public interface DiagnosisService {
    void saveDiagnosis(Diagnosis diagnosis);

    Diagnosis getDiagnosisById(int id);

    Diagnosis updateDiagnosisById(int id, Diagnosis diagnosis);

    void addDiagnosisToTreatment(int diagnosisId, int treatmentId);

    void deleteTreatmentsWithDiagnosis(int treatmentId, int diagnosesId);
}
