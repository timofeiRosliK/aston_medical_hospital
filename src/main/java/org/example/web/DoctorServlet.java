package org.example.web;

import org.example.dao.impl.DoctorDaoImpl;
import org.example.dto.DoctorCreateDto;
import org.example.dto.DoctorResponseDto;
import org.example.dto.DoctorUpdateDto;
import org.example.mapper.DoctorMapper;
import org.example.model.Doctor;
import org.example.service.DoctorService;
import org.example.service.impl.DoctorServiceImpl;
import org.example.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/doctors")
public class DoctorServlet extends HttpServlet {
    private DoctorService doctorService = new DoctorServiceImpl(new DoctorDaoImpl());
    private DoctorMapper doctorMapper = DoctorMapper.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        int id = WebUtil.getIdFromRequest(req);

        Doctor doctor = doctorService.getDoctorById(id);
        DoctorResponseDto response = doctorMapper.toDoctorResponseDto(doctor);

        PrintWriter writer = resp.getWriter();
        writer.println(WebUtil.getObjectMapper().writeValueAsString(response));

        resp.setStatus(201);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String data = WebUtil.getRequestBody(req);

        DoctorCreateDto doctorCreateDto = WebUtil.getObjectMapper().readValue(data.toString(), DoctorCreateDto.class);
        Doctor doctor = doctorMapper.toDoctor(doctorCreateDto);
        doctorService.saveDoctor(doctor);

        resp.setStatus(200);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        int id = WebUtil.getIdFromRequest(req);
        String data = WebUtil.getRequestBody(req);

        DoctorUpdateDto doctorUpdateDto = WebUtil.getObjectMapper().readValue(data.toString(), DoctorUpdateDto.class);
        Doctor doctor = doctorMapper.toDoctor(doctorUpdateDto);
        Doctor updatedDoctor = doctorService.updateDoctorById(id, doctor);
        DoctorResponseDto response = doctorMapper.toDoctorResponseDto(updatedDoctor);

        PrintWriter writer = resp.getWriter();
        writer.println(WebUtil.getObjectMapper().writeValueAsString(response));

        resp.setStatus(201);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = WebUtil.getIdFromRequest(req);

        doctorService.removeDoctor(id);

        PrintWriter writer = resp.getWriter();
        writer.println("The doctor with id =" + id + " is removed");

        resp.setStatus(204);
    }
}
