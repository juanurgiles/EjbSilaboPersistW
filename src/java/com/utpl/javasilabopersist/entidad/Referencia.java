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
import javax.persistence.Lob;
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
    @NamedQuery(name = "Referencia.findAll", query = "SELECT r FROM Referencia r"),
    @NamedQuery(name = "Referencia.findByIdReferencia", query = "SELECT r FROM Referencia r WHERE r.idReferencia = :idReferencia"),
    @NamedQuery(name = "Referencia.findByTituloReferencia", query = "SELECT r FROM Referencia r WHERE r.tituloReferencia = :tituloReferencia"),
    @NamedQuery(name = "Referencia.findByAutorPersonal", query = "SELECT r FROM Referencia r WHERE r.autorPersonal = :autorPersonal"),
    @NamedQuery(name = "Referencia.findByAutorCorporativo", query = "SELECT r FROM Referencia r WHERE r.autorCorporativo = :autorCorporativo"),
    @NamedQuery(name = "Referencia.findByEdicion", query = "SELECT r FROM Referencia r WHERE r.edicion = :edicion"),
    @NamedQuery(name = "Referencia.findByAnio", query = "SELECT r FROM Referencia r WHERE r.anio = :anio"),
    @NamedQuery(name = "Referencia.findByPaginas", query = "SELECT r FROM Referencia r WHERE r.paginas = :paginas"),
    @NamedQuery(name = "Referencia.findByUrl", query = "SELECT r FROM Referencia r WHERE r.url = :url"),
    @NamedQuery(name = "Referencia.findByPublicacion", query = "SELECT r FROM Referencia r WHERE r.publicacion = :publicacion")})
public class Referencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idReferencia;
    @Lob
    @Column(length = 65535)
    private String clasificacion;
    @Column(name = "titulo_referencia", length = 300)
    private String tituloReferencia;
    @Column(name = "autor_personal", length = 200)
    private String autorPersonal;
    @Column(name = "autor_corporativo", length = 200)
    private String autorCorporativo;
    @Column(length = 40)
    private String edicion;
    private Integer anio;
    @Column(length = 50)
    private String paginas;
    @Column(length = 500)
    private String url;
    @Column(length = 500)
    private String publicacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "referenciaId")
    private Collection<SilaboReferencia> silaboReferenciaCollection;
    @JoinColumn(name = "idTipoReferencia", referencedColumnName = "idTipoReferencia")
    @ManyToOne
    private TipoReferencia idTipoReferencia;

    public Referencia() {
    }

    public Referencia(Integer idReferencia) {
        this.idReferencia = idReferencia;
    }

    public Integer getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Integer idReferencia) {
        this.idReferencia = idReferencia;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getTituloReferencia() {
        return tituloReferencia;
    }

    public void setTituloReferencia(String tituloReferencia) {
        this.tituloReferencia = tituloReferencia;
    }

    public String getAutorPersonal() {
        return autorPersonal;
    }

    public void setAutorPersonal(String autorPersonal) {
        this.autorPersonal = autorPersonal;
    }

    public String getAutorCorporativo() {
        return autorCorporativo;
    }

    public void setAutorCorporativo(String autorCorporativo) {
        this.autorCorporativo = autorCorporativo;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getPaginas() {
        return paginas;
    }

    public void setPaginas(String paginas) {
        this.paginas = paginas;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(String publicacion) {
        this.publicacion = publicacion;
    }

    @XmlTransient
    public Collection<SilaboReferencia> getSilaboReferenciaCollection() {
        return silaboReferenciaCollection;
    }

    public void setSilaboReferenciaCollection(Collection<SilaboReferencia> silaboReferenciaCollection) {
        this.silaboReferenciaCollection = silaboReferenciaCollection;
    }

    public TipoReferencia getIdTipoReferencia() {
        return idTipoReferencia;
    }

    public void setIdTipoReferencia(TipoReferencia idTipoReferencia) {
        this.idTipoReferencia = idTipoReferencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReferencia != null ? idReferencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Referencia)) {
            return false;
        }
        Referencia other = (Referencia) object;
        if ((this.idReferencia == null && other.idReferencia != null) || (this.idReferencia != null && !this.idReferencia.equals(other.idReferencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.utpl.javasilabopersist.entidad.Referencia[ idReferencia=" + idReferencia + " ]";
    }
    
}
