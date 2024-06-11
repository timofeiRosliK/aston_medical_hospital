package org.example.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.DiagnosisTreatmentCreateDto;
import org.example.model.Diagnosis;
import org.example.service.DiagnosisService;
import org.example.util.WebUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DiagnosisTreatmentServletTest {
    private int diagnosisId;
    private int treatmentId;
    private DiagnosisTreatmentCreateDto diagnosisTreatmentCreateDto;
    private String data;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;

    @Mock
    private DiagnosisService diagnosisService;

    @BeforeEach
    void setUp() {
        diagnosisId = 1;
        treatmentId = 1;
        diagnosisTreatmentCreateDto = new DiagnosisTreatmentCreateDto(diagnosisId, treatmentId);
        data = "{\"diagnosisId\":\"" + diagnosisId + " \"treatmentId\":\"" + treatmentId + " \"}";

    }

    @InjectMocks
    private DiagnosisTreatmentServlet diagnosisTreatmentServlet;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoPost() throws IOException {
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        try (MockedStatic<WebUtil> webUtilMockedStatic = mockStatic(WebUtil.class)) {
            webUtilMockedStatic.when(WebUtil::getObjectMapper).thenReturn(objectMapperMock);
            webUtilMockedStatic.when(() -> WebUtil.getRequestBody(request)).thenReturn(data);
            when(objectMapperMock.readValue(data, DiagnosisTreatmentCreateDto.class)).thenReturn(diagnosisTreatmentCreateDto);
            doNothing().when(diagnosisService).
                    addDiagnosisToTreatment(diagnosisTreatmentCreateDto.getDiagnosisId(), diagnosisTreatmentCreateDto.getTreatmentId());

            diagnosisTreatmentServlet.doPost(request, response);

            verify(response).setStatus(200);

        }
    }

    @Test
    void testDoDelete() throws IOException {
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        try (MockedStatic<WebUtil> webUtilMockedStatic = mockStatic(WebUtil.class)) {
            webUtilMockedStatic.when(WebUtil::getObjectMapper).thenReturn(objectMapperMock);
            webUtilMockedStatic.when(() -> WebUtil.getRequestBody(request)).thenReturn(data);
            when(objectMapperMock.
                    readValue(data, DiagnosisTreatmentCreateDto.class)).thenReturn(diagnosisTreatmentCreateDto);
            doNothing().when(diagnosisService)
                    .deleteTreatmentsWithDiagnosis(diagnosisTreatmentCreateDto.getTreatmentId(),
                            diagnosisTreatmentCreateDto.getDiagnosisId());

            diagnosisTreatmentServlet.doDelete(request, response);
            verify(response).setStatus(204);
        }
    }
}