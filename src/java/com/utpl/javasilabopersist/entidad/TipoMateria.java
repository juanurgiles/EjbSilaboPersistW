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
import javax.persistence.Lob;
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
    @NamedQuery(name = "TipoMateria.findAll", query = "SELECT t FROM TipoMateria t"),
    @NamedQuery(name = "TipoMateria.findByIdTipoMateria", query = "SELECT t FROM TipoMateria t WHERE t.idTipoMateria = :idTipoMateria"),
    @NamedQuery(name = "TipoMateria.findByNombreTIpomateria", query = "SELECT t FROM TipoMateria t WHERE t.nombreTIpomateria = :nombreTIpomateria")})
public class TipoMateria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idTipoMateria;
    @Column(name = "nombre_TIpomateria", length = 100)
    private String nombreTIpomateria;
    @Lob
    @Column(name = "descripcion_Tipomateria", length = 65535)
    private String descripcionTipomateria;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoMateria")
    private Collection<Materia> materiaCollection;

    public TipoMateria() {
    }

    public TipoMateria(Integer idTipoMateria) {
        this.idTipoMateria = idTipoMateria;
    }

    public Integer getIdTipoMateria() {
        return idTipoMateria;
    }

    public void setIdTipoMateria(Integer idTipoMateria) {
        this.idTipoMateria = idTipoMateria;
    }

    public String getNombreTIpomateria() {
        return nombreTIpomateria;
    }

    public void setNombreTIpomateria(String nombreTIpomateria) {
        this.nombreTIpomateria = nombreTIpomateria;
    }

    public String getDescripcionTipomateria() {
        return descripcionTipomateria;
    }

    public void setDescripcionTipomateria(String descripcionTipomateria) {
        this.descripcionTipomateria = descripcionTipomateria;
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
        hash += (idTipoMateria != null ? idTipoMateria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoMateria)) {
            return false;
        }
        TipoMateria other = (TipoMateria) object;
        if ((this.idTipoMateria == null && other.idTipoMateria != null) || (this.idTipoMateria != null && !this.idTipoMateria.equals(other.idTipoMateria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.utpl.javasilabopersist.entidad.TipoMateria[ idTipoMateria=" + idTipoMateria + " ]";
    }
    
}
