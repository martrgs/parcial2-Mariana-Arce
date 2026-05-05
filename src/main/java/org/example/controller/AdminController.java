package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.ConsultaRequest;
import org.example.dto.ConsultaResponse;
import org.example.dto.MedicoRequest;
import org.example.dto.MedicoResponse;
import org.example.dto.MedicoUpdateRequest;
import org.example.service.ConsultaService;
import org.example.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administrador - Consultas", description = "Gestion completa de consultas medicas")
public class AdminController {

    private final ConsultaService consultaService;
    private final UsuarioService usuarioService;

    public AdminController(ConsultaService consultaService, UsuarioService usuarioService) {
        this.consultaService = consultaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/consultas")
    @Operation(summary = "Listar consultas", description = "Muestra todas las consultas registradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consultas encontradas"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido")
    })
    public List<ConsultaResponse> listar() {
        return consultaService.listarTodas();
    }

    @PostMapping("/consultas")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear consulta", description = "Registra una consulta con paciente, motivo, consultorio, horario y medico.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consulta creada"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido")
    })
    public ConsultaResponse crear(@Valid @RequestBody ConsultaRequest request) {
        return consultaService.crear(request);
    }

    @PutMapping("/consultas/{id}")
    @Operation(summary = "Editar consulta", description = "Actualiza los datos principales de una consulta.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta actualizada"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido")
    })
    public ConsultaResponse editar(@PathVariable Long id, @Valid @RequestBody ConsultaRequest request) {
        return consultaService.actualizar(id, request);
    }

    @DeleteMapping("/consultas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar consulta", description = "Elimina una consulta por id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Consulta eliminada"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido")
    })
    public void eliminar(@PathVariable Long id) {
        consultaService.eliminar(id);
    }

    @GetMapping("/medicos")
    @Operation(summary = "Listar medicos", description = "Muestra todos los medicos registrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medicos encontrados"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido")
    })
    public List<MedicoResponse> listarMedicos() {
        return usuarioService.listarMedicos();
    }

    @PostMapping("/medicos")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear medico", description = "Crea un usuario medico y su registro en la tabla medicos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Medico creado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido")
    })
    public MedicoResponse crearMedico(@Valid @RequestBody MedicoRequest request) {
        return usuarioService.crearMedico(request);
    }

    @PutMapping("/medicos/{id}")
    @Operation(summary = "Editar medico", description = "Actualiza el nombre del medico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medico actualizado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido")
    })
    public MedicoResponse editarMedico(@PathVariable Long id, @Valid @RequestBody MedicoUpdateRequest request) {
        return usuarioService.actualizarMedico(id, request);
    }

    @DeleteMapping("/medicos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar medico", description = "Elimina un medico si no tiene consultas relacionadas.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Medico eliminado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido")
    })
    public void eliminarMedico(@PathVariable Long id) {
        usuarioService.eliminarMedico(id);
    }
}
