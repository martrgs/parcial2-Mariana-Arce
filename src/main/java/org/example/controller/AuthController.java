package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.LoginRequest;
import org.example.dto.MedicoResponse;
import org.example.dto.PacienteResponse;
import org.example.dto.RegisterRequest;
import org.example.dto.UsuarioResponse;
import org.example.repository.UsuarioRepository;
import org.example.service.UsuarioService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Autenticacion y usuarios", description = "Inicio de sesion, registro y consulta de medicos y pacientes")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/auth/login")
    @Operation(summary = "Iniciar sesion", description = "Valida credenciales y devuelve el rol real guardado en la base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inicio de sesion exitoso"),
            @ApiResponse(responseCode = "401", description = "Usuario o contrasena incorrectos"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido")
    })
    public Map<String, Object> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var usuario = usuarioRepository.findByUsername(request.getUsername()).orElseThrow();
        return Map.of(
                "username", usuario.getUsername(),
                "role", usuario.getRol().getNombre()
        );
    }

    @PostMapping("/auth/register")
    @Operation(summary = "Registrar usuario", description = "Registra usuarios para pruebas de la aplicacion, cifrando la contrasena con BCrypt.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario registrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido")
    })
    public UsuarioResponse registrar(@Valid @RequestBody RegisterRequest request) {
        return usuarioService.registrar(request);
    }

    @GetMapping("/medicos")
    @Operation(summary = "Listar medicos", description = "Devuelve los medicos registrados para cargar el desplegable.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de medicos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido")
    })
    public List<MedicoResponse> listarMedicos() {
        return usuarioService.listarMedicos();
    }

    @GetMapping("/pacientes")
    @Operation(summary = "Listar pacientes", description = "Devuelve los pacientes registrados para asignar consultas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pacientes"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso prohibido")
    })
    public List<PacienteResponse> listarPacientes() {
        return usuarioService.listarPacientes();
    }
}
