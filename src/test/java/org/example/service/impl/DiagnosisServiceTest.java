package org.example.service.impl;

import org.example.dao.DiagnosisDao;
import org.example.dao.TreatmentDao;
import org.example.exceptions.DiagnosisNotFoundException;
import org.example.exceptions.TreatmentNotFoundException;
import org.example.model.Diagnosis;
import org.example.model.Treatment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

 class DiagnosisServiceTest {
    private Diagnosis diagnosis;
    private Treatment treatment;
    private List<Treatment> treatments;

    @Mock
    private DiagnosisDao diagnosisDao;

    @Mock
    private TreatmentDao treatmentDao;

    @InjectMocks
    private DiagnosisServiceImpl diagnosisService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        treatment = new Treatment(1, "radiation therapy");
        treatments = new ArrayList<>();
        treatments.add(treatment);
        diagnosis = new Diagnosis(1, "Cancer", treatments);
    }

    @Test
    void save_WhenSaveDiagnosisInDb_ShouldDiagnosisSaved() {
        diagnosisService.saveDiagnosis(diagnosis);
        verify(diagnosisDao).saveDiagnosis(diagnosis);
    }

    @Test
    void get_WhenGetDiagnosisById_ShouldDiagnosisGot() {

        when(diagnosisDao.getDiagnosisById(diagnosis.getDiagnosisId())).thenReturn(diagnosis);
        when(treatmentDao.getAllTreatmentByDiagnosisId(diagnosis.getDiagnosisId())).thenReturn(treatments);


        Diagnosis result = diagnosisService.getDiagnosisById(diagnosis.getDiagnosisId());

        Assertions.assertEquals(diagnosis, result);
    }

    @Test
    void get_WhenGetDiagnosis_ShouldThrowException() {
        Diagnosis fever = new Diagnosis(2, "fever", Arrays.asList(new Treatment("therapy")));

        when(diagnosisDao.getDiagnosisById(fever.getDiagnosisId())).thenThrow(DiagnosisNotFoundException.class);

        Assertions.assertThrows(DiagnosisNotFoundException.class, () ->
                diagnosisService.getDiagnosisById(fever.getDiagnosisId()));
    }

    @Test
    void update_WhenUpdateDiagnosis_ShouldDiagnosisUpdate() {
        Diagnosis updatedDiagnosis = new Diagnosis(1, "Acid", Arrays.asList(new Treatment("drug")));

        when(diagnosisDao.getDiagnosisById(diagnosis.getDiagnosisId())).thenReturn(diagnosis);
        when(diagnosisDao.updateDiagnosisById(diagnosis.getDiagnosisId(), updatedDiagnosis)).thenReturn(updatedDiagnosis);

        Diagnosis result = diagnosisService.updateDiagnosisById(diagnosis.getDiagnosisId(), updatedDiagnosis);
        Assertions.assertEquals(updatedDiagnosis, result);
    }

    @Test
    void add_WhenAddDiagnosisWithTreatment_ShouldBeAdded() {
        diagnosisService.addDiagnosisToTreatment(diagnosis.getDiagnosisId(), treatment.getTreatmentId());

        verify(diagnosisDao).addDiagnosisToTreatment(diagnosis.getDiagnosisId(), treatment.getTreatmentId());
    }

    @Test
    void delete_WhenDeleteTreatmentAndDiagnosis_ShouldBeDeleted() {
        when(diagnosisDao.getDiagnosisById(diagnosis.getDiagnosisId())).thenReturn(diagnosis);
        when(treatmentDao.getTreatmentById(treatment.getTreatmentId())).thenReturn(treatment);

        diagnosisService.deleteTreatmentsWithDiagnosis(treatment.getTreatmentId(), diagnosis.getDiagnosisId());
        verify(diagnosisDao, times(1)).deleteTreatmentsWithDiagnosis
                (treatment.getTreatmentId(), diagnosis.getDiagnosisId());
    }

    @Test
    void delete_WhenDeleteTreatmentAndDiagnosis_ShouldThrowDiagnosisNotFoundException() {
        when(diagnosisDao.getDiagnosisById(diagnosis.getDiagnosisId())).thenThrow(DiagnosisNotFoundException.class);

        Assertions.assertThrows(DiagnosisNotFoundException.class,
                () -> diagnosisService.deleteTreatmentsWithDiagnosis(treatment.getTreatmentId(),
                        diagnosis.getDiagnosisId()));

        verify(diagnosisDao, never()).deleteTreatmentsWithDiagnosis(treatment.getTreatmentId(), diagnosis.getDiagnosisId());
    }

    @Test
    void delete_WhenDeleteTreatmentAndDiagnosis_ShouldThrowTreatmentNotFoundException() {
        when(treatmentDao.getTreatmentById(treatment.getTreatmentId())).thenThrow(TreatmentNotFoundException.class);

        Assertions.assertThrows(TreatmentNotFoundException.class,
                () -> diagnosisService.deleteTreatmentsWithDiagnosis(treatment.getTreatmentId(),
                        diagnosis.getDiagnosisId()));

        verify(diagnosisDao, never()).deleteTreatmentsWithDiagnosis(treatment.getTreatmentId(), diagnosis.getDiagnosisId());
    }

}
