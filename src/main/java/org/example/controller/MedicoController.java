package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.ConsultaResponse;
import org.example.service.ConsultaService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/medico/consultas")
@Tag(name = "Medico - Consultas", description = "Consultas asignadas al medico autenticado")
public class MedicoController {

    private final ConsultaService consultaService;

    public MedicoController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping
    @Operation(summary = "Mis consultas", description = "Lista solo las consultas asignadas al medico autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consultas del medico"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido")
    })
    public List<ConsultaResponse> listar(Authentication authentication) {
        return consultaService.listarPorMedico(authentication.getName());
    }
}
