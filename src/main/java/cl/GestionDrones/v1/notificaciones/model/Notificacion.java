package cl.GestionDrones.v1.notificaciones.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; 

    @Column(name = "tipo_destinatario", nullable = false, length = 30)
    private String tipoDestinatario; 

    
    @Column(name = "id_destinatario", nullable = false)
    private Long idDestinatario; 

    @Column(name = "tipo_notificacion", nullable = false, length = 50)
    private String tipoNotificacion; 

    @Column(name = "mensaje", nullable = false, length = 500)
    private String mensaje; 

    @Column(name = "estado", nullable = false, length = 20)
    private String estado; 

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion; 

    
    public Notificacion() {
    }

    
    public Notificacion(Long id, String tipoDestinatario, Long idDestinatario, String tipoNotificacion, String mensaje, String estado, LocalDateTime fechaCreacion) {
        this.id = id;
        this.tipoDestinatario = tipoDestinatario;
        this.idDestinatario = idDestinatario;
        this.tipoNotificacion = tipoNotificacion;
        this.mensaje = mensaje;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoDestinatario() {
        return tipoDestinatario;
    }

    public void setTipoDestinatario(String tipoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
    }

    public Long getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(Long idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}