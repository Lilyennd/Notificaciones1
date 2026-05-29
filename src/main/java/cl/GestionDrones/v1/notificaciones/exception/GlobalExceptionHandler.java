package cl.GestionDrones.v1.notificaciones.exception;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        System.out.println("✅ GlobalExceptionHandler de Notificaciones SE HA REGISTRADO CORRECTAMENTE");
    }

    
    @ExceptionHandler(NotificacionInvalidaException.class)
    public ProblemDetail handleNotificacionInvalida(NotificacionInvalidaException ex) {
        System.out.println("🔴 GlobalExceptionHandler EJECUTADO - Violación de regla de negocio en alerta: " + ex.getMessage());

       
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(422), 
                ex.getMessage()
        );

        problem.setTitle("Regala de Negocio de Notificación Violada");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("codigo_error", "DGAC-ERR-NOTIFICACION-NEGOCIO");
        
        return problem;
    }

    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(jakarta.persistence.EntityNotFoundException ex) {
        System.out.println("🔴 GlobalExceptionHandler EJECUTADO - Notificación no encontrada: " + ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, 
                ex.getMessage()
        );

        problem.setTitle("Notificación No Registrada");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("codigo_error", "DGAC-ERR-NOTIFICACION-404");
        
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        System.out.println("🔴 GlobalExceptionHandler EJECUTADO - Errores de validación en Notificación detectados");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Error de validación en los datos de la notificación enviados");

        problem.setTitle("Notificación Validation Error");
        problem.setProperty("timestamp", Instant.now());

       
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage()
                                : "Valor inválido"));

        problem.setProperty("errors", errors);

        System.out.println("🔴 Errores encontrados en la notificación: " + errors);
        return problem;
    }

    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleJsonParseError(HttpMessageNotReadableException ex) {
        System.out.println("🟡 Error de parseo JSON capturado en Notificaciones");
        System.out.println("🟡 Mensaje: " + ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Error al procesar el JSON enviado para la notificación");

        problem.setTitle("JSON Parse Error");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("detalle", ex.getMostSpecificCause().getMessage());
        return problem;
    }

    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());

        problem.setTitle("Notificación Not Found");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        System.out.println("🔴 EXCEPCIÓN CAPTURADA EN NOTIFICACIONES: " + ex.getClass().getName());
        System.out.println("🔴 Mensaje: " + ex.getMessage());
        ex.printStackTrace();

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor en el microservicio de notificaciones");

        problem.setTitle("Internal Server Error");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("detalle", ex.getMessage());
        problem.setProperty("tipoExcepcion", ex.getClass().getSimpleName());
        return problem;
    }
}