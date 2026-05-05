package org.example.service;

import org.example.dto.MedicoResponse;
import org.example.dto.MedicoRequest;
import org.example.dto.MedicoUpdateRequest;
import org.example.dto.PacienteResponse;
import org.example.dto.RegisterRequest;
import org.example.dto.UsuarioResponse;
import org.example.entity.Medico;
import org.example.entity.Paciente;
import org.example.entity.Rol;
import org.example.entity.Usuario;
import org.example.repository.MedicoRepository;
import org.example.repository.PacienteRepository;
import org.example.repository.RolRepository;
import org.example.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
                          MedicoRepository medicoRepository, PacienteRepository pacienteRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse registrar(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        Rol rol = rolRepository.findByNombre(request.getRole())
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado en la base de datos"));

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername().trim());
        usuario.setNombreCompleto(request.getNombreCompleto().trim());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rol);
        usuario = usuarioRepository.save(usuario);

        if ("MEDICO".equals(rol.getNombre())) {
            Medico medico = new Medico();
            medico.setNombre(usuario.getNombreCompleto());
            medico.setUsuario(usuario);
            medicoRepository.save(medico);
        }

        if ("PACIENTE".equals(rol.getNombre())) {
            Paciente paciente = new Paciente();
            paciente.setNombre(usuario.getNombreCompleto());
            paciente.setUsuario(usuario);
            pacienteRepository.save(paciente);
        }

        return UsuarioResponse.fromEntity(usuario);
    }

    public List<MedicoResponse> listarMedicos() {
        return medicoRepository.findAll().stream()
                .map(MedicoResponse::fromEntity)
                .toList();
    }

    public MedicoResponse crearMedico(MedicoRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        Rol rol = rolRepository.findByNombre("MEDICO")
                .orElseThrow(() -> new IllegalArgumentException("Rol MEDICO no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername().trim());
        usuario.setNombreCompleto(request.getNombre().trim());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rol);
        usuario = usuarioRepository.save(usuario);

        Medico medico = new Medico();
        medico.setNombre(request.getNombre().trim());
        medico.setUsuario(usuario);
        return MedicoResponse.fromEntity(medicoRepository.save(medico));
    }

    public MedicoResponse actualizarMedico(Long id, MedicoUpdateRequest request) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));

        medico.setNombre(request.getNombre().trim());
        medico.getUsuario().setNombreCompleto(request.getNombre().trim());
        usuarioRepository.save(medico.getUsuario());
        return MedicoResponse.fromEntity(medicoRepository.save(medico));
    }

    @Transactional
    public void eliminarMedico(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));
        Usuario usuario = medico.getUsuario();
        medicoRepository.delete(medico);
        usuarioRepository.delete(usuario);
    }

    public List<PacienteResponse> listarPacientes() {
        return pacienteRepository.findAll().stream()
                .map(PacienteResponse::fromEntity)
                .toList();
    }
}
