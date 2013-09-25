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
    @NamedQuery(name = "Silabo.findAll", query = "SELECT s FROM Silabo s"),
    @NamedQuery(name = "Silabo.findByIdSilabo", query = "SELECT s FROM Silabo s WHERE s.idSilabo = :idSilabo"),
    @NamedQuery(name = "Silabo.findByCodigoSilabo", query = "SELECT s FROM Silabo s WHERE s.codigoSilabo = :codigoSilabo"),
    @NamedQuery(name = "Silabo.findByNombreSilabo", query = "SELECT s FROM Silabo s WHERE s.nombreSilabo = :nombreSilabo"),
    @NamedQuery(name = "Silabo.findByRef1", query = "SELECT s FROM Silabo s WHERE s.ref1 = :ref1"),
    @NamedQuery(name = "Silabo.findByRef2", query = "SELECT s FROM Silabo s WHERE s.ref2 = :ref2"),
    @NamedQuery(name = "Silabo.findByRef3", query = "SELECT s FROM Silabo s WHERE s.ref3 = :ref3"),
    @NamedQuery(name = "Silabo.findByRef4", query = "SELECT s FROM Silabo s WHERE s.ref4 = :ref4"),
    @NamedQuery(name = "Silabo.findByRef5", query = "SELECT s FROM Silabo s WHERE s.ref5 = :ref5"),
    @NamedQuery(name = "Silabo.findByRef6", query = "SELECT s FROM Silabo s WHERE s.ref6 = :ref6")})
