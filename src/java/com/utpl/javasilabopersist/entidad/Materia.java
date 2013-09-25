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
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
    @NamedQuery(name = "Materia.findAll", query = "SELECT m FROM Materia m"),
    @NamedQuery(name = "Materia.findByIdMateria", query = "SELECT m FROM Materia m WHERE m.idMateria = :idMateria"),
    @NamedQuery(name = "Materia.findByCodigoMateria", query = "SELECT m FROM Materia m WHERE m.codigoMateria = :codigoMateria"),
    @NamedQuery(name = "Materia.findByNombreMateria", query = "SELECT m FROM Materia m WHERE m.nombreMateria = :nombreMateria"),
    @NamedQuery(name = "Materia.findByCiclo", query = "SELECT m FROM Materia m WHERE m.ciclo = :ciclo"),
    @NamedQuery(name = "Materia.findByTeorico", query = "SELECT m FROM Materia m WHERE m.teorico = :teorico"),
    @NamedQuery(name = "Materia.findByPracticas", query = "SELECT m FROM Materia m WHERE m.practicas = :practicas"),
    @NamedQuery(name = "Materia.findByTeoricoPracticas", query = "SELECT m FROM Materia m WHERE m.teoricoPracticas = :teoricoPracticas"),
    @NamedQuery(name = "Materia.findByTotal", query = "SELECT m FROM Materia m WHERE m.total = :total")})
public class Materia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idMateria;
    @Column(name = "codigo_materia", length = 15)
    private String codigoMateria;
    @Column(name = "nombre_materia", length = 200)
    private String nombreMateria;
    @Lob
    @Column(name = "descripcion_materia", length = 65535)
    private String descripcionMateria;
    private Integer ciclo;
    private Integer teorico;
    private Integer practicas;
    @Column(name = "teorico_practicas")
    private Integer teoricoPracticas;
    private Integer total;
    @JoinTable(name = "PROFESOR_MATERIA", joinColumns = {
        @JoinColumn(name = "MATERIA_ID", referencedColumnName = "idMateria", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "PROFESOR_ID", referencedColumnName = "idProfesor", nullable = false)})
    @ManyToMany
    private Collection<Profesor> profesorCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "materiaId")
    private Collection<MateriaMalla> materiaMallaCollection;
    @JoinColumn(name = "idTipoMateria", referencedColumnName = "idTipoMateria", nullable = false)
    @ManyToOne(optional = false)
    private TipoMateria idTipoMateria;
    @JoinColumn(name = "idEjeFormacion", referencedColumnName = "idEjeFormacion", nullable = false)
    @ManyToOne(optional = false)
    private EjeFormacion idEjeFormacion;
    @JoinColumn(name = "Silabo_idSilabo", referencedColumnName = "idSilabo", nullable = false)
    @ManyToOne(optional = false)
    private Silabo silaboidSilabo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMateria")
    private Collection<Contenido> contenidoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "materiaidMateria")
    private Collection<Correquisitos> correquisitosCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMateriaMateria")
    private Collection<Correquisitos> correquisitosCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "materiaidMateria")
    private Collection<Requisitos> requisitosCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMateriaMateria")
    private Collection<Requisitos> requisitosCollection1;

    public Materia() {
    }

    public Materia(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public String getCodigoMateria() {
        return codigoMateria;
    }

    public void setCodigoMateria(String codigoMateria) {
        this.codigoMateria = codigoMateria;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public String getDescripcionMateria() {
        return descripcionMateria;
    }

    public void setDescripcionMateria(String descripcionMateria) {
        this.descripcionMateria = descripcionMateria;
    }

    public Integer getCiclo() {
        return ciclo;
    }

    public void setCiclo(Integer ciclo) {
        this.ciclo = ciclo;
    }

    public Integer getTeorico() {
        return teorico;
    }

    public void setTeorico(Integer teorico) {
        this.teorico = teorico;
    }

    public Integer getPracticas() {
        return practicas;
    }

    public void setPracticas(Integer practicas) {
        this.practicas = practicas;
    }

    public Integer getTeoricoPracticas() {
        return teoricoPracticas;
    }

    public void setTeoricoPracticas(Integer teoricoPracticas) {
        this.teoricoPracticas = teoricoPracticas;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @XmlTransient
    public Collection<Profesor> getProfesorCollection() {
        return profesorCollection;
    }

    public void setProfesorCollection(Collection<Profesor> profesorCollection) {
        this.profesorCollection = profesorCollection;
    }

    @XmlTransient
    public Collection<MateriaMalla> getMateriaMallaCollection() {
        return materiaMallaCollection;
    }

    public void setMateriaMallaCollection(Collection<MateriaMalla> materiaMallaCollection) {
        this.materiaMallaCollection = materiaMallaCollection;
    }

    public TipoMateria getIdTipoMateria() {
        return idTipoMateria;
    }

    public void setIdTipoMateria(TipoMateria idTipoMateria) {
        this.idTipoMateria = idTipoMateria;
    }

    public EjeFormacion getIdEjeFormacion() {
        return idEjeFormacion;
    }

    public void setIdEjeFormacion(EjeFormacion idEjeFormacion) {
        this.idEjeFormacion = idEjeFormacion;
    }

    public Silabo getSilaboidSilabo() {
        return silaboidSilabo;
    }

    public void setSilaboidSilabo(Silabo silaboidSilabo) {
        this.silaboidSilabo = silaboidSilabo;
    }

    @XmlTransient
    public Collection<Contenido> getContenidoCollection() {
        return contenidoCollection;
    }

    public void setContenidoCollection(Collection<Contenido> contenidoCollection) {
        this.contenidoCollection = contenidoCollection;
    }

    @XmlTransient
    public Collection<Correquisitos> getCorrequisitosCollection() {
        return correquisitosCollection;
    }

    public void setCorrequisitosCollection(Collection<Correquisitos> correquisitosCollection) {
        this.correquisitosCollection = correquisitosCollection;
    }

    @XmlTransient
    public Collection<Correquisitos> getCorrequisitosCollection1() {
        return correquisitosCollection1;
    }

    public void setCorrequisitosCollection1(Collection<Correquisitos> correquisitosCollection1) {
        this.correquisitosCollection1 = correquisitosCollection1;
    }

    @XmlTransient
    public Collection<Requisitos> getRequisitosCollection() {
        return requisitosCollection;
    }

    public void setRequisitosCollection(Collection<Requisitos> requisitosCollection) {
        this.requisitosCollection = requisitosCollection;
    }

    @XmlTransient
    public Collection<Requisitos> getRequisitosCollection1() {
        return requisitosCollection1;
    }

    public void setRequisitosCollection1(Collection<Requisitos> requisitosCollection1) {
        this.requisitosCollection1 = requisitosCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMateria != null ? idMateria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Materia)) {
            return false;
        }
        Materia other = (Materia) object;
        if ((this.idMateria == null && other.idMateria != null) || (this.idMateria != null && !this.idMateria.equals(other.idMateria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.utpl.javasilabopersist.entidad.Materia[ idMateria=" + idMateria + " ]";
    }
    
}
