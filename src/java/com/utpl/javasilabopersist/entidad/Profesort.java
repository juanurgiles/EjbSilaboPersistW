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
    @NamedQuery(name = "Profesort.findAll", query = "SELECT p FROM Profesort p"),
    @NamedQuery(name = "Profesort.findById", query = "SELECT p FROM Profesort p WHERE p.id = :id"),
    @NamedQuery(name = "Profesort.findByNombre", query = "SELECT p FROM Profesort p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Profesort.findByCorreo", query = "SELECT p FROM Profesort p WHERE p.correo = :correo"),
    @NamedQuery(name = "Profesort.findByUsuario", query = "SELECT p FROM Profesort p WHERE p.usuario = :usuario"),
    @NamedQuery(name = "Profesort.findByPass", query = "SELECT p FROM Profesort p WHERE p.pass = :pass")})
public class Profesort implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(length = 45)
    private String nombre;
    @Column(length = 45)
    private String correo;
    @Column(length = 45)
    private String usuario;
    @Column(length = 45)
    private String pass;
    @JoinColumn(name = "tipoT_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private TipoT tipoTid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profesortId")
    private Collection<Materiat> materiatCollection;

    public Profesort() {
    }

    public Profesort(Integer id) {
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public TipoT getTipoTid() {
        return tipoTid;
    }

    public void setTipoTid(TipoT tipoTid) {
        this.tipoTid = tipoTid;
    }

    @XmlTransient
    public Collection<Materiat> getMateriatCollection() {
        return materiatCollection;
    }

    public void setMateriatCollection(Collection<Materiat> materiatCollection) {
        this.materiatCollection = materiatCollection;
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
        if (!(object instanceof Profesort)) {
            return false;
        }
        Profesort other = (Profesort) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.utpl.javasilabopersist.entidad.Profesort[ id=" + id + " ]";
    }
    
}
