package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebUtilTest {

    @Test
    void testGetObjectMapper() {
        ObjectMapper objectMapper = WebUtil.getObjectMapper();
        assertNotNull(objectMapper);
    }

    @Test
    void testGetIdFromRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("id")).thenReturn("123");

        int id = WebUtil.getIdFromRequest(request);

        assertEquals(123, id);
    }

    @Test
    void testGetIdFromRequestInvalidNumber() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("id")).thenReturn("abc");

        assertThrows(NumberFormatException.class, () -> WebUtil.getIdFromRequest(request));
    }

    @Test
    void testGetRequestBody() throws IOException {
        String requestBody = "{\"name\":\"John\"}";
        HttpServletRequest request = mock(HttpServletRequest.class);
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        Mockito.when(request.getReader()).thenReturn(reader);

        String result = WebUtil.getRequestBody(request);

        assertEquals(requestBody, result);
    }

    @Test
    void testGetRequestBodyEmpty() throws IOException {
        String requestBody = "";
        HttpServletRequest request = mock(HttpServletRequest.class);
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);

        String result = WebUtil.getRequestBody(request);

        assertEquals(requestBody, result);
    }

    @Test
    void testGetRequestBodyIOException() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getReader()).thenThrow(new IOException("Test IOException"));

        assertThrows(IOException.class, () -> WebUtil.getRequestBody(request));
    }
}
