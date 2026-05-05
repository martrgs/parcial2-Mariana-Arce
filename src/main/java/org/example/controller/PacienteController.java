package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.ConsultaResponse;
import org.example.dto.HorarioRequest;
import org.example.service.ConsultaService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/paciente/consultas")
@Tag(name = "Paciente - Consultas", description = "Consultas del paciente y actualizacion de horarios propios")
public class PacienteController {

    private final ConsultaService consultaService;

    public PacienteController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping
    @Operation(summary = "Mis consultas", description = "Lista solo las consultas asignadas al paciente autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consultas del paciente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido")
    })
    public List<ConsultaResponse> listar(Authentication authentication) {
        return consultaService.listarPorPaciente(authentication.getName());
    }

    @PutMapping("/{id}/horario")
    @Operation(summary = "Actualizar horario", description = "Permite al paciente modificar el horario solo de sus propias citas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Horario actualizado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido")
    })
    public ConsultaResponse actualizarHorario(@PathVariable Long id, @Valid @RequestBody HorarioRequest request, Authentication authentication) {
        return consultaService.actualizarHorarioPaciente(id, request, authentication.getName());
    }
}
