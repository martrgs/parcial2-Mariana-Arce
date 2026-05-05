package org.example.repository;

import org.example.entity.Paciente;
import org.example.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByUsuario(Usuario usuario);
}
