package cl.GestionDrones.v1.notificaciones.exception;

public class NotificacionInvalidaException extends RuntimeException {
    
    public NotificacionInvalidaException(String mensaje) {
        super(mensaje);
    }
    
    public NotificacionInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
