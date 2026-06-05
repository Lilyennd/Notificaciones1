package cl.GestionDrones.v1.notificaciones.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.GestionDrones.v1.notificaciones.model.Notificacion;
import cl.GestionDrones.v1.notificaciones.repository.NotificacionRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    
    public List<Notificacion> getNotificaciones() {
        return notificacionRepository.findAll();
    }

    
    public Notificacion saveNotificacion(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    
    public Notificacion getNotificacionId(Long id) {
        return notificacionRepository.selectPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("La notificación con ID " + id + " no existe en el sistema DGAC."));
    }

    
    public Notificacion updateNotificacion(Notificacion notificacion) {
        if (notificacion.getId() == null || !notificacionRepository.selectPorId(notificacion.getId()).isPresent()) {
            throw new EntityNotFoundException("No se puede actualizar: La notificación no existe.");
        }
        return notificacionRepository.save(notificacion);
    }

    
    public String deleteNotificacion(Long id) {
        if (!notificacionRepository.selectPorId(id).isPresent()) {
            throw new EntityNotFoundException("No se puede eliminar: La notificación con ID " + id + " no existe.");
        }
        notificacionRepository.deleteById(id);
        return "Notificación eliminada exitosamente del sistema DGAC.";
    }

    
    public int totalNotificaciones() {
        return (int) notificacionRepository.count();
    }

    
    public int totalNotificacionesV2() {
        return notificacionRepository.totalNotificaciones();
    }

    
    public List<Notificacion> obtenerPorIdDestinatario(Long idDestinatario) {
    return notificacionRepository.selectPorIdDestinatario(idDestinatario);
}

    public List<Notificacion> obtenerPorTipoDestinatario(String tipoDestinatario) {
    return notificacionRepository.selectPorTipoDestinatario(tipoDestinatario);
}

    
    public List<Notificacion> obtenerPorEstado(String estado) {
        return notificacionRepository.selectPorEstado(estado);
    }

    
    public List<Notificacion> obtenerPorTipoNotificacion(String tipoNotificacion) {
        return notificacionRepository.selectPorTipoNotificacion(tipoNotificacion);
    }
}
