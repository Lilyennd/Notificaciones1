package cl.GestionDrones.v1.notificaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.GestionDrones.v1.notificaciones.model.Notificacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    @Query(value = "SELECT * FROM notificaciones WHERE id = :id", nativeQuery = true)
    Optional<Notificacion> selectPorId(@Param("id") Long id);

    // 1. Conservamos este por si otros servicios o componentes lo usan
    @Query(value = "SELECT * FROM notificaciones WHERE id_destinatario = :idDestinatario AND tipo_destinatario = :tipoDestinatario", nativeQuery = true)
    List<Notificacion> selectPorDestinatario(@Param("idDestinatario") Long idDestinatario, @Param("tipoDestinatario") String tipoDestinatario);

    // 🌟 NUEVO: Query nativa para buscar SOLO por ID de destinatario
    @Query(value = "SELECT * FROM notificaciones WHERE id_destinatario = :idDestinatario", nativeQuery = true)
    List<Notificacion> selectPorIdDestinatario(@Param("idDestinatario") Long idDestinatario);

    // 🌟 NUEVO: Query nativa para buscar SOLO por Tipo de destinatario
    @Query(value = "SELECT * FROM notificaciones WHERE tipo_destinatario = :tipoDestinatario", nativeQuery = true)
    List<Notificacion> selectPorTipoDestinatario(@Param("tipoDestinatario") String tipoDestinatario);

    @Query(value = "SELECT * FROM notificaciones WHERE estado = :estado", nativeQuery = true)
    List<Notificacion> selectPorEstado(@Param("estado") String estado);

    @Query(value = "SELECT * FROM notificaciones WHERE tipo_notificacion = :tipoNotificacion", nativeQuery = true)
    List<Notificacion> selectPorTipoNotificacion(@Param("tipoNotificacion") String tipoNotificacion);

    default int totalNotificaciones() {
        return (int) this.count(); 
    }
}
