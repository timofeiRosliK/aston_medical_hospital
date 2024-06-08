package org.example.service;

import org.example.model.Treatment;

public interface TreatmentService {
    void saveTreatment(Treatment treatment);

    Treatment getTreatmentById(int id);

    Treatment updateTreatmentById(int id, Treatment treatment);

    void removeTreatment(int id);
}
