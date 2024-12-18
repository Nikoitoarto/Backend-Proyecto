package com.ProyectoFormulario.ProyectoFormulario.Dto;

import java.util.List;

public class LoginResponseDto {

    private String token;
    private String usuarioNombre;
    private String contrasena;
    private Long personaId;
    private Long rolId;
    private List<Long> formularioId;
    private Long usuarioId;

    // Constructor


    public LoginResponseDto(String token, String usuarioNombre, Long personaId, Long rolId, String contrasena, List<Long> formularioId, Long usuarioId) {
        this.token = token;
        this.usuarioNombre = usuarioNombre;
        this.personaId = personaId;
        this.rolId = rolId;
        this.contrasena = contrasena;
        this.formularioId = formularioId;
        this.usuarioId = usuarioId;
    }


    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<Long> getFormularioId() {
        return formularioId;
    }

    public void setFormularioId(List<Long> formularioId) {
        this.formularioId = formularioId;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    // Getters y Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public Long getPersonaId() { return personaId; }
    public void setPersonaId(Long personaId) { this.personaId = personaId; }

    public Long getRolId() { return rolId; }
    public void setRolId(Long rolId) { this.rolId = rolId; }
}
