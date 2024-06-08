package org.example.service.impl;

import org.example.dao.TreatmentDao;
import org.example.exceptions.TreatmentNotFoundException;
import org.example.model.Treatment;
import org.example.service.TreatmentService;

import java.util.Optional;

public class TreatmentServiceImpl implements TreatmentService {
    private final TreatmentDao treatmentDao;

    public TreatmentServiceImpl(TreatmentDao treatmentDao) {
        this.treatmentDao = treatmentDao;
    }

    @Override
    public void saveTreatment(Treatment treatment) {
        treatmentDao.saveTreatment(treatment);
    }

    @Override
    public Treatment getTreatmentById(int id) {
        return Optional.of(treatmentDao.getTreatmentById(id))
                .orElseThrow(() ->new TreatmentNotFoundException("Treatment is not found"));
    }

    @Override
    public Treatment updateTreatmentById(int id, Treatment treatment) {
        Treatment treatmentFromDb = treatmentDao.getTreatmentById(id);

        if(treatmentFromDb == null){
            throw new TreatmentNotFoundException("Treatment is not found");
        }

        return treatmentDao.updateTreatmentById(id, treatment);
    }

    @Override
    public void removeTreatment(int id) {
        Treatment treatmentFromDb = treatmentDao.getTreatmentById(id);

        if(treatmentFromDb == null){
            throw new TreatmentNotFoundException("Treatment is not found");
        }

        treatmentDao.removeTreatment(id);
    }
}
