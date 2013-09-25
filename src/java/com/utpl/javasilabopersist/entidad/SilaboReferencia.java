/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.entidad;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */
@Entity
@Table(name = "SILABO_REFERENCIA", catalog = "digysoft_fartes1", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SilaboReferencia.findAll", query = "SELECT s FROM SilaboReferencia s"),
    @NamedQuery(name = "SilaboReferencia.findByIdSilaboReferencia", query = "SELECT s FROM SilaboReferencia s WHERE s.idSilaboReferencia = :idSilaboReferencia"),
    @NamedQuery(name = "SilaboReferencia.findByTipoJer", query = "SELECT s FROM SilaboReferencia s WHERE s.tipoJer = :tipoJer")})
public class SilaboReferencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(nullable = false, length = 45)
    private String idSilaboReferencia;
    @Basic(optional = false)
    @Column(nullable = false)
    private short tipoJer;
    @JoinColumn(name = "REFERENCIA_ID", referencedColumnName = "idReferencia", nullable = false)
    @ManyToOne(optional = false)
    private Referencia referenciaId;
    @JoinColumn(name = "SILABO_ID", referencedColumnName = "idSilabo", nullable = false)
    @ManyToOne(optional = false)
    private Silabo silaboId;

    public SilaboReferencia() {
    }

    public SilaboReferencia(String idSilaboReferencia) {
        this.idSilaboReferencia = idSilaboReferencia;
    }

    public SilaboReferencia(String idSilaboReferencia, short tipoJer) {
        this.idSilaboReferencia = idSilaboReferencia;
        this.tipoJer = tipoJer;
    }

    public String getIdSilaboReferencia() {
        return idSilaboReferencia;
    }

    public void setIdSilaboReferencia(String idSilaboReferencia) {
        this.idSilaboReferencia = idSilaboReferencia;
    }

    public short getTipoJer() {
        return tipoJer;
    }

    public void setTipoJer(short tipoJer) {
        this.tipoJer = tipoJer;
    }

    public Referencia getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Referencia referenciaId) {
        this.referenciaId = referenciaId;
    }

    public Silabo getSilaboId() {
        return silaboId;
    }

    public void setSilaboId(Silabo silaboId) {
        this.silaboId = silaboId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSilaboReferencia != null ? idSilaboReferencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SilaboReferencia)) {
            return false;
        }
        SilaboReferencia other = (SilaboReferencia) object;
        if ((this.idSilaboReferencia == null && other.idSilaboReferencia != null) || (this.idSilaboReferencia != null && !this.idSilaboReferencia.equals(other.idSilaboReferencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.utpl.javasilabopersist.entidad.SilaboReferencia[ idSilaboReferencia=" + idSilaboReferencia + " ]";
    }
    
}
