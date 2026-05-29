package cl.GestionDrones.v1.notificaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;


public record CreateNotificacionRequest(

        @NotBlank(message = "El tipo de destinatario no puede ser vacío")
        @Size(max = 30, message = "El tipo de destinatario no puede superar los 30 caracteres")
        String tipoDestinatario, 

        @NotNull(message = "El ID del destinatario es obligatorio")
        Long idDestinatario, 

        @NotBlank(message = "El tipo de notificación no puede ser vacío")
        @Size(max = 50, message = "El tipo de notificación no puede superar los 50 caracteres")
        String tipoNotificacion, 

        @NotBlank(message = "El mensaje no puede ser vacío")
        @Size(max = 500, message = "El mensaje no puede superar los 500 caracteres")
        String mensaje, 

        @NotBlank(message = "El estado inicial no puede ser vacío")
        @Size(max = 20, message = "El estado no puede superar los 20 caracteres")
        String estado, 

        @NotNull(message = "La fecha de creación es obligatoria")
        LocalDateTime fechaCreacion 
) {}