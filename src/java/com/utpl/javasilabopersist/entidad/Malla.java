/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.entidad;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @NamedQuery(name = "Malla.findAll", query = "SELECT m FROM Malla m"),
    @NamedQuery(name = "Malla.findByIdMalla", query = "SELECT m FROM Malla m WHERE m.idMalla = :idMalla"),
    @NamedQuery(name = "Malla.findByNombreMalla", query = "SELECT m FROM Malla m WHERE m.nombreMalla = :nombreMalla"),
    @NamedQuery(name = "Malla.findByAnio", query = "SELECT m FROM Malla m WHERE m.anio = :anio"),
    @NamedQuery(name = "Malla.findByAnios", query = "SELECT m FROM Malla m WHERE m.anios = :anios"),
    @NamedQuery(name = "Malla.findByPeriodos", query = "SELECT m FROM Malla m WHERE m.periodos = :periodos")})
public class Malla implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idMalla;
    @Column(name = "nombre_Malla", length = 100)
    private String nombreMalla;
    private Integer anio;
    private Integer anios;
    private Integer periodos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mallaId")
    private Collection<MateriaMalla> materiaMallaCollection;
    @JoinColumn(name = "idVersionMalla", referencedColumnName = "idVersionMalla")
    @ManyToOne
    private VersionMalla idVersionMalla;
    @JoinColumn(name = "idCarrera", referencedColumnName = "idCarrera")
    @ManyToOne
    private Carrera idCarrera;

    public Malla() {
    }

    public Malla(Integer idMalla) {
        this.idMalla = idMalla;
    }

    public Integer getIdMalla() {
        return idMalla;
    }

    public void setIdMalla(Integer idMalla) {
        this.idMalla = idMalla;
    }

    public String getNombreMalla() {
        return nombreMalla;
    }

    public void setNombreMalla(String nombreMalla) {
        this.nombreMalla = nombreMalla;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getAnios() {
        return anios;
    }

    public void setAnios(Integer anios) {
        this.anios = anios;
    }

    public Integer getPeriodos() {
        return periodos;
    }

    public void setPeriodos(Integer periodos) {
        this.periodos = periodos;
    }

    @XmlTransient
    public Collection<MateriaMalla> getMateriaMallaCollection() {
        return materiaMallaCollection;
    }

    public void setMateriaMallaCollection(Collection<MateriaMalla> materiaMallaCollection) {
        this.materiaMallaCollection = materiaMallaCollection;
    }

    public VersionMalla getIdVersionMalla() {
        return idVersionMalla;
    }

    public void setIdVersionMalla(VersionMalla idVersionMalla) {
        this.idVersionMalla = idVersionMalla;
    }

    public Carrera getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Carrera idCarrera) {
        this.idCarrera = idCarrera;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMalla != null ? idMalla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Malla)) {
            return false;
        }
        Malla other = (Malla) object;
        if ((this.idMalla == null && other.idMalla != null) || (this.idMalla != null && !this.idMalla.equals(other.idMalla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.utpl.javasilabopersist.entidad.Malla[ idMalla=" + idMalla + " ]";
    }
    
}
