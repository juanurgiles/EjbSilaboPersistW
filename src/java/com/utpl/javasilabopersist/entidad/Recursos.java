/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.entidad;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */
@Entity
@Table(catalog = "digysoft_fartes1", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recursos.findAll", query = "SELECT r FROM Recursos r"),
    @NamedQuery(name = "Recursos.findByIdRecursos", query = "SELECT r FROM Recursos r WHERE r.idRecursos = :idRecursos"),
    @NamedQuery(name = "Recursos.findByNombreRecurso", query = "SELECT r FROM Recursos r WHERE r.nombreRecurso = :nombreRecurso")})
public class Recursos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idRecursos;
    @Column(name = "nombre_recurso", length = 200)
    private String nombreRecurso;

    public Recursos() {
    }

    public Recursos(Integer idRecursos) {
        this.idRecursos = idRecursos;
    }

    public Integer getIdRecursos() {
        return idRecursos;
    }

    public void setIdRecursos(Integer idRecursos) {
        this.idRecursos = idRecursos;
    }

    public String getNombreRecurso() {
        return nombreRecurso;
    }

    public void setNombreRecurso(String nombreRecurso) {
        this.nombreRecurso = nombreRecurso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRecursos != null ? idRecursos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recursos)) {
            return false;
        }
        Recursos other = (Recursos) object;
        if ((this.idRecursos == null && other.idRecursos != null) || (this.idRecursos != null && !this.idRecursos.equals(other.idRecursos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.utpl.javasilabopersist.entidad.Recursos[ idRecursos=" + idRecursos + " ]";
    }
    
}
