package org.example.service;

import org.example.dto.ConsultaRequest;
import org.example.dto.ConsultaResponse;
import org.example.dto.HorarioRequest;
import org.example.entity.Consulta;
import org.example.entity.Consultorio;
import org.example.entity.Medico;
import org.example.entity.Paciente;
import org.example.entity.Usuario;
import org.example.repository.ConsultaRepository;
import org.example.repository.ConsultorioRepository;
import org.example.repository.MedicoRepository;
import org.example.repository.PacienteRepository;
import org.example.repository.UsuarioRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final ConsultorioRepository consultorioRepository;
    private final UsuarioRepository usuarioRepository;

    public ConsultaService(ConsultaRepository consultaRepository, MedicoRepository medicoRepository,
                           PacienteRepository pacienteRepository, ConsultorioRepository consultorioRepository,
                           UsuarioRepository usuarioRepository) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.consultorioRepository = consultorioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<ConsultaResponse> listarTodas() {
        return consultaRepository.findAll().stream()
                .map(ConsultaResponse::fromEntity)
                .toList();
    }

    public List<ConsultaResponse> listarPorMedico(String username) {
        Medico medico = buscarMedicoPorUsuario(username);
        return consultaRepository.findByMedico(medico).stream()
                .map(ConsultaResponse::fromEntity)
                .toList();
    }

    public List<ConsultaResponse> listarPorPaciente(String username) {
        Paciente paciente = buscarPacientePorUsuario(username);
        return consultaRepository.findByPaciente(paciente).stream()
                .map(ConsultaResponse::fromEntity)
                .toList();
    }

    public ConsultaResponse crear(ConsultaRequest request) {
        Consulta consulta = new Consulta();
        llenarConsulta(consulta, request);
        return ConsultaResponse.fromEntity(consultaRepository.save(consulta));
    }

    public ConsultaResponse actualizar(Long id, ConsultaRequest request) {
        Consulta consulta = buscarConsulta(id);
        llenarConsulta(consulta, request);
        return ConsultaResponse.fromEntity(consultaRepository.save(consulta));
    }

    public void eliminar(Long id) {
        if (!consultaRepository.existsById(id)) {
            throw new IllegalArgumentException("Consulta no encontrada");
        }
        consultaRepository.deleteById(id);
    }

    public ConsultaResponse actualizarHorarioPaciente(Long id, HorarioRequest request, String username) {
        Consulta consulta = buscarConsulta(id);
        Paciente paciente = buscarPacientePorUsuario(username);

        if (!consulta.getPaciente().getId().equals(paciente.getId())) {
            throw new AccessDeniedException("Solo puedes modificar tus propias citas");
        }

        validarHorario(request.getHoraInicio(), request.getHoraFin());
        consulta.setHoraInicio(request.getHoraInicio());
        consulta.setHoraFin(request.getHoraFin());
        return ConsultaResponse.fromEntity(consultaRepository.save(consulta));
    }

    private void llenarConsulta(Consulta consulta, ConsultaRequest request) {
        validarHorario(request.getHoraInicio(), request.getHoraFin());

        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        if (!paciente.getNombre().equalsIgnoreCase(request.getNombrePaciente().trim())) {
            throw new IllegalArgumentException("El nombre del paciente no coincide con el paciente seleccionado");
        }

        Medico medico = medicoRepository.findById(request.getMedicoId())
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));

        Consultorio consultorio = consultorioRepository.findByNumero(request.getNumeroConsultorio())
                .orElseGet(() -> {
                    Consultorio nuevo = new Consultorio();
                    nuevo.setNumero(request.getNumeroConsultorio());
                    return consultorioRepository.save(nuevo);
                });

        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setConsultorio(consultorio);
        consulta.setMotivo(request.getMotivo().trim());
        consulta.setHoraInicio(request.getHoraInicio());
        consulta.setHoraFin(request.getHoraFin());
    }

    private void validarHorario(LocalTime inicio, LocalTime fin) {
        if (!inicio.isBefore(fin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora final");
        }
    }

    private Consulta buscarConsulta(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta no encontrada"));
    }

    private Medico buscarMedicoPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return medicoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));
    }

    private Paciente buscarPacientePorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return pacienteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
    }
}
