package cl.GestionDrones.v1.notificaciones.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.GestionDrones.v1.notificaciones.dto.CreateNotificacionRequest;
import cl.GestionDrones.v1.notificaciones.dto.UpdateNotificacionRequest;
import cl.GestionDrones.v1.notificaciones.mapper.NotificacionMapper;
import cl.GestionDrones.v1.notificaciones.model.Notificacion;
import cl.GestionDrones.v1.notificaciones.service.NotificacionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/notificaciones")
public class NotificacionController {

        private final NotificacionService notificacionService;

        public NotificacionController(NotificacionService notificacionService) {
                this.notificacionService = notificacionService;
        }

        @GetMapping
        public ResponseEntity<List<Notificacion>> listarNotificaciones() {
                List<Notificacion> notificaciones = notificacionService.getNotificaciones();
                
                if (notificaciones.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                
                return ResponseEntity.ok(notificaciones);
        }

        @PostMapping
        public ResponseEntity<Notificacion> agregarNotificacion(@Valid @RequestBody CreateNotificacionRequest request) {
                
                if (request.mensaje() == null || request.mensaje().trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                
                if (request.tipoDestinatario() == null || request.tipoDestinatario().trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                Notificacion nuevaNotificacion = notificacionService.saveNotificacion(NotificacionMapper.toModel(request));
                
                if (nuevaNotificacion == null) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                
                return ResponseEntity.status(HttpStatus.CREATED).body(nuevaNotificacion);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Notificacion> buscarNotificacion(@PathVariable Long id) {
                if (id <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                Notificacion notificacion = notificacionService.getNotificacionId(id);
                
                if (notificacion == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                
                return ResponseEntity.ok(notificacion);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Notificacion> actualizarNotificacion(@PathVariable Long id,
                        @Valid @RequestBody UpdateNotificacionRequest request) {
                
                if (id <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                if (request.estado() == null || request.estado().trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                Notificacion notificacionActualizada = notificacionService.updateNotificacion(NotificacionMapper.toModel(id, request));
                
                if (notificacionActualizada == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                
                return ResponseEntity.ok(notificacionActualizada);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long id) {
                if (id <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                notificacionService.deleteNotificacion(id);
                return ResponseEntity.noContent().build(); 
        }

        @GetMapping("/total")
        public ResponseEntity<Integer> totalNotificaciones() {
                int total = notificacionService.totalNotificacionesV2();
                
                if (total < 0) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
                }
                
                return ResponseEntity.ok(total);
        }

        
        
        @GetMapping("/destinatario/id/{idDestinatario}")
        public ResponseEntity<List<Notificacion>> buscarPorIdDestinatario(@PathVariable Long idDestinatario) {
                if (idDestinatario <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                List<Notificacion> notificaciones = notificacionService.obtenerPorIdDestinatario(idDestinatario);
                
                if (notificaciones.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                
                return ResponseEntity.ok(notificaciones);
        }

        
        @GetMapping("/destinatario/tipo/{tipoDestinatario}")
        public ResponseEntity<List<Notificacion>> buscarPorTipoDestinatario(@PathVariable String tipoDestinatario) {
                if (tipoDestinatario == null || tipoDestinatario.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                List<Notificacion> notificaciones = notificacionService.obtenerPorTipoDestinatario(tipoDestinatario);
                
                if (notificaciones.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                
                return ResponseEntity.ok(notificaciones);
        }

        
        @GetMapping("/estado/{estado}")
        public ResponseEntity<List<Notificacion>> buscarPorEstado(@PathVariable String estado) {
                if (estado == null || estado.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                List<Notificacion> notificaciones = notificacionService.obtenerPorEstado(estado);
                
                if (notificaciones.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notificaciones);
                }
                
                return ResponseEntity.ok(notificaciones);
        }

        
        @GetMapping("/tipo/{tipoNotificacion}")
        public ResponseEntity<List<Notificacion>> buscarPorTipoNotificacion(@PathVariable String tipoNotificacion) {
                if (tipoNotificacion == null || tipoNotificacion.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                List<Notificacion> notificaciones = notificacionService.obtenerPorTipoNotificacion(tipoNotificacion);
                
                if (notificaciones.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notificaciones);
                }
                
                return ResponseEntity.ok(notificaciones);
        }
}