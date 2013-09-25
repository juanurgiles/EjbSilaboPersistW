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
    @NamedQuery(name = "EjeFormacion.findAll", query = "SELECT e FROM EjeFormacion e"),
    @NamedQuery(name = "EjeFormacion.findByIdEjeFormacion", query = "SELECT e FROM EjeFormacion e WHERE e.idEjeFormacion = :idEjeFormacion"),
    @NamedQuery(name = "EjeFormacion.findByNombreEjeFormacion", query = "SELECT e FROM EjeFormacion e WHERE e.nombreEjeFormacion = :nombreEjeFormacion"),
    @NamedQuery(name = "EjeFormacion.findByDescripcionEjeFormacion", query = "SELECT e FROM EjeFormacion e WHERE e.descripcionEjeFormacion = :descripcionEjeFormacion")})
public class EjeFormacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idEjeFormacion;
    @Column(name = "nombre_Eje_Formacion", length = 45)
    private String nombreEjeFormacion;
    @Column(name = "descripcion_Eje_Formacion", length = 45)
    private String descripcionEjeFormacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEjeFormacion")
    private Collection<Materia> materiaCollection;

    public EjeFormacion() {
    }

    public EjeFormacion(Integer idEjeFormacion) {
        this.idEjeFormacion = idEjeFormacion;
    }

    public Integer getIdEjeFormacion() {
        return idEjeFormacion;
    }

    public void setIdEjeFormacion(Integer idEjeFormacion) {
        this.idEjeFormacion = idEjeFormacion;
    }

    public String getNombreEjeFormacion() {
        return nombreEjeFormacion;
    }

    public void setNombreEjeFormacion(String nombreEjeFormacion) {
        this.nombreEjeFormacion = nombreEjeFormacion;
    }

    public String getDescripcionEjeFormacion() {
        return descripcionEjeFormacion;
    }

    public void setDescripcionEjeFormacion(String descripcionEjeFormacion) {
        this.descripcionEjeFormacion = descripcionEjeFormacion;
    }

    @XmlTransient
    public Collection<Materia> getMateriaCollection() {
        return materiaCollection;
    }

    public void setMateriaCollection(Collection<Materia> materiaCollection) {
        this.materiaCollection = materiaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEjeFormacion != null ? idEjeFormacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EjeFormacion)) {
            return false;
        }
        EjeFormacion other = (EjeFormacion) object;
        if ((this.idEjeFormacion == null && other.idEjeFormacion != null) || (this.idEjeFormacion != null && !this.idEjeFormacion.equals(other.idEjeFormacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.utpl.javasilabopersist.entidad.EjeFormacion[ idEjeFormacion=" + idEjeFormacion + " ]";
    }
    
}
