package org.example.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.TreatmentCreateDto;
import org.example.dto.TreatmentResponseDto;
import org.example.dto.TreatmentUpdateDto;
import org.example.mapper.TreatmentMapper;
import org.example.model.Treatment;
import org.example.service.TreatmentService;
import org.example.util.WebUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TreatmentServletTest {
    private int treatmentId;
    private String data;
    private TreatmentCreateDto treatmentCreateDto;
    private TreatmentUpdateDto treatmentUpdateDto;
    private TreatmentResponseDto treatmentResponseDto;
    private Treatment treatment;
    private Treatment updatedTreatment;
    private String updatedData;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;

    @Mock
    private TreatmentService treatmentService;

    @Mock
    private TreatmentMapper treatmentMapper;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private TreatmentServlet treatmentServlet;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        treatmentId = 1;
        treatment = new Treatment(treatmentId, "radiation therapy");
        updatedTreatment = new Treatment(treatmentId, "updatedTreatment");
        treatmentCreateDto = new TreatmentCreateDto(treatment.getName());
        treatmentUpdateDto = new TreatmentUpdateDto(updatedTreatment.getName());
        String name = treatmentCreateDto.getName();
        String updatedName = updatedTreatment.getName();
        treatmentResponseDto = new TreatmentResponseDto(treatmentId, treatment.getName());
        data = "{\"name\":\"" + name + "\"}";
        updatedData = "{\"name\":\"" + updatedName + "\"}";
    }

    @Test
    void testDoGet() throws ServletException, IOException {
        try (MockedStatic<WebUtil> webUtilMockedStatic = mockStatic(WebUtil.class)) {
            webUtilMockedStatic.when(() -> WebUtil.getIdFromRequest(request)).thenReturn(treatmentId);
            when(treatmentService.getTreatmentById(treatmentId)).thenReturn(treatment);
            when(treatmentMapper.toTreatmentResponseDto(treatment)).thenReturn(treatmentResponseDto);
            when(response.getWriter()).thenReturn(printWriter);

            webUtilMockedStatic.when(WebUtil::getObjectMapper).thenReturn(new ObjectMapper());
            treatmentServlet.doGet(request, response);

            verify(response).setContentType("application/json");
            verify(response).setStatus(200);
            verify(response.getWriter()).println(anyString());
        }
    }

    @Test
    void testDoPost() throws ServletException, IOException {
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        try (MockedStatic<WebUtil> webUtilMockedStatic = mockStatic(WebUtil.class)) {
            webUtilMockedStatic.when(WebUtil::getObjectMapper).thenReturn(objectMapperMock);
            webUtilMockedStatic.when(() -> WebUtil.getRequestBody(request)).thenReturn(data);
            when(objectMapperMock.readValue(data, TreatmentCreateDto.class)).thenReturn(treatmentCreateDto);
            when(treatmentMapper.toTreatment(treatmentCreateDto)).thenReturn(treatment);
            doNothing().when(treatmentService).saveTreatment(treatment);
            treatmentServlet.doPost(request, response);

            verify(response).setStatus(200);
        }
    }

    @Test
    void doPut() throws IOException, ServletException {
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        try (MockedStatic<WebUtil> webUtilMockedStatic = mockStatic(WebUtil.class)) {
            webUtilMockedStatic.when(() -> WebUtil.getIdFromRequest(request)).thenReturn(treatmentId);
            webUtilMockedStatic.when(WebUtil::getObjectMapper).thenReturn(objectMapperMock);
            webUtilMockedStatic.when(() -> WebUtil.getRequestBody(request)).thenReturn(updatedData);
            when(objectMapperMock.readValue(updatedData, TreatmentUpdateDto.class)).thenReturn(treatmentUpdateDto);
            when(treatmentMapper.toTreatment(treatmentUpdateDto)).thenReturn(updatedTreatment);
            when(treatmentService.updateTreatmentById(treatment.getTreatmentId(), updatedTreatment)).thenReturn(updatedTreatment);
            when(response.getWriter()).thenReturn(printWriter);


            treatmentServlet.doPut(request, response);

            verify(response).setContentType("application/json");
            verify(response).setStatus(201);
        }
    }

    @Test
    void doDelete() throws IOException, ServletException {
        try (MockedStatic<WebUtil> webUtilMockedStatic = mockStatic(WebUtil.class)) {
            webUtilMockedStatic.when(() -> WebUtil.getIdFromRequest(request)).thenReturn(treatmentId);
            when(response.getWriter()).thenReturn(printWriter);

            treatmentServlet.doDelete(request, response);

            verify(treatmentService).removeTreatment(treatmentId);
            verify(response.getWriter()).println("The treatment with id " + treatmentId + " is removed from db");
            verify(response).setStatus(204);

        }
    }
}