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
    @NamedQuery(name = "TipoReferencia.findAll", query = "SELECT t FROM TipoReferencia t"),
    @NamedQuery(name = "TipoReferencia.findByIdTipoReferencia", query = "SELECT t FROM TipoReferencia t WHERE t.idTipoReferencia = :idTipoReferencia"),
    @NamedQuery(name = "TipoReferencia.findByNombreTipoReferencia", query = "SELECT t FROM TipoReferencia t WHERE t.nombreTipoReferencia = :nombreTipoReferencia")})
public class TipoReferencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idTipoReferencia;
    @Column(name = "nombre_tipo_referencia", length = 100)
    private String nombreTipoReferencia;
    @Lob
    @Column(name = "descripcion_referencia", length = 65535)
    private String descripcionReferencia;
    @OneToMany(mappedBy = "idTipoReferencia")
    private Collection<Referencia> referenciaCollection;

    public TipoReferencia() {
    }

    public TipoReferencia(Integer idTipoReferencia) {
        this.idTipoReferencia = idTipoReferencia;
    }

    public Integer getIdTipoReferencia() {
        return idTipoReferencia;
    }

    public void setIdTipoReferencia(Integer idTipoReferencia) {
        this.idTipoReferencia = idTipoReferencia;
    }

    public String getNombreTipoReferencia() {
        return nombreTipoReferencia;
    }

    public void setNombreTipoReferencia(String nombreTipoReferencia) {
        this.nombreTipoReferencia = nombreTipoReferencia;
    }

    public String getDescripcionReferencia() {
        return descripcionReferencia;
    }

    public void setDescripcionReferencia(String descripcionReferencia) {
        this.descripcionReferencia = descripcionReferencia;
    }

    @XmlTransient
    public Collection<Referencia> getReferenciaCollection() {
        return referenciaCollection;
    }

    public void setReferenciaCollection(Collection<Referencia> referenciaCollection) {
        this.referenciaCollection = referenciaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoReferencia != null ? idTipoReferencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoReferencia)) {
            return false;
        }
        TipoReferencia other = (TipoReferencia) object;
        if ((this.idTipoReferencia == null && other.idTipoReferencia != null) || (this.idTipoReferencia != null && !this.idTipoReferencia.equals(other.idTipoReferencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + nombreTipoReferencia + " ]";
    }
    
}
