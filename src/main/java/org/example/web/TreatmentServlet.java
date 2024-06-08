package org.example.web;


import org.example.dao.impl.TreatmentDaoImpl;
import org.example.dto.TreatmentCreateDto;
import org.example.dto.TreatmentResponseDto;
import org.example.dto.TreatmentUpdateDto;
import org.example.mapper.TreatmentMapper;
import org.example.model.Treatment;
import org.example.service.TreatmentService;
import org.example.service.impl.TreatmentServiceImpl;
import org.example.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/treatments")
public class TreatmentServlet extends HttpServlet {
    private TreatmentService treatmentService = new TreatmentServiceImpl(new TreatmentDaoImpl());
    private TreatmentMapper treatmentMapper = TreatmentMapper.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        int id = WebUtil.getIdFromRequest(req);
        Treatment treatment = treatmentService.getTreatmentById(id);
        TreatmentResponseDto response = treatmentMapper.toTreatmentResponseDto(treatment);

        PrintWriter writer = resp.getWriter();
        writer.println(WebUtil.getObjectMapper().writeValueAsString(response));

        System.out.println(response);

        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String data = WebUtil.getRequestBody(req);

        TreatmentCreateDto treatmentCreateDto = WebUtil.getObjectMapper().readValue(data, TreatmentCreateDto.class);
        Treatment treatment = treatmentMapper.toTreatment(treatmentCreateDto);

        treatmentService.saveTreatment(treatment);

        resp.setStatus(200);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        int id = WebUtil.getIdFromRequest(req);
        String data = WebUtil.getRequestBody(req);

        TreatmentUpdateDto treatmentUpdateDto = WebUtil.getObjectMapper().readValue(data, TreatmentUpdateDto.class);
        Treatment treatment = treatmentMapper.toTreatment(treatmentUpdateDto);
        Treatment treatmentToDb = treatmentService.updateTreatmentById(id, treatment);
        TreatmentResponseDto treatmentResponseDto = treatmentMapper.toTreatmentResponseDto(treatmentToDb);

        PrintWriter writer = resp.getWriter();
        writer.println(WebUtil.getObjectMapper().writeValueAsString(treatmentResponseDto));

        resp.setStatus(201);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = WebUtil.getIdFromRequest(req);

        treatmentService.removeTreatment(id);

        PrintWriter writer = resp.getWriter();
        writer.println("The treatment with id " + id  + " is removed from db");

        resp.setStatus(204);
    }
}
