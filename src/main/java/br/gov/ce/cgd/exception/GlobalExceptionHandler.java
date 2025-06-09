package br.gov.ce.cgd.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Controller
@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(UsuarioNaoEncontradoException.class)
//    public ResponseEntity<Map<String, String>> handleUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
//        Map<String, String> response = new HashMap<>();
//        response.put("erro", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//    }

    @ExceptionHandler(DoisFAAJaHabilitadoException.class)
    public ResponseEntity<Map<String, String>> handleDoisFAAJaHabilitado(DoisFAAJaHabilitadoException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("erro", "Ocorreu um erro interno. Tente novamente mais tarde.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
