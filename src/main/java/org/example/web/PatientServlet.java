package org.example.web;

import org.example.dao.impl.PatientDaoImpl;
import org.example.dto.PatientCreateDto;
import org.example.dto.PatientResponseDto;
import org.example.dto.PatientUpdateDto;
import org.example.mapper.PatientMapper;
import org.example.model.Patient;
import org.example.service.PatientService;
import org.example.service.impl.PatientServiceImpl;
import org.example.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/patients")
public class PatientServlet extends HttpServlet {
    private PatientMapper patientMapper = PatientMapper.INSTANCE;
    private PatientService patientService = new PatientServiceImpl(new PatientDaoImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        int id = WebUtil.getIdFromRequest(req);

        Patient patient = patientService.getPatientById(id);
        PatientResponseDto response = patientMapper.toPatientResponseDto(patient);

        PrintWriter writer = resp.getWriter();
        writer.println(WebUtil.getObjectMapper().writeValueAsString(response));

        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String data = WebUtil.getRequestBody(req);

        PatientCreateDto patientCreateDto = WebUtil.getObjectMapper().readValue(data, PatientCreateDto.class);
        Patient patient = patientMapper.toPatient(patientCreateDto);
        patientService.savePatient(patient);

        resp.setStatus(201);
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        int id = WebUtil.getIdFromRequest(req);
        String data = WebUtil.getRequestBody(req);

        PatientUpdateDto patientUpdateDto = WebUtil.getObjectMapper().readValue(data, PatientUpdateDto.class);
        Patient patient = patientMapper.toPatient(patientUpdateDto);
        Patient updatedPatient = patientService.updatePatientById(id, patientUpdateDto.getDoctorId(), patientUpdateDto.getDiagnosisId(), patient);
        PatientResponseDto responseDto = patientMapper.toPatientResponseDto(updatedPatient);

        PrintWriter writer = resp.getWriter();
        writer.println(WebUtil.getObjectMapper().writeValueAsString(responseDto));

        resp.setStatus(201);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = WebUtil.getIdFromRequest(req);

        patientService.removePatient(id);

        PrintWriter writer = resp.getWriter();
        writer.println("The patient with id = " + id + " is removed");

        resp.setStatus(204);

    }
}
