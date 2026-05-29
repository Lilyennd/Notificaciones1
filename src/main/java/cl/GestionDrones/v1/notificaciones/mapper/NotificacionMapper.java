package cl.GestionDrones.v1.notificaciones.mapper;

import cl.GestionDrones.v1.notificaciones.dto.CreateNotificacionRequest;
import cl.GestionDrones.v1.notificaciones.dto.UpdateNotificacionRequest;
import cl.GestionDrones.v1.notificaciones.model.Notificacion;
import java.time.LocalDateTime;

public class NotificacionMapper {

    
    public static Notificacion toModel(CreateNotificacionRequest request) {
        return new Notificacion(
                null, 
                request.tipoDestinatario(), 
                request.idDestinatario(),   
                request.tipoNotificacion(), 
                request.mensaje(),         
                request.estado(),           
                LocalDateTime.now()         
        );
    }

    
    public static Notificacion toModel(Long id, UpdateNotificacionRequest request) {
        return new Notificacion(
                id,                         
                request.tipoDestinatario(), 
                request.idDestinatario(),   
                request.tipoNotificacion(), 
                request.mensaje(),          
                request.estado(),           
                request.fechaCreacion()     
        );
    }
}