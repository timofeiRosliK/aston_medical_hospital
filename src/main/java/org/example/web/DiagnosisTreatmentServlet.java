package org.example.web;

import org.example.dao.impl.DiagnosisDaoImpl;
import org.example.dao.impl.TreatmentDaoImpl;
import org.example.dto.DiagnosisTreatmentCreateDto;
import org.example.service.DiagnosisService;
import org.example.service.impl.DiagnosisServiceImpl;
import org.example.util.WebUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/diagnoses/treatments")
public class DiagnosisTreatmentServlet extends HttpServlet {
    private DiagnosisService diagnosisService = new DiagnosisServiceImpl(new DiagnosisDaoImpl(), new TreatmentDaoImpl());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = WebUtil.getRequestBody(req);

        DiagnosisTreatmentCreateDto dto = WebUtil.getObjectMapper().readValue(data, DiagnosisTreatmentCreateDto.class);

        diagnosisService.addDiagnosisToTreatment(dto.getDiagnosisId(), dto.getTreatmentId());

        resp.setStatus(200);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = WebUtil.getRequestBody(req);

        DiagnosisTreatmentCreateDto dto = WebUtil.getObjectMapper().readValue(data, DiagnosisTreatmentCreateDto.class);

        diagnosisService.deleteTreatmentsWithDiagnosis(dto.getTreatmentId(),dto.getDiagnosisId());

        resp.setStatus(204);
    }

}
