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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(catalog = "digysoft_fartes1", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Logro.findAll", query = "SELECT l FROM Logro l"),
    @NamedQuery(name = "Logro.findByIdLogro", query = "SELECT l FROM Logro l WHERE l.idLogro = :idLogro"),
    @NamedQuery(name = "Logro.findByNombre", query = "SELECT l FROM Logro l WHERE l.nombre = :nombre"),
    @NamedQuery(name = "Logro.findByValor", query = "SELECT l FROM Logro l WHERE l.valor = :valor"),
    @NamedQuery(name = "Logro.findByDebe", query = "SELECT l FROM Logro l WHERE l.debe = :debe")})
public class Logro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idLogro;
    @Column(length = 100)
    private String nombre;
    @Lob
    @Column(length = 65535)
    private String descripcion;
    private Integer valor;
    @Column(length = 100)
    private String debe;
    @JoinColumn(name = "idContenido", referencedColumnName = "idContenido")
    @ManyToOne
    private Contenido idContenido;

    public Logro() {
    }

    public Logro(Integer idLogro) {
        this.idLogro = idLogro;
    }

    public Integer getIdLogro() {
        return idLogro;
    }

    public void setIdLogro(Integer idLogro) {
        this.idLogro = idLogro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public String getDebe() {
        return debe;
    }

    public void setDebe(String debe) {
        this.debe = debe;
    }

    public Contenido getIdContenido() {
        return idContenido;
    }

    public void setIdContenido(Contenido idContenido) {
        this.idContenido = idContenido;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLogro != null ? idLogro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Logro)) {
            return false;
        }
        Logro other = (Logro) object;
        if ((this.idLogro == null && other.idLogro != null) || (this.idLogro != null && !this.idLogro.equals(other.idLogro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.utpl.javasilabopersist.entidad.Logro[ idLogro=" + idLogro + " ]";
    }
    
}
