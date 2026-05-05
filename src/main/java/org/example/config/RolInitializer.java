package org.example.config;

import org.example.entity.Rol;
import org.example.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RolInitializer {

    @Bean
    CommandLineRunner crearRoles(RolRepository rolRepository) {
        return args -> {
            crearRolSiNoExiste(rolRepository, 1L, "ADMINISTRADOR");
            crearRolSiNoExiste(rolRepository, 2L, "MEDICO");
            crearRolSiNoExiste(rolRepository, 3L, "PACIENTE");
        };
    }

    private void crearRolSiNoExiste(RolRepository rolRepository, Long id, String nombre) {
        if (rolRepository.findByNombre(nombre).isPresent()) {
            return;
        }
        Rol rol = new Rol();
        rol.setId(id);
        rol.setNombre(nombre);
        rolRepository.save(rol);
    }
}
