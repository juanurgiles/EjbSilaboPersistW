/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.entidad;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */
@Entity
@Table(catalog = "digysoft_fartes1", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Materiat.findAll", query = "SELECT m FROM Materiat m"),
    @NamedQuery(name = "Materiat.findById", query = "SELECT m FROM Materiat m WHERE m.id = :id"),
    @NamedQuery(name = "Materiat.findByNombre", query = "SELECT m FROM Materiat m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "Materiat.findByArchivo", query = "SELECT m FROM Materiat m WHERE m.archivo = :archivo"),
    @NamedQuery(name = "Materiat.findByFecha", query = "SELECT m FROM Materiat m WHERE m.fecha = :fecha")})
public class Materiat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(length = 100)
    private String nombre;
    @Column(length = 100)
    private String archivo;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "profesort_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Profesort profesortId;

    public Materiat() {
    }

    public Materiat(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Profesort getProfesortId() {
        return profesortId;
    }

    public void setProfesortId(Profesort profesortId) {
        this.profesortId = profesortId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Materiat)) {
            return false;
        }
        Materiat other = (Materiat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.utpl.javasilabopersist.entidad.Materiat[ id=" + id + " ]";
    }
    
}
