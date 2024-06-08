package org.example.service.impl;

import org.example.dao.DiagnosisDao;
import org.example.dao.TreatmentDao;
import org.example.exceptions.DiagnosisNotFoundException;
import org.example.exceptions.TreatmentNotFoundException;
import org.example.model.Diagnosis;
import org.example.model.Treatment;
import org.example.service.DiagnosisService;

import java.util.List;
import java.util.Optional;

public class DiagnosisServiceImpl implements DiagnosisService {
    private final DiagnosisDao diagnosisDao;
    private final TreatmentDao treatmentDao;

    public DiagnosisServiceImpl(DiagnosisDao diagnosisDao, TreatmentDao treatmentDao) {
        this.diagnosisDao = diagnosisDao;
        this.treatmentDao = treatmentDao;
    }

    @Override
    public void saveDiagnosis(Diagnosis diagnosis) {
        diagnosisDao.saveDiagnosis(diagnosis);
    }

    @Override
    public Diagnosis getDiagnosisById(int id) {
        Diagnosis diagnosis = Optional.of(diagnosisDao.getDiagnosisById(id))
                .orElseThrow(() ->new DiagnosisNotFoundException("Diagnosis is not Found"));

        List<Treatment> treatments = Optional.of(treatmentDao.getAllTreatmentByDiagnosisId(id))
                .orElseThrow(() ->new TreatmentNotFoundException("Treatment is not Found"));

        diagnosis.setTreatmentList(treatments);
        return diagnosis;
    }

    @Override
    public Diagnosis updateDiagnosisById(int id, Diagnosis diagnosis) {
        Diagnosis diagnosisFromDb = diagnosisDao.getDiagnosisById(id);

        if(diagnosisFromDb == null){
            throw new DiagnosisNotFoundException("Diagnosis is not found");
        }

        Diagnosis updatedDiagnosis = diagnosisDao.updateDiagnosisById(id, diagnosis);
        return updatedDiagnosis;
    }


    @Override
    public void addDiagnosisToTreatment(int diagnosisId, int treatmentId) {
        diagnosisDao.addDiagnosisToTreatment(diagnosisId, treatmentId);
    }

    @Override
    public void deleteTreatmentsWithDiagnosis(int treatmentId, int diagnosesId) {
        Diagnosis diagnosisFromDb = diagnosisDao.getDiagnosisById(diagnosesId);

        Treatment treatmentFromDb = treatmentDao.getTreatmentById(treatmentId);

        if(diagnosisFromDb == null){
            throw new DiagnosisNotFoundException("Diagnosis is not found");
        }

        if(treatmentFromDb == null){
            throw new TreatmentNotFoundException("Treatment is not found");
        }

        diagnosisDao.deleteTreatmentsWithDiagnosis(treatmentId, diagnosesId);
    }
}
