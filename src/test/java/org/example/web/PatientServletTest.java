package org.example.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.PatientCreateDto;
import org.example.dto.PatientResponseDto;
import org.example.dto.PatientUpdateDto;
import org.example.mapper.PatientMapper;
import org.example.model.Diagnosis;
import org.example.model.Doctor;
import org.example.model.Gender;
import org.example.model.Patient;
import org.example.service.PatientService;
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

class PatientServletTest {
    private int patientId;
    private Patient patient;
    private Patient updatedPatient;
    private PatientCreateDto patientCreateDto;
    private PatientUpdateDto patientUpdateDto;
    private PatientResponseDto patientResponseDto;
    private String data;
    private String updatedData;
    private Doctor doctor;
    private Diagnosis diagnosis;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;

    @Mock
    private PatientService patientService;

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private PatientServlet patientServlet;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        patientId = 1;
        patient = new Patient(patientId, "Ivan", "Ivanov", Gender.MAN);
        updatedPatient = new Patient(patientId, "Tonya", "Morozova", Gender.WOMAN);
        doctor = new Doctor(1, "Galya", "Ivanova", "oncologist");
        diagnosis = new Diagnosis(1, "cancer");
        patientResponseDto = new PatientResponseDto(patientId, patient.getFirstName(), patient.getLastName(),
                patient.getGender());
        patientUpdateDto = new PatientUpdateDto(updatedPatient.getFirstName(), updatedPatient.getLastName(),
                updatedPatient.getGender(), doctor.getId(), diagnosis.getDiagnosisId());
        data = "{\"name\":\"" + patient.getFirstName() + "\"}";
        updatedData = "{\"name\":\"" + updatedPatient.getFirstName() + "\"}";
    }

    @Test
    void testdoGet() throws ServletException, IOException {
        try (MockedStatic<WebUtil> webUtilMockedStatic = mockStatic(WebUtil.class)) {
            webUtilMockedStatic.when(() -> WebUtil.getIdFromRequest(request)).thenReturn(patientId);
            when(patientService.getPatientById(patientId)).thenReturn(patient);
            when(patientMapper.toPatientResponseDto(patient)).thenReturn(patientResponseDto);
            when(response.getWriter()).thenReturn(printWriter);

            webUtilMockedStatic.when(WebUtil::getObjectMapper).thenReturn(new ObjectMapper());
            patientServlet.doGet(request, response);

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
            when(objectMapperMock.readValue(data, PatientCreateDto.class)).thenReturn(patientCreateDto);
            when(patientMapper.toPatient(patientCreateDto)).thenReturn(patient);
            doNothing().when(patientService).savePatient(patient);

            patientServlet.doPost(request, response);

            verify(response).setStatus(201);
        }
    }


    @Test
    void testDoPut() throws IOException, ServletException {
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        try (MockedStatic<WebUtil> webUtilMockedStatic = mockStatic(WebUtil.class)) {
            webUtilMockedStatic.when(() -> WebUtil.getIdFromRequest(request)).thenReturn(patientId);
            webUtilMockedStatic.when(WebUtil::getObjectMapper).thenReturn(objectMapperMock);
            webUtilMockedStatic.when(() -> WebUtil.getRequestBody(request)).thenReturn(updatedData);
            when(objectMapperMock.readValue(updatedData, PatientUpdateDto.class)).thenReturn(patientUpdateDto);
            when(patientMapper.toPatient(patientUpdateDto)).thenReturn(updatedPatient);
            when(patientService.updatePatientById(patient.getId(), doctor.getId(), diagnosis.getDiagnosisId(), updatedPatient))
                    .thenReturn(updatedPatient);
            when(response.getWriter()).thenReturn(printWriter);

            patientServlet.doPut(request, response);

            verify(response).setContentType("application/json");
            verify(response).setStatus(201);
        }
    }

    @Test
    void doDelete() throws ServletException, IOException {
        try (MockedStatic<WebUtil> webUtilMockedStatic = mockStatic(WebUtil.class)) {
            webUtilMockedStatic.when(() -> WebUtil.getIdFromRequest(request)).thenReturn(patientId);
            when(response.getWriter()).thenReturn(printWriter);

            patientServlet.doDelete(request, response);

            verify(patientService).removePatient(patientId);
            ;
            verify(response.getWriter()).println("The patient with id = " + patientId + " is removed");
            verify(response).setStatus(204);
        }
    }
}