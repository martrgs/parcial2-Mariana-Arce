const base = location.port === "8081" ? "" : "http://127.0.0.1:8081";
let consultasAdmin = [];
let medicosAdmin = [];

function authHeader() {
    return localStorage.getItem("auth") || "";
}

function mostrarMensaje(texto, tipo = "danger") {
    const alerta = document.getElementById("mensaje");
    if (!alerta) {
        alert(texto);
        return;
    }
    alerta.className = `alert alert-${tipo}`;
    alerta.textContent = texto;
    alerta.classList.remove("d-none");
}

async function requestJson(url, options = {}) {
    let response;

    try {
        response = await fetch(base + url, {
            ...options,
            headers: {
                "Content-Type": "application/json",
                ...(options.headers || {})
            }
        });
    } catch (error) {
        throw new Error("No se pudo conectar con el backend en localhost:8081.");
    }

    const text = await response.text();
    let data = null;

    try {
        data = text ? JSON.parse(text) : null;
    } catch (error) {
        data = { message: "Respuesta no valida del servidor." };
    }

    if (!response.ok) {
        if (response.status === 403) {
            window.location.href = "sin-permiso.html";
        }
        throw new Error(data?.message || "No fue posible completar la operacion");
    }

    return data;
}

function redirigirPorRol(role) {
    const paginas = {
        ADMINISTRADOR: "administrador.html",
        MEDICO: "medico.html",
        PACIENTE: "paciente.html"
    };
    window.location.href = paginas[role] || "sin-permiso.html";
}

async function login() {
    const username = document.getElementById("user").value.trim();
    const password = document.getElementById("pass").value;

    try {
        const data = await requestJson("/api/auth/login", {
            method: "POST",
            body: JSON.stringify({ username, password })
        });
        localStorage.setItem("auth", "Basic " + btoa(username + ":" + password));
        localStorage.setItem("role", data.role);
        localStorage.setItem("username", data.username);
        redirigirPorRol(data.role);
    } catch (error) {
        mostrarMensaje(error.message);
    }
}

async function registrar() {
    const username = document.getElementById("user").value.trim();
    const nombreCompleto = document.getElementById("nombreCompleto").value.trim();
    const password = document.getElementById("pass").value;
    const role = document.getElementById("role").value;

    try {
        await requestJson("/api/auth/register", {
            method: "POST",
            body: JSON.stringify({ username, nombreCompleto, password, role })
        });
        mostrarMensaje("Usuario registrado correctamente.", "success");
        setTimeout(() => window.location.href = "login.html", 900);
    } catch (error) {
        mostrarMensaje(error.message);
    }
}

function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}

function validarSesion(rolEsperado) {
    if (!authHeader()) {
        window.location.href = "login.html";
        return false;
    }
    if (localStorage.getItem("role") !== rolEsperado) {
        window.location.href = "sin-permiso.html";
        return false;
    }
    const usuario = document.getElementById("usuarioActual");
    if (usuario) {
        usuario.textContent = localStorage.getItem("username") || "";
    }
    return true;
}

function formatoHorario(consulta) {
    return `${consulta.horaInicio} - ${consulta.horaFin}`;
}

async function cargarSelectsAdmin() {
    const medicos = await requestJson("/api/medicos", {
        headers: { Authorization: authHeader() }
    });
    const pacientes = await requestJson("/api/pacientes", {
        headers: { Authorization: authHeader() }
    });

    const medicoSelect = document.getElementById("medicoId");
    const pacienteSelect = document.getElementById("pacienteId");
    medicoSelect.innerHTML = '<option value="">Seleccione medico</option>';
    pacienteSelect.innerHTML = '<option value="">Seleccione paciente</option>';

    medicos.forEach(medico => {
        medicoSelect.innerHTML += `<option value="${medico.id}">${medico.nombre}</option>`;
    });

    pacientes.forEach(paciente => {
        pacienteSelect.innerHTML += `<option value="${paciente.id}" data-nombre="${paciente.nombre}">${paciente.nombre}</option>`;
    });
}

function copiarNombrePaciente() {
    const select = document.getElementById("pacienteId");
    const opcion = select.options[select.selectedIndex];
    document.getElementById("nombrePaciente").value = opcion?.dataset?.nombre || "";
}

async function cargarAdministrador() {
    if (!validarSesion("ADMINISTRADOR")) return;
    try {
        await cargarSelectsAdmin();
        await cargarMedicosAdmin();
        const data = await requestJson("/api/admin/consultas", {
            headers: { Authorization: authHeader() }
        });
        consultasAdmin = data;
        const tabla = document.getElementById("tabla");
        tabla.innerHTML = "";

        data.forEach(c => {
            tabla.innerHTML += `
            <tr>
                <td><strong>${c.nombrePaciente}</strong><span>${c.motivo}</span></td>
                <td>${c.numeroConsultorio}</td>
                <td>${formatoHorario(c)}</td>
                <td>${c.nombreMedico}</td>
                <td class="acciones">
                    <button class="btn btn-outline-success btn-sm" onclick="prepararEdicion(${c.id})">Editar</button>
                    <button class="btn btn-outline-danger btn-sm" onclick="eliminarConsulta(${c.id})">Eliminar</button>
                </td>
            </tr>`;
        });
    } catch (error) {
        mostrarMensaje(error.message);
    }
}

async function cargarMedicosAdmin() {
    const tablaMedicos = document.getElementById("tablaMedicos");
    if (!tablaMedicos) return;

    const medicos = await requestJson("/api/admin/medicos", {
        headers: { Authorization: authHeader() }
    });
    medicosAdmin = medicos;

    tablaMedicos.innerHTML = "";
    medicos.forEach(m => {
        tablaMedicos.innerHTML += `
        <tr>
            <td>${m.nombre}</td>
            <td class="acciones">
                <button class="btn btn-outline-success btn-sm" onclick="editarMedico(${m.id})">Editar</button>
                <button class="btn btn-outline-danger btn-sm" onclick="eliminarMedico(${m.id})">Eliminar</button>
            </td>
        </tr>`;
    });
}

async function guardarMedico(event) {
    event.preventDefault();
    const username = document.getElementById("medicoUser").value.trim();
    const nombre = document.getElementById("medicoNombre").value.trim();
    const password = document.getElementById("medicoPass").value;

    try {
        await requestJson("/api/admin/medicos", {
            method: "POST",
            headers: { Authorization: authHeader() },
            body: JSON.stringify({ username, nombre, password })
        });
        document.getElementById("formMedico").reset();
        await cargarAdministrador();
        mostrarMensaje("Medico creado.", "success");
    } catch (error) {
        mostrarMensaje(error.message);
    }
}

async function editarMedico(id) {
    const medico = medicosAdmin.find(m => m.id === id);
    if (!medico) return;

    const nombreActual = medico.nombre;
    const nombre = prompt("Nuevo nombre del medico:", nombreActual);
    if (!nombre) return;

    try {
        await requestJson(`/api/admin/medicos/${id}`, {
            method: "PUT",
            headers: { Authorization: authHeader() },
            body: JSON.stringify({ nombre })
        });
        await cargarAdministrador();
        mostrarMensaje("Medico actualizado.", "success");
    } catch (error) {
        mostrarMensaje(error.message);
    }
}

async function eliminarMedico(id) {
    if (!confirm("Deseas eliminar este medico?")) return;

    try {
        await requestJson(`/api/admin/medicos/${id}`, {
            method: "DELETE",
            headers: { Authorization: authHeader() }
        });
        await cargarAdministrador();
        mostrarMensaje("Medico eliminado.", "success");
    } catch (error) {
        mostrarMensaje(error.message);
    }
}

function leerFormularioConsulta() {
    return {
        nombrePaciente: document.getElementById("nombrePaciente").value.trim(),
        motivo: document.getElementById("motivo").value.trim(),
        numeroConsultorio: Number(document.getElementById("numeroConsultorio").value),
        horaInicio: document.getElementById("inicio").value,
        horaFin: document.getElementById("fin").value,
        medicoId: Number(document.getElementById("medicoId").value),
        pacienteId: Number(document.getElementById("pacienteId").value)
    };
}

