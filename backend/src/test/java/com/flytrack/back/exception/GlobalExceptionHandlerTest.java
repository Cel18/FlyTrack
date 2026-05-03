package com.flytrack.back.exception;

import com.flytrack.back.dto.ErrorResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound() {
        ResponseEntity<ErrorResponseDTO> response = handler.handleNotFound(new ResourceNotFoundException("Not found"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleBadRequest() {
        ResponseEntity<ErrorResponseDTO> response = handler.handleBadRequest(new BadRequestException("Bad request"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleAuthentication() {
        ResponseEntity<ErrorResponseDTO> response = handler.handleAuthentication(new AuthenticationException("Auth failed") {});
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void handleAccessDenied() {
        ResponseEntity<ErrorResponseDTO> response = handler.handleAccessDenied(new AccessDeniedException("Denied"));
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void handleGeneric() {
        ResponseEntity<ErrorResponseDTO> response = handler.handleGeneric(new Exception("Generic error"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testExceptionInstantiation() {
        BadRequestException bre = new BadRequestException("msg");
        assertEquals("msg", bre.getMessage());
        
        ResourceNotFoundException rnfe = new ResourceNotFoundException("msg");
        assertEquals("msg", rnfe.getMessage());
    }

    @Test
    void handleValidation() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult br = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(br);
        when(br.getFieldErrors()).thenReturn(List.of(new FieldError("obj", "field", "message")));

        ResponseEntity<ErrorResponseDTO> response = handler.handleValidation(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
