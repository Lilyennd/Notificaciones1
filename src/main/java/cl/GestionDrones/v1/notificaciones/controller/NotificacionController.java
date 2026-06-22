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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Notificaciones", description = "Operaciones relacionadas con las notificaciones")
@RestController
@RequestMapping("/api/v1/notificaciones")
public class NotificacionController {

        private final NotificacionService notificacionService;

        public NotificacionController(NotificacionService notificacionService) {
                this.notificacionService = notificacionService;
        }

        @Operation(summary = "Obtener todas las notificaciones", description = "Retorna una lista completa de todas las notificaciones registradas en el sistema")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de notificaciones obtenida con éxito", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Notificacion.class))),
            @ApiResponse(responseCode = "204", description = "No hay contenido disponible (Lista vacía)", content = @Content)
        })
        @GetMapping
        public ResponseEntity<List<Notificacion>> listarNotificaciones() {
                List<Notificacion> notificaciones = notificacionService.getNotificaciones();
                
                if (notificaciones.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                
                return ResponseEntity.ok(notificaciones);
        }

        @Operation(summary = "Agregar una nueva notificación", description = "Registra una notificación validando que el mensaje y destinatario no estén vacíos")
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Estructura JSON de la nueva notificación a registrar",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreateNotificacionRequest.class),
                examples = @ExampleObject(
                    name = "Ejemplo de Nueva Notificación",
                    value = "{\n  \"tipoDestinatario\": \"PILOTO\",\n  \"idDestinatario\": 45,\n  \"tipoNotificacion\": \"ALERTA_CLIMA\",\n  \"mensaje\": \"Condiciones climáticas adversas en la zona de vuelo programada; vientos sobre 30 nudos.\",\n  \"estado\": \"PENDIENTE\"\n}"
                )
            )
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notificación registrada de manera exitosa", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Notificacion.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o formato incorrecto", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        })
        @PostMapping
        public ResponseEntity<Notificacion> agregarNotificacion(
            @Valid @RequestBody CreateNotificacionRequest request
        ) {
                
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

        @Operation(summary = "Obtener una notificación por código", description = "Busca y retorna los detalles de una notificación específica utilizando su ID")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación encontrada exitosamente", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Notificacion.class))),
            @ApiResponse(responseCode = "400", description = "ID de notificación inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada", content = @Content)
        })
        @GetMapping("/{id}")
        public ResponseEntity<Notificacion> buscarNotificacion(
            @Parameter(description = "ID de la notificación a buscar", required = true, example = "1")
            @PathVariable Long id
        ) {
                if (id <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                Notificacion notificacion = notificacionService.getNotificacionId(id);
                
                if (notificacion == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                
                return ResponseEntity.ok(notificacion);
        }

        @Operation(summary = "Actualizar una notificación", description = "Modifica el estado y detalles de una notificación ya existente")
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Estructura JSON con los nuevos campos de la notificación",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UpdateNotificacionRequest.class),
                examples = @ExampleObject(
                    name = "Ejemplo de Actualización de Notificación",
                    value = "{\n  \"tipoDestinatario\": \"PILOTO\",\n  \"idDestinatario\": 45,\n  \"tipoNotificacion\": \"ALERTA_CLIMA\",\n  \"mensaje\": \"Condiciones climáticas adversas en la zona de vuelo programada; vientos sobre 30 nudos.\",\n  \"estado\": \"ENVIADO\"\n}"
                )
            )
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación actualizada de manera exitosa", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Notificacion.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido o estado vacío en la petición", content = @Content),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada", content = @Content)
        })
        @PutMapping("/{id}")
        public ResponseEntity<Notificacion> actualizarNotificacion(
            @Parameter(description = "ID de la notificación a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateNotificacionRequest request
        ) {
                
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

        @Operation(summary = "Eliminar una notificación", description = "Remueve de forma permanente una notificación del sistema mediante su ID")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notificación estimada de forma exitosa"),
            @ApiResponse(responseCode = "400", description = "ID proporcionado es inválido", content = @Content)
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminarNotificacion(
            @Parameter(description = "ID de la notificación a eliminar", required = true, example = "1")
            @PathVariable Long id
        ) {
                if (id <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                notificacionService.deleteNotificacion(id);
                return ResponseEntity.noContent().build(); 
        }

        @Operation(summary = "Obtener total de notificaciones", description = "Retorna la cantidad total de registros de notificaciones")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo obtenido con éxito", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
        })
        @GetMapping("/total")
        public ResponseEntity<Integer> totalNotificaciones() {
                int total = notificacionService.totalNotificacionesV2();
                
                if (total < 0) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
                }
                
                return ResponseEntity.ok(total);
        }

        @Operation(summary = "Buscar por ID de destinatario", description = "Recupera una lista de notificaciones enviadas a un ID de destinatario específico")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de notificaciones encontrada", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Notificacion.class))),
            @ApiResponse(responseCode = "204", description = "No existen notificaciones registradas para ese destinatario", content = @Content),
            @ApiResponse(responseCode = "400", description = "ID del destinatario inválido", content = @Content)
        })
        @GetMapping("/destinatario/id/{idDestinatario}")
        public ResponseEntity<List<Notificacion>> buscarPorIdDestinatario(
            @Parameter(description = "ID único del destinatario", required = true, example = "45")
            @PathVariable Long idDestinatario
        ) {
                if (idDestinatario <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                List<Notificacion> notificaciones = notificacionService.obtenerPorIdDestinatario(idDestinatario);
                
                if (notificaciones.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                
                return ResponseEntity.ok(notificaciones);
        }

        @Operation(summary = "Buscar por tipo de destinatario", description = "Filtra la lista de notificaciones de acuerdo con la categoría del destinatario (ej. PILOTO, EMPRESA)")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificaciones recuperadas con éxito", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Notificacion.class))),
            @ApiResponse(responseCode = "204", description = "No hay datos para esa categoría de destinatario", content = @Content),
            @ApiResponse(responseCode = "400", description = "Tipo de destinatario vacío o nulo", content = @Content)
        })
        @GetMapping("/destinatario/tipo/{tipoDestinatario}")
        public ResponseEntity<List<Notificacion>> buscarPorTipoDestinatario(
            @Parameter(description = "Categoría o tipo de destinatario", required = true, example = "PILOTO")
            @PathVariable String tipoDestinatario
        ) {
                if (tipoDestinatario == null || tipoDestinatario.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                List<Notificacion> notificaciones = notificacionService.obtenerPorTipoDestinatario(tipoDestinatario);
                
                if (notificaciones.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                
                return ResponseEntity.ok(notificaciones);
        }

        @Operation(summary = "Buscar por estado de notificación", description = "Filtra las notificaciones de acuerdo a su estado de envío (ej. ENVIADO, PENDIENTE)")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificaciones filtradas con éxito", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Notificacion.class))),
            @ApiResponse(responseCode = "400", description = "Estado provisto vacío o nulo", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontraron notificaciones en ese estado", content = @Content)
        })
        @GetMapping("/estado/{estado}")
        public ResponseEntity<List<Notificacion>> buscarPorEstado(
            @Parameter(description = "Estado actual de la notificación", required = true, example = "ENVIADO")
            @PathVariable String estado
        ) {
                if (estado == null || estado.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                List<Notificacion> notificaciones = notificacionService.obtenerPorEstado(estado);
                
                if (notificaciones.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notificaciones);
                }
                
                return ResponseEntity.ok(notificaciones);
        }

        @Operation(summary = "Buscar por tipo de notificación", description = "Filtra los registros según el tipo o canal de alerta")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificaciones encontradas", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Notificacion.class))),
            @ApiResponse(responseCode = "400", description = "Tipo de notificación vacío o nulo", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontraron registros de ese tipo", content = @Content)
        })
        @GetMapping("/tipo/{tipoNotificacion}")
        public ResponseEntity<List<Notificacion>> buscarPorTipoNotificacion(
            @Parameter(description = "Tipo de alerta o notificación", required = true, example = "ALERTA_CLIMA")
            @PathVariable String tipoNotificacion
        ) {
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