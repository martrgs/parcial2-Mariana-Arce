# Consultas Médicas

Base de datos en PostgreSQL:

```sql
CREATE DATABASE consultasMedicas;

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(40) NOT NULL UNIQUE,
    nombre_completo VARCHAR(40) NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol_id BIGINT NOT NULL,
    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (rol_id)
        REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS medicos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(40) NOT NULL,
    usuario_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_medico_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS pacientes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(40) NOT NULL,
    usuario_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_paciente_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS consultorios (
    id BIGSERIAL PRIMARY KEY,
    numero INTEGER NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS consultas (
    id BIGSERIAL PRIMARY KEY,
    motivo VARCHAR(100) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    paciente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,
    consultorio_id BIGINT NOT NULL,
    CONSTRAINT fk_consulta_paciente
        FOREIGN KEY (paciente_id)
        REFERENCES pacientes(id),
    CONSTRAINT fk_consulta_medico
        FOREIGN KEY (medico_id)
        REFERENCES medicos(id),
    CONSTRAINT fk_consulta_consultorio
        FOREIGN KEY (consultorio_id)
        REFERENCES consultorios(id)
);

INSERT INTO roles (id, nombre) VALUES
    (1, 'ADMINISTRADOR'),
    (2, 'MEDICO'),
    (3, 'PACIENTE')
ON CONFLICT (id) DO NOTHING;


```

Tablas principales:

- `roles`: ADMINISTRADOR, MEDICO, PACIENTE.
- `usuarios`: credenciales y rol.
- `medicos`: datos del medico, relacionado con usuario.
- `pacientes`: datos del paciente, relacionado con usuario.
- `consultorios`: numeros de consultorio.
- `consultas`: cita medica con paciente, medico y consultorio por llaves foraneas.

Los usuarios se crean desde el formulario de registro.

Se insertaron los roles automáticamente ya que son datos base del sistema.
