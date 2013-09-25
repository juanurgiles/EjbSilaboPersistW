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
    @NamedQuery(name = "Contenido.findAll", query = "SELECT c FROM Contenido c"),
    @NamedQuery(name = "Contenido.findByIdContenido", query = "SELECT c FROM Contenido c WHERE c.idContenido = :idContenido"),
    @NamedQuery(name = "Contenido.findByCodigo", query = "SELECT c FROM Contenido c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "Contenido.findByNumeroSesion", query = "SELECT c FROM Contenido c WHERE c.numeroSesion = :numeroSesion"),
    @NamedQuery(name = "Contenido.findByNombreContenido", query = "SELECT c FROM Contenido c WHERE c.nombreContenido = :nombreContenido")})
public class Contenido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idContenido;
    @Column(length = 10)
    private String codigo;
    @Column(name = "numero_sesion")
    private Integer numeroSesion;
    @Column(name = "nombre_contenido", length = 45)
    private String nombreContenido;
    @Lob
    @Column(length = 65535)
    private String contenido;
    @Lob
    @Column(name = "estrategias_aprendizaje", length = 65535)
    private String estrategiasAprendizaje;
    @JoinColumn(name = "idMateria", referencedColumnName = "idMateria", nullable = false)
    @ManyToOne(optional = false)
    private Materia idMateria;
    @OneToMany(mappedBy = "idContenido")
    private Collection<Logro> logroCollection;

    public Contenido() {
    }

    public Contenido(Integer idContenido) {
        this.idContenido = idContenido;
    }

    public Integer getIdContenido() {
        return idContenido;
    }

    public void setIdContenido(Integer idContenido) {
        this.idContenido = idContenido;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getNumeroSesion() {
        return numeroSesion;
    }

    public void setNumeroSesion(Integer numeroSesion) {
        this.numeroSesion = numeroSesion;
    }

    public String getNombreContenido() {
        return nombreContenido;
    }

    public void setNombreContenido(String nombreContenido) {
        this.nombreContenido = nombreContenido;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getEstrategiasAprendizaje() {
        return estrategiasAprendizaje;
    }

    public void setEstrategiasAprendizaje(String estrategiasAprendizaje) {
        this.estrategiasAprendizaje = estrategiasAprendizaje;
    }

    public Materia getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Materia idMateria) {
        this.idMateria = idMateria;
    }

    @XmlTransient
    public Collection<Logro> getLogroCollection() {
        return logroCollection;
    }

    public void setLogroCollection(Collection<Logro> logroCollection) {
        this.logroCollection = logroCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContenido != null ? idContenido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contenido)) {
            return false;
        }
        Contenido other = (Contenido) object;
        if ((this.idContenido == null && other.idContenido != null) || (this.idContenido != null && !this.idContenido.equals(other.idContenido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.utpl.javasilabopersist.entidad.Contenido[ idContenido=" + idContenido + " ]";
    }
    
}
