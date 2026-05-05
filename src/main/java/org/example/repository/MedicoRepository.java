package org.example.repository;

import org.example.entity.Medico;
import org.example.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByUsuario(Usuario usuario);
}
