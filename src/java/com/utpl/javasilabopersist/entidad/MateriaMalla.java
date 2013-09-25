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
@Table(name = "MATERIA_MALLA", catalog = "digysoft_fartes1", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MateriaMalla.findAll", query = "SELECT m FROM MateriaMalla m"),
    @NamedQuery(name = "MateriaMalla.findByIdMATERIAMALLA", query = "SELECT m FROM MateriaMalla m WHERE m.idMATERIAMALLA = :idMATERIAMALLA")})
public class MateriaMalla implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idMATERIA_MALLA", nullable = false, length = 45)
    private String idMATERIAMALLA;
    @JoinColumn(name = "Ciclo_IdCiclo", referencedColumnName = "IdCiclo")
    @ManyToOne
    private Ciclo cicloIdCiclo;
    @JoinColumn(name = "MALLA_ID", referencedColumnName = "idMalla", nullable = false)
    @ManyToOne(optional = false)
    private Malla mallaId;
    @JoinColumn(name = "MATERIA_ID", referencedColumnName = "idMateria", nullable = false)
    @ManyToOne(optional = false)
    private Materia materiaId;

    public MateriaMalla() {
    }

    public MateriaMalla(String idMATERIAMALLA) {
        this.idMATERIAMALLA = idMATERIAMALLA;
    }

    public String getIdMATERIAMALLA() {
        return idMATERIAMALLA;
    }

    public void setIdMATERIAMALLA(String idMATERIAMALLA) {
        this.idMATERIAMALLA = idMATERIAMALLA;
    }

    public Ciclo getCicloIdCiclo() {
        return cicloIdCiclo;
    }

    public void setCicloIdCiclo(Ciclo cicloIdCiclo) {
        this.cicloIdCiclo = cicloIdCiclo;
    }

    public Malla getMallaId() {
        return mallaId;
    }

    public void setMallaId(Malla mallaId) {
        this.mallaId = mallaId;
    }

    public Materia getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(Materia materiaId) {
        this.materiaId = materiaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMATERIAMALLA != null ? idMATERIAMALLA.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MateriaMalla)) {
            return false;
        }
        MateriaMalla other = (MateriaMalla) object;
        if ((this.idMATERIAMALLA == null && other.idMATERIAMALLA != null) || (this.idMATERIAMALLA != null && !this.idMATERIAMALLA.equals(other.idMATERIAMALLA))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.utpl.javasilabopersist.entidad.MateriaMalla[ idMATERIAMALLA=" + idMATERIAMALLA + " ]";
    }
    
}
