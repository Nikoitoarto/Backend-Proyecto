package com.ProyectoFormulario.ProyectoFormulario.Entity;


import jakarta.persistence.*;

@Entity
@Table(name = "vicerrectoria")
public class Vicerrectoria extends AbaseEntity {


    @Column(name = "area", length = 50, nullable = false)
    private String area;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;

    @ManyToOne
    @JoinColumn(name = "revision_id", nullable = false)
    private RevisionFormulario revision;

    public RevisionFormulario getRevision() {
        return revision;
    }

    public void setRevision(RevisionFormulario revision) {
        this.revision = revision;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
