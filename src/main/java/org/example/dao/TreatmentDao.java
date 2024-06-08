package org.example.dao;

import org.example.model.Treatment;

import java.util.List;

public interface TreatmentDao {
    int saveTreatment(Treatment treatment);

    Treatment getTreatmentById(int id);

    Treatment updateTreatmentById(int id, Treatment treatment);

    void removeTreatment(int id);

    List<Treatment> getAllTreatmentByDiagnosisId(int diagnosisId);
}
