package org.example.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.DiagnosisCreateDto;
import org.example.dto.DiagnosisResponseDto;
import org.example.dto.DiagnosisUpdateDto;
import org.example.dto.TreatmentResponseDto;
import org.example.dto.TreatmentUpdateDto;
import org.example.mapper.DiagnosisMapper;
import org.example.mapper.TreatmentMapper;
import org.example.model.Diagnosis;
import org.example.model.Treatment;
import org.example.service.DiagnosisService;
import org.example.util.WebUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DiagnosisServletTest {
    private int diagnosisId;
    private List<Treatment> treatments;
    private List<Treatment> updatedTreatments;
    private List<TreatmentResponseDto> treatmentResponseDto;
    private List<TreatmentResponseDto> treatmentUpdatedResponseDto;
    private String data;
    private String updateData;
    private DiagnosisCreateDto diagnosisCreateDto;
    private DiagnosisUpdateDto diagnosisUpdateDto;
    private DiagnosisResponseDto diagnosisResponseDto;
    private DiagnosisResponseDto diagnosisUpdateResponseDto;
    private Diagnosis diagnosis;
    private Treatment treatment;
    private Treatment updatedTreatment;
    private Diagnosis updatedDiagnosis;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;

    @Mock
    private DiagnosisService diagnosisService;

    @Mock
    private DiagnosisMapper diagnosisMapper;

    @Mock
    private TreatmentMapper treatmentMapper;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private DiagnosisServlet diagnosisServlet;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        diagnosisId = 1;
        treatment = new Treatment(1, "radiation therapy");
        treatments = List.of(new Treatment(treatment.getTreatmentId(), treatment.getName()));
        diagnosis = new Diagnosis(diagnosisId, "cancer", treatments);
        updatedTreatment = new Treatment(1, "drugs");
        String name = diagnosis.getName();
        treatmentResponseDto = List.of(new TreatmentResponseDto(treatment.getTreatmentId(), treatment.getName()));
        diagnosisResponseDto = new DiagnosisResponseDto(diagnosisId, diagnosis.getName(), treatmentResponseDto);
        treatmentUpdatedResponseDto = List.of(new TreatmentResponseDto(updatedTreatment.getTreatmentId(), updatedTreatment.getName()));
        updatedTreatments = List.of(new Treatment(updatedTreatment.getTreatmentId(), updatedTreatment.getName()));
        diagnosisCreateDto = new DiagnosisCreateDto(diagnosis.getName());
        updatedDiagnosis = new Diagnosis(diagnosisId, "flu", updatedTreatments);
        String updatedName = updatedDiagnosis.getName();
        diagnosisUpdateResponseDto = new DiagnosisResponseDto(updatedDiagnosis.getDiagnosisId(), updatedDiagnosis.getName(), treatmentUpdatedResponseDto);

        diagnosisUpdateDto = new DiagnosisUpdateDto(updatedDiagnosis.getName());
        data = "{\"name\":\"" + name + "\"}";
        updateData = "{\"name\":\"" + updatedName + "\"}";
    }

    @Test
    void testDoGet() throws IOException, ServletException {
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        try (MockedStatic<WebUtil> webUtilMockedStatic = mockStatic(WebUtil.class)) {
            webUtilMockedStatic.when(() -> WebUtil.getIdFromRequest(request)).thenReturn(diagnosisId);
            webUtilMockedStatic.when(WebUtil::getObjectMapper).thenReturn(objectMapperMock);
            when(diagnosisService.getDiagnosisById(diagnosis.getDiagnosisId())).thenReturn(diagnosis);
            when(treatmentMapper.toTreatmentDtoListResponse(treatments)).thenReturn(treatmentResponseDto);
            when(diagnosisMapper.toDiagnosisResponseDto(diagnosis)).thenReturn(diagnosisResponseDto);
            when(response.getWriter()).thenReturn(printWriter);

            diagnosisServlet.doGet(request, response);


            verify(response).setContentType("application/json");
            verify(response).setStatus(200);
        }
    }

    @Test
    void testDoPost() throws ServletException, IOException {
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        try (MockedStatic<WebUtil> webUtilMockedStatic = mockStatic(WebUtil.class)) {
            webUtilMockedStatic.when(WebUtil::getObjectMapper).thenReturn(objectMapperMock);
            webUtilMockedStatic.when(() -> WebUtil.getRequestBody(request)).thenReturn(data);
            when(objectMapperMock.readValue(data, DiagnosisCreateDto.class)).thenReturn(diagnosisCreateDto);
            when(diagnosisMapper.toDiagnosis(diagnosisCreateDto)).thenReturn(diagnosis);
            Mockito.doNothing().when(diagnosisService).saveDiagnosis(diagnosis);

            diagnosisServlet.doPost(request, response);

            verify(response).setStatus(201);
        }
    }

    @Test
    void testDoPut() throws IOException, ServletException {
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        try (MockedStatic<WebUtil> webUtilMockedStatic = mockStatic(WebUtil.class)) {
            webUtilMockedStatic.when(() -> WebUtil.getIdFromRequest(request)).thenReturn(diagnosisId);
            webUtilMockedStatic.when(WebUtil::getObjectMapper).thenReturn(objectMapperMock);
            webUtilMockedStatic.when(() -> WebUtil.getRequestBody(request)).thenReturn(updateData);
            when(objectMapperMock.readValue(updateData, DiagnosisUpdateDto.class)).thenReturn(diagnosisUpdateDto);
            when(diagnosisMapper.toDiagnosis(diagnosisUpdateDto)).thenReturn(updatedDiagnosis);
            when(diagnosisService.updateDiagnosisById(diagnosis.getDiagnosisId(), updatedDiagnosis)).thenReturn(updatedDiagnosis);
            when(treatmentMapper.toTreatmentDtoListResponse(updatedTreatments)).thenReturn(treatmentUpdatedResponseDto);
            when(diagnosisMapper.toDiagnosisResponseDto(updatedDiagnosis)).thenReturn(diagnosisUpdateResponseDto);
            when(diagnosisService.updateDiagnosisById(diagnosis.getDiagnosisId(), updatedDiagnosis)).
                    thenReturn(updatedDiagnosis);
            when(response.getWriter()).thenReturn(printWriter);

            diagnosisServlet.doPut(request, response);

            verify(response).setContentType("application/JSON");
            verify(response).setStatus(200);
        }
    }

}