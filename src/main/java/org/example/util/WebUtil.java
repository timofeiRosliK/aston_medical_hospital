package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class WebUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private WebUtil() {
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static int getIdFromRequest(HttpServletRequest request){
         return Integer.parseInt(request.getParameter("id"));
    }

    public static String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader bufferedReader = request.getReader();

        while ((line = bufferedReader.readLine()) != null){
            sb.append(line);
        }

        return sb.toString();
    }

}
