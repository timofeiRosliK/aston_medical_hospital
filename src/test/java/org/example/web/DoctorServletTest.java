package org.example.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.DoctorCreateDto;
import org.example.dto.DoctorResponseDto;
import org.example.dto.DoctorUpdateDto;
import org.example.mapper.DoctorMapper;
import org.example.model.Doctor;
import org.example.service.DoctorService;
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

class DoctorServletTest {
    private int doctorId;
    private Doctor doctor;
    private Doctor updatedDoctor;
    private DoctorCreateDto doctorCreateDto;
    private DoctorUpdateDto doctorUpdateDto;
    private DoctorResponseDto doctorResponseDto;
    private String data;
    private String updatedData;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;

    @Mock
    private DoctorService doctorService;

    @Mock
    private DoctorMapper doctorMapper;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private DoctorServlet doctorServlet;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        doctorId = 1;
        doctor = new Doctor(doctorId, "Galina", "Ivanova", "oncologist");
        updatedDoctor = new Doctor(doctorId, "Marya", "Lapteva", "surgeon");
        doctorCreateDto = new DoctorCreateDto(doctor.getFirstName(), doctor.getLastName(), doctor.getSpecialization());
        doctorUpdateDto = new DoctorUpdateDto(updatedDoctor.getFirstName(), updatedDoctor.getLastName(),
                updatedDoctor.getSpecialization());
        doctorResponseDto = new DoctorResponseDto(doctorId, doctor.getFirstName(), doctor.getLastName(), doctor.getSpecialization());
                updatedDoctor.getSpecialization();
        String name = doctor.getFirstName();
        String updatedName = updatedDoctor.getFirstName();
        data = "{\"name\":\"" + name + "\"}";
        updatedData = "{\"name\":\"" + updatedName + "\"}";

    }


    @Test
    void testDoGet() throws IOException, ServletException {
        try (MockedStatic<WebUtil> webUtilMockedStatic = mockStatic(WebUtil.class)) {
            webUtilMockedStatic.when(() -> WebUtil.getIdFromRequest(request)).thenReturn(doctorId);
            when(doctorService.getDoctorById(doctorId)).thenReturn(doctor);
            when(doctorMapper.toDoctorResponseDto(doctor)).thenReturn(doctorResponseDto);
            when(response.getWriter()).thenReturn(printWriter);

            webUtilMockedStatic.when(WebUtil::getObjectMapper).thenReturn(new ObjectMapper());
            doctorServlet.doGet(request, response);

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
            when(objectMapperMock.readValue(data, DoctorCreateDto.class)).thenReturn(doctorCreateDto);
            when(doctorMapper.toDoctor(doctorCreateDto)).thenReturn(doctor);
            doNothing().when(doctorService).saveDoctor(doctor);

            doctorServlet.doPost(request, response);

            verify(response).setStatus(200);
        }
    }


    @Test
    void testDoPut() throws IOException, ServletException {
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        try (MockedStatic<WebUtil> webUtilMockedStatic = mockStatic(WebUtil.class)) {
            webUtilMockedStatic.when(() -> WebUtil.getIdFromRequest(request)).thenReturn(doctorId);
            webUtilMockedStatic.when(WebUtil::getObjectMapper).thenReturn(objectMapperMock);
            webUtilMockedStatic.when(() -> WebUtil.getRequestBody(request)).thenReturn(updatedData);
            when(objectMapperMock.readValue(updatedData, DoctorUpdateDto.class)).thenReturn(doctorUpdateDto);
            when(doctorMapper.toDoctor(doctorUpdateDto)).thenReturn(updatedDoctor);
            when(doctorService.updateDoctorById(doctor.getId(), updatedDoctor)).thenReturn(updatedDoctor);
            when(response.getWriter()).thenReturn(printWriter);

            doctorServlet.doPut(request, response);

            verify(response).setContentType("application/json");
            verify(response).setStatus(201);
        }
    }
        @Test
        void testDoDelete () throws IOException, ServletException {
            try (MockedStatic<WebUtil> webUtilMockedStatic = mockStatic(WebUtil.class)) {
                webUtilMockedStatic.when(() -> WebUtil.getIdFromRequest(request)).thenReturn(doctorId);
                when(response.getWriter()).thenReturn(printWriter);

                doctorServlet.doDelete(request, response);

                verify(doctorService).removeDoctor(doctorId);
                verify(response.getWriter()).println("The doctor with id =" + doctorId + " is removed");
                verify(response).setStatus(204);

            }
        }
    }
