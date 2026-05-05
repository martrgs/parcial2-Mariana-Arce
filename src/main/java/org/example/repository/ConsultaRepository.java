package org.example.repository;

import org.example.entity.Consulta;
import org.example.entity.Medico;
import org.example.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByMedico(Medico medico);

    List<Consulta> findByPaciente(Paciente paciente);
}
