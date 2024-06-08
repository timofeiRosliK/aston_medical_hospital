package org.example.web;

import org.example.dao.impl.DiagnosisDaoImpl;
import org.example.dao.impl.TreatmentDaoImpl;
import org.example.dto.DiagnosisCreateDto;
import org.example.dto.DiagnosisResponseDto;
import org.example.dto.DiagnosisUpdateDto;
import org.example.dto.TreatmentResponseDto;
import org.example.mapper.DiagnosisMapper;
import org.example.mapper.TreatmentMapper;
import org.example.model.Diagnosis;
import org.example.model.Treatment;
import org.example.service.DiagnosisService;
import org.example.service.impl.DiagnosisServiceImpl;
import org.example.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/diagnoses")
public class DiagnosisServlet extends HttpServlet {
    private DiagnosisService diagnosisService = new DiagnosisServiceImpl(new DiagnosisDaoImpl(), new TreatmentDaoImpl());
    private DiagnosisMapper diagnosisMapper = DiagnosisMapper.INSTANCE;
    private TreatmentMapper treatmentMapper = TreatmentMapper.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        int id = WebUtil.getIdFromRequest(req);

        Diagnosis diagnosis = diagnosisService.getDiagnosisById(id);
        List<Treatment> treatmentList = diagnosis.getTreatmentList();
        List<TreatmentResponseDto> treatmentsDto = treatmentMapper.toTreatmentDtoListResponse(treatmentList);
        DiagnosisResponseDto response = diagnosisMapper.toDiagnosisResponseDto(diagnosis);
        response.setTreatments(treatmentsDto);

        PrintWriter writer = resp.getWriter();
        writer.println(WebUtil.getObjectMapper().writeValueAsString(response));

        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String data = WebUtil.getRequestBody(req);

        DiagnosisCreateDto diagnosisCreateDto = WebUtil.getObjectMapper().readValue(data, DiagnosisCreateDto.class);
        Diagnosis diagnosis = diagnosisMapper.toDiagnosis(diagnosisCreateDto);
        diagnosisService.saveDiagnosis(diagnosis);

        resp.setStatus(201);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/JSON");
        int id = WebUtil.getIdFromRequest(req);
        String data = WebUtil.getRequestBody(req);

        DiagnosisUpdateDto dto = WebUtil.getObjectMapper().readValue(data, DiagnosisUpdateDto.class);
        Diagnosis diagnosis = diagnosisMapper.toDiagnosis(dto);
        Diagnosis updatedDiagnosis = diagnosisService.updateDiagnosisById(id, diagnosis);
        List<Treatment> treatmentList = updatedDiagnosis.getTreatmentList();
        List<TreatmentResponseDto> treatmentResponse = treatmentMapper.toTreatmentDtoListResponse(treatmentList);
        DiagnosisResponseDto responseDto = diagnosisMapper.toDiagnosisResponseDto(updatedDiagnosis);
        responseDto.setTreatments(treatmentResponse);

        PrintWriter writer = resp.getWriter();
        writer.println(WebUtil.getObjectMapper().writeValueAsString(responseDto));

        resp.setStatus(200);
    }
}