public class Silabo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idSilabo;
    @Column(name = "codigo_silabo", length = 20)
    private String codigoSilabo;
    @Column(name = "nombre_silabo", length = 100)
    private String nombreSilabo;
    @Lob
    @Column(name = "objetivos_materia", length = 65535)
    private String objetivosMateria;
    @Lob
    @Column(name = "resultados_logros", length = 65535)
    private String resultadosLogros;
    @Lob
    @Column(length = 65535)
    private String indicadores;
    @Lob
    @Column(name = "situaciones_evaluacion", length = 65535)
    private String situacionesEvaluacion;
    @Lob
    @Column(name = "criterios_evaluacion_aprendizaje", length = 65535)
    private String criteriosEvaluacionAprendizaje;
    @Lob
    @Column(name = "criterios_evaluacion_curso", length = 65535)
    private String criteriosEvaluacionCurso;
    @Basic(optional = false)
    @Lob
    @Column(nullable = false, length = 65535)
    private String recursos;
    @Basic(optional = false)
    @Column(nullable = false, length = 300)
    private String ref1;
    @Basic(optional = false)
    @Column(nullable = false, length = 300)
    private String ref2;
    @Basic(optional = false)
    @Column(nullable = false, length = 300)
    private String ref3;
    @Basic(optional = false)
    @Column(nullable = false, length = 300)
    private String ref4;
    @Basic(optional = false)
    @Column(nullable = false, length = 300)
    private String ref5;
    @Basic(optional = false)
    @Column(nullable = false, length = 300)
    private String ref6;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "silaboidSilabo")
    private Collection<Materia> materiaCollection;
    @JoinColumn(name = "idPeriodo", referencedColumnName = "idPeriodo")
    @ManyToOne
    private Periodo idPeriodo;
    @JoinColumn(name = "idModalidad", referencedColumnName = "idModalidad")
    @ManyToOne
    private Modalidad idModalidad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSilabo")
    private Collection<Logros> logrosCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "silaboId")
    private Collection<SilaboReferencia> silaboReferenciaCollection;

    public Silabo() {
    }

    public Silabo(Integer idSilabo) {
        this.idSilabo = idSilabo;
    }

    public Silabo(Integer idSilabo, String recursos, String ref1, String ref2, String ref3, String ref4, String ref5, String ref6) {
        this.idSilabo = idSilabo;
        this.recursos = recursos;
        this.ref1 = ref1;
        this.ref2 = ref2;
        this.ref3 = ref3;
        this.ref4 = ref4;
        this.ref5 = ref5;
        this.ref6 = ref6;
    }

    public Integer getIdSilabo() {
        return idSilabo;
    }

    public void setIdSilabo(Integer idSilabo) {
        this.idSilabo = idSilabo;
    }

    public String getCodigoSilabo() {
        return codigoSilabo;
    }

    public void setCodigoSilabo(String codigoSilabo) {
        this.codigoSilabo = codigoSilabo;
    }

    public String getNombreSilabo() {
        return nombreSilabo;
    }

    public void setNombreSilabo(String nombreSilabo) {
        this.nombreSilabo = nombreSilabo;
    }

    public String getObjetivosMateria() {
        return objetivosMateria;
    }

    public void setObjetivosMateria(String objetivosMateria) {
        this.objetivosMateria = objetivosMateria;
    }

    public String getResultadosLogros() {
        return resultadosLogros;
    }

    public void setResultadosLogros(String resultadosLogros) {
        this.resultadosLogros = resultadosLogros;
    }

    public String getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(String indicadores) {
        this.indicadores = indicadores;
    }

    public String getSituacionesEvaluacion() {
        return situacionesEvaluacion;
    }

    public void setSituacionesEvaluacion(String situacionesEvaluacion) {
        this.situacionesEvaluacion = situacionesEvaluacion;
    }

    public String getCriteriosEvaluacionAprendizaje() {
        return criteriosEvaluacionAprendizaje;
    }

    public void setCriteriosEvaluacionAprendizaje(String criteriosEvaluacionAprendizaje) {
        this.criteriosEvaluacionAprendizaje = criteriosEvaluacionAprendizaje;
    }

    public String getCriteriosEvaluacionCurso() {
        return criteriosEvaluacionCurso;
    }

    public void setCriteriosEvaluacionCurso(String criteriosEvaluacionCurso) {
        this.criteriosEvaluacionCurso = criteriosEvaluacionCurso;
    }

    public String getRecursos() {
        return recursos;
    }

    public void setRecursos(String recursos) {
        this.recursos = recursos;
    }

    public String getRef1() {
        return ref1;
    }

    public void setRef1(String ref1) {
        this.ref1 = ref1;
    }

    public String getRef2() {
        return ref2;
    }

    public void setRef2(String ref2) {
        this.ref2 = ref2;
    }

    public String getRef3() {
        return ref3;
    }

    public void setRef3(String ref3) {
        this.ref3 = ref3;
    }

    public String getRef4() {
        return ref4;
    }

    public void setRef4(String ref4) {
        this.ref4 = ref4;
    }

    public String getRef5() {
        return ref5;
    }

    public void setRef5(String ref5) {
        this.ref5 = ref5;
    }

    public String getRef6() {
        return ref6;
    }

    public void setRef6(String ref6) {
        this.ref6 = ref6;
    }

    @XmlTransient
    public Collection<Materia> getMateriaCollection() {
        return materiaCollection;
    }

    public void setMateriaCollection(Collection<Materia> materiaCollection) {
        this.materiaCollection = materiaCollection;
    }

    public Periodo getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Periodo idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Modalidad getIdModalidad() {
        return idModalidad;
    }

    public void setIdModalidad(Modalidad idModalidad) {
        this.idModalidad = idModalidad;
    }

    @XmlTransient
    public Collection<Logros> getLogrosCollection() {
        return logrosCollection;
    }

    public void setLogrosCollection(Collection<Logros> logrosCollection) {
        this.logrosCollection = logrosCollection;
    }

    @XmlTransient
    public Collection<SilaboReferencia> getSilaboReferenciaCollection() {
        return silaboReferenciaCollection;
    }

    public void setSilaboReferenciaCollection(Collection<SilaboReferencia> silaboReferenciaCollection) {
        this.silaboReferenciaCollection = silaboReferenciaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSilabo != null ? idSilabo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Silabo)) {
            return false;
        }
        Silabo other = (Silabo) object;
        if ((this.idSilabo == null && other.idSilabo != null) || (this.idSilabo != null && !this.idSilabo.equals(other.idSilabo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.utpl.javasilabopersist.entidad.Silabo[ idSilabo=" + idSilabo + " ]";
    }
    
}