function limpiarFormulario() {
    document.getElementById("consultaId").value = "";
    document.getElementById("formConsulta").reset();
    document.getElementById("guardarTexto").textContent = "Guardar";
    document.getElementById("cancelarEdicion").classList.add("d-none");
}

function prepararEdicion(id) {
    const consulta = consultasAdmin.find(c => c.id === id);
    if (!consulta) return;

    document.getElementById("consultaId").value = consulta.id;
    document.getElementById("nombrePaciente").value = consulta.nombrePaciente;
    document.getElementById("motivo").value = consulta.motivo;
    document.getElementById("numeroConsultorio").value = consulta.numeroConsultorio;
    document.getElementById("inicio").value = consulta.horaInicio;
    document.getElementById("fin").value = consulta.horaFin;
    document.getElementById("medicoId").value = consulta.medicoId;
    document.getElementById("pacienteId").value = consulta.pacienteId;
    document.getElementById("guardarTexto").textContent = "Actualizar";
    document.getElementById("cancelarEdicion").classList.remove("d-none");
}

async function guardarConsulta(event) {
    event.preventDefault();
    const id = document.getElementById("consultaId").value;
    const url = id ? `/api/admin/consultas/${id}` : "/api/admin/consultas";
    const method = id ? "PUT" : "POST";

    try {
        await requestJson(url, {
            method,
            headers: { Authorization: authHeader() },
            body: JSON.stringify(leerFormularioConsulta())
        });
        limpiarFormulario();
        await cargarAdministrador();
        mostrarMensaje(id ? "Consulta actualizada." : "Consulta creada.", "success");
    } catch (error) {
        mostrarMensaje(error.message);
    }
}

async function eliminarConsulta(id) {
    if (!confirm("Deseas eliminar esta consulta?")) return;
    try {
        await requestJson(`/api/admin/consultas/${id}`, {
            method: "DELETE",
            headers: { Authorization: authHeader() }
        });
        await cargarAdministrador();
        mostrarMensaje("Consulta eliminada.", "success");
    } catch (error) {
        mostrarMensaje(error.message);
    }
}

async function cargarMedico() {
    if (!validarSesion("MEDICO")) return;
    try {
        const data = await requestJson("/api/medico/consultas", {
            headers: { Authorization: authHeader() }
        });
        llenarTablaConsultaSimple(data, false);
    } catch (error) {
        mostrarMensaje(error.message);
    }
}

async function cargarPaciente() {
    if (!validarSesion("PACIENTE")) return;
    try {
        const data = await requestJson("/api/paciente/consultas", {
            headers: { Authorization: authHeader() }
        });
        llenarTablaConsultaSimple(data, true);
    } catch (error) {
        mostrarMensaje(error.message);
    }
}

function llenarTablaConsultaSimple(data, permiteEditar) {
    const tabla = document.getElementById("tabla");
    tabla.innerHTML = "";

    data.forEach(c => {
        tabla.innerHTML += `
        <tr>
            <td><strong>${c.nombrePaciente}</strong><span>${c.motivo}</span></td>
            <td>${c.numeroConsultorio}</td>
            <td>${formatoHorario(c)}</td>
            <td>${c.nombreMedico}</td>
            <td>${permiteEditar ? `<button class="btn btn-outline-success btn-sm" onclick='editarHorario(${c.id}, "${c.horaInicio}", "${c.horaFin}")'>Editar horario</button>` : "Solo lectura"}</td>
        </tr>`;
    });
}

async function editarHorario(id, horaInicioActual, horaFinActual) {
    const horaInicio = prompt("Nueva hora de inicio (HH:mm):", horaInicioActual);
    const horaFin = prompt("Nueva hora final (HH:mm):", horaFinActual);

    if (!horaInicio || !horaFin) return;

    try {
        await requestJson(`/api/paciente/consultas/${id}/horario`, {
            method: "PUT",
            headers: { Authorization: authHeader() },
            body: JSON.stringify({ horaInicio, horaFin })
        });
        await cargarPaciente();
        mostrarMensaje("Horario actualizado.", "success");
    } catch (error) {
        mostrarMensaje(error.message);
    }
}
