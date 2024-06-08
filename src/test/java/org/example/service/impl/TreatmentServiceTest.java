package org.example.service.impl;

import org.example.dao.TreatmentDao;
import org.example.exceptions.TreatmentNotFoundException;
import org.example.model.Treatment;
import org.example.service.impl.TreatmentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TreatmentServiceTest {
    private Treatment treatment;

    @Mock
    private TreatmentDao treatmentDao;

    @InjectMocks
    private TreatmentServiceImpl treatmentService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        treatment = new Treatment(1, "radiation therapy");
    }

    @Test
    void save_WhenSaveTreatmentInDb_ShouldTreatmentSaved() {
        treatmentService.saveTreatment(treatment);

        verify(treatmentDao, times(1)).saveTreatment(treatment);
    }

    @Test
    void get_WhenGetTreatmentById_ShouldTreatmentGot() {
        when(treatmentDao.getTreatmentById(treatment.getTreatmentId())).thenReturn(treatment);

        Treatment result = treatmentService.getTreatmentById(treatment.getTreatmentId());

        Assertions.assertEquals(treatment, result);
    }

    @Test
    void get_WhenGetTreatment_ShouldThrowTreatmentNotException() {
        Treatment drugTherapy = new Treatment(2, "drugTherapy");

        when(treatmentDao.getTreatmentById(drugTherapy.getTreatmentId())).thenThrow(TreatmentNotFoundException.class);

        Assertions.assertThrows(TreatmentNotFoundException.class, () ->
                treatmentService.getTreatmentById(drugTherapy.getTreatmentId()));
    }

    @Test
    void update_WhenUpdateTreatment_ShouldTreatmentUpdate() {
        Treatment updatedTreatment = new Treatment(1, "chemotherapy");

        when(treatmentDao.getTreatmentById((treatment.getTreatmentId()))).thenReturn(treatment);
        when(treatmentDao.updateTreatmentById(treatment.getTreatmentId(), updatedTreatment)).thenReturn(updatedTreatment);

        Treatment result = treatmentService.updateTreatmentById(treatment.getTreatmentId(), updatedTreatment);
        Assertions.assertEquals(updatedTreatment, result);
    }


    @Test
    void delete_WhenDeleteTreatment_ShouldTreatmentBeDeleted() {
        when(treatmentDao.getTreatmentById(treatment.getTreatmentId())).thenReturn(treatment);

        treatmentService.removeTreatment(treatment.getTreatmentId());

        verify(treatmentDao, times(1)).removeTreatment(treatment.getTreatmentId());
    }

    @Test
    void delete_WhenDeleteTreatment_ShouldThrowTreatmentNotFoundException() {
        when(treatmentDao.getTreatmentById(treatment.getTreatmentId())).thenThrow(TreatmentNotFoundException.class);

        Assertions.assertThrows(TreatmentNotFoundException.class,
                () -> treatmentService.removeTreatment(treatment.getTreatmentId()));

        verify(treatmentDao, never()).removeTreatment(treatment.getTreatmentId());
    }


}
