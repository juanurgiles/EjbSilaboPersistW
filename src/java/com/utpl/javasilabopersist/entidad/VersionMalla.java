/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.entidad;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author root
 */
@Entity
@Table(catalog = "digysoft_fartes1", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VersionMalla.findAll", query = "SELECT v FROM VersionMalla v"),
    @NamedQuery(name = "VersionMalla.findByIdVersionMalla", query = "SELECT v FROM VersionMalla v WHERE v.idVersionMalla = :idVersionMalla"),
    @NamedQuery(name = "VersionMalla.findByNombre", query = "SELECT v FROM VersionMalla v WHERE v.nombre = :nombre"),
    @NamedQuery(name = "VersionMalla.findByAnio", query = "SELECT v FROM VersionMalla v WHERE v.anio = :anio"),
    @NamedQuery(name = "VersionMalla.findByActivo", query = "SELECT v FROM VersionMalla v WHERE v.activo = :activo")})
public class VersionMalla implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idVersionMalla;
    @Column(length = 200)
    private String nombre;
    @Column(length = 45)
    private String anio;
    private Boolean activo;
    @OneToMany(mappedBy = "idVersionMalla")
    private Collection<Malla> mallaCollection;

    public VersionMalla() {
    }

    public VersionMalla(Integer idVersionMalla) {
        this.idVersionMalla = idVersionMalla;
    }

    public Integer getIdVersionMalla() {
        return idVersionMalla;
    }

    public void setIdVersionMalla(Integer idVersionMalla) {
        this.idVersionMalla = idVersionMalla;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @XmlTransient
    public Collection<Malla> getMallaCollection() {
        return mallaCollection;
    }

    public void setMallaCollection(Collection<Malla> mallaCollection) {
        this.mallaCollection = mallaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVersionMalla != null ? idVersionMalla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VersionMalla)) {
            return false;
        }
        VersionMalla other = (VersionMalla) object;
        if ((this.idVersionMalla == null && other.idVersionMalla != null) || (this.idVersionMalla != null && !this.idVersionMalla.equals(other.idVersionMalla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.utpl.javasilabopersist.entidad.VersionMalla[ idVersionMalla=" + idVersionMalla + " ]";
    }
    
}
