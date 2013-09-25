/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.controlador;

import com.utpl.javasilabopersist.controlador.exceptions.IllegalOrphanException;
import com.utpl.javasilabopersist.controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.utpl.javasilabopersist.entidad.TipoMateria;
import com.utpl.javasilabopersist.entidad.EjeFormacion;
import com.utpl.javasilabopersist.entidad.Silabo;
import com.utpl.javasilabopersist.entidad.Profesor;
import java.util.ArrayList;
import java.util.Collection;
import com.utpl.javasilabopersist.entidad.MateriaMalla;
import com.utpl.javasilabopersist.entidad.Contenido;
import com.utpl.javasilabopersist.entidad.Correquisitos;
import com.utpl.javasilabopersist.entidad.Materia;
import com.utpl.javasilabopersist.entidad.Requisitos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class MateriaJpaController implements Serializable {

    public MateriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Materia materia) {
        if (materia.getProfesorCollection() == null) {
            materia.setProfesorCollection(new ArrayList<Profesor>());
        }
        if (materia.getMateriaMallaCollection() == null) {
            materia.setMateriaMallaCollection(new ArrayList<MateriaMalla>());
        }
        if (materia.getContenidoCollection() == null) {
            materia.setContenidoCollection(new ArrayList<Contenido>());
        }
        if (materia.getCorrequisitosCollection() == null) {
            materia.setCorrequisitosCollection(new ArrayList<Correquisitos>());
        }
        if (materia.getCorrequisitosCollection1() == null) {
            materia.setCorrequisitosCollection1(new ArrayList<Correquisitos>());
        }
        if (materia.getRequisitosCollection() == null) {
            materia.setRequisitosCollection(new ArrayList<Requisitos>());
        }
        if (materia.getRequisitosCollection1() == null) {
            materia.setRequisitosCollection1(new ArrayList<Requisitos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoMateria idTipoMateria = materia.getIdTipoMateria();
            if (idTipoMateria != null) {
                idTipoMateria = em.getReference(idTipoMateria.getClass(), idTipoMateria.getIdTipoMateria());
                materia.setIdTipoMateria(idTipoMateria);
            }
            EjeFormacion idEjeFormacion = materia.getIdEjeFormacion();
            if (idEjeFormacion != null) {
                idEjeFormacion = em.getReference(idEjeFormacion.getClass(), idEjeFormacion.getIdEjeFormacion());
                materia.setIdEjeFormacion(idEjeFormacion);
            }
            Silabo silaboidSilabo = materia.getSilaboidSilabo();
            if (silaboidSilabo != null) {
                silaboidSilabo = em.getReference(silaboidSilabo.getClass(), silaboidSilabo.getIdSilabo());
                materia.setSilaboidSilabo(silaboidSilabo);
            }
            Collection<Profesor> attachedProfesorCollection = new ArrayList<Profesor>();
            for (Profesor profesorCollectionProfesorToAttach : materia.getProfesorCollection()) {
                profesorCollectionProfesorToAttach = em.getReference(profesorCollectionProfesorToAttach.getClass(), profesorCollectionProfesorToAttach.getIdProfesor());
                attachedProfesorCollection.add(profesorCollectionProfesorToAttach);
            }
            materia.setProfesorCollection(attachedProfesorCollection);
            Collection<MateriaMalla> attachedMateriaMallaCollection = new ArrayList<MateriaMalla>();
            for (MateriaMalla materiaMallaCollectionMateriaMallaToAttach : materia.getMateriaMallaCollection()) {
                materiaMallaCollectionMateriaMallaToAttach = em.getReference(materiaMallaCollectionMateriaMallaToAttach.getClass(), materiaMallaCollectionMateriaMallaToAttach.getIdMATERIAMALLA());
                attachedMateriaMallaCollection.add(materiaMallaCollectionMateriaMallaToAttach);
            }
            materia.setMateriaMallaCollection(attachedMateriaMallaCollection);
            Collection<Contenido> attachedContenidoCollection = new ArrayList<Contenido>();
            for (Contenido contenidoCollectionContenidoToAttach : materia.getContenidoCollection()) {
                contenidoCollectionContenidoToAttach = em.getReference(contenidoCollectionContenidoToAttach.getClass(), contenidoCollectionContenidoToAttach.getIdContenido());
                attachedContenidoCollection.add(contenidoCollectionContenidoToAttach);
            }
            materia.setContenidoCollection(attachedContenidoCollection);
            Collection<Correquisitos> attachedCorrequisitosCollection = new ArrayList<Correquisitos>();
            for (Correquisitos correquisitosCollectionCorrequisitosToAttach : materia.getCorrequisitosCollection()) {
                correquisitosCollectionCorrequisitosToAttach = em.getReference(correquisitosCollectionCorrequisitosToAttach.getClass(), correquisitosCollectionCorrequisitosToAttach.getIdRequisito());
                attachedCorrequisitosCollection.add(correquisitosCollectionCorrequisitosToAttach);
            }
            materia.setCorrequisitosCollection(attachedCorrequisitosCollection);
            Collection<Correquisitos> attachedCorrequisitosCollection1 = new ArrayList<Correquisitos>();
            for (Correquisitos correquisitosCollection1CorrequisitosToAttach : materia.getCorrequisitosCollection1()) {
                correquisitosCollection1CorrequisitosToAttach = em.getReference(correquisitosCollection1CorrequisitosToAttach.getClass(), correquisitosCollection1CorrequisitosToAttach.getIdRequisito());
                attachedCorrequisitosCollection1.add(correquisitosCollection1CorrequisitosToAttach);
            }
            materia.setCorrequisitosCollection1(attachedCorrequisitosCollection1);
            Collection<Requisitos> attachedRequisitosCollection = new ArrayList<Requisitos>();
            for (Requisitos requisitosCollectionRequisitosToAttach : materia.getRequisitosCollection()) {
                requisitosCollectionRequisitosToAttach = em.getReference(requisitosCollectionRequisitosToAttach.getClass(), requisitosCollectionRequisitosToAttach.getIdRequisito());
                attachedRequisitosCollection.add(requisitosCollectionRequisitosToAttach);
            }
            materia.setRequisitosCollection(attachedRequisitosCollection);
            Collection<Requisitos> attachedRequisitosCollection1 = new ArrayList<Requisitos>();
            for (Requisitos requisitosCollection1RequisitosToAttach : materia.getRequisitosCollection1()) {
                requisitosCollection1RequisitosToAttach = em.getReference(requisitosCollection1RequisitosToAttach.getClass(), requisitosCollection1RequisitosToAttach.getIdRequisito());
                attachedRequisitosCollection1.add(requisitosCollection1RequisitosToAttach);
            }
            materia.setRequisitosCollection1(attachedRequisitosCollection1);
            em.persist(materia);
            if (idTipoMateria != null) {
                idTipoMateria.getMateriaCollection().add(materia);
                idTipoMateria = em.merge(idTipoMateria);
            }
            if (idEjeFormacion != null) {
                idEjeFormacion.getMateriaCollection().add(materia);
                idEjeFormacion = em.merge(idEjeFormacion);
            }
            if (silaboidSilabo != null) {
                silaboidSilabo.getMateriaCollection().add(materia);
                silaboidSilabo = em.merge(silaboidSilabo);
            }
            for (Profesor profesorCollectionProfesor : materia.getProfesorCollection()) {
                profesorCollectionProfesor.getMateriaCollection().add(materia);
                profesorCollectionProfesor = em.merge(profesorCollectionProfesor);
            }
            for (MateriaMalla materiaMallaCollectionMateriaMalla : materia.getMateriaMallaCollection()) {
                Materia oldMateriaIdOfMateriaMallaCollectionMateriaMalla = materiaMallaCollectionMateriaMalla.getMateriaId();
                materiaMallaCollectionMateriaMalla.setMateriaId(materia);
                materiaMallaCollectionMateriaMalla = em.merge(materiaMallaCollectionMateriaMalla);
                if (oldMateriaIdOfMateriaMallaCollectionMateriaMalla != null) {
                    oldMateriaIdOfMateriaMallaCollectionMateriaMalla.getMateriaMallaCollection().remove(materiaMallaCollectionMateriaMalla);
                    oldMateriaIdOfMateriaMallaCollectionMateriaMalla = em.merge(oldMateriaIdOfMateriaMallaCollectionMateriaMalla);
                }
            }
            for (Contenido contenidoCollectionContenido : materia.getContenidoCollection()) {
                Materia oldIdMateriaOfContenidoCollectionContenido = contenidoCollectionContenido.getIdMateria();
                contenidoCollectionContenido.setIdMateria(materia);
                contenidoCollectionContenido = em.merge(contenidoCollectionContenido);
                if (oldIdMateriaOfContenidoCollectionContenido != null) {
                    oldIdMateriaOfContenidoCollectionContenido.getContenidoCollection().remove(contenidoCollectionContenido);
                    oldIdMateriaOfContenidoCollectionContenido = em.merge(oldIdMateriaOfContenidoCollectionContenido);
                }
            }
            for (Correquisitos correquisitosCollectionCorrequisitos : materia.getCorrequisitosCollection()) {
                Materia oldMateriaidMateriaOfCorrequisitosCollectionCorrequisitos = correquisitosCollectionCorrequisitos.getMateriaidMateria();
                correquisitosCollectionCorrequisitos.setMateriaidMateria(materia);
                correquisitosCollectionCorrequisitos = em.merge(correquisitosCollectionCorrequisitos);
                if (oldMateriaidMateriaOfCorrequisitosCollectionCorrequisitos != null) {
                    oldMateriaidMateriaOfCorrequisitosCollectionCorrequisitos.getCorrequisitosCollection().remove(correquisitosCollectionCorrequisitos);
                    oldMateriaidMateriaOfCorrequisitosCollectionCorrequisitos = em.merge(oldMateriaidMateriaOfCorrequisitosCollectionCorrequisitos);
                }
            }
            for (Correquisitos correquisitosCollection1Correquisitos : materia.getCorrequisitosCollection1()) {
                Materia oldIdMateriaMateriaOfCorrequisitosCollection1Correquisitos = correquisitosCollection1Correquisitos.getIdMateriaMateria();
                correquisitosCollection1Correquisitos.setIdMateriaMateria(materia);
                correquisitosCollection1Correquisitos = em.merge(correquisitosCollection1Correquisitos);
                if (oldIdMateriaMateriaOfCorrequisitosCollection1Correquisitos != null) {
                    oldIdMateriaMateriaOfCorrequisitosCollection1Correquisitos.getCorrequisitosCollection1().remove(correquisitosCollection1Correquisitos);
                    oldIdMateriaMateriaOfCorrequisitosCollection1Correquisitos = em.merge(oldIdMateriaMateriaOfCorrequisitosCollection1Correquisitos);
                }
            }
            for (Requisitos requisitosCollectionRequisitos : materia.getRequisitosCollection()) {
                Materia oldMateriaidMateriaOfRequisitosCollectionRequisitos = requisitosCollectionRequisitos.getMateriaidMateria();
                requisitosCollectionRequisitos.setMateriaidMateria(materia);
                requisitosCollectionRequisitos = em.merge(requisitosCollectionRequisitos);
                if (oldMateriaidMateriaOfRequisitosCollectionRequisitos != null) {
                    oldMateriaidMateriaOfRequisitosCollectionRequisitos.getRequisitosCollection().remove(requisitosCollectionRequisitos);
                    oldMateriaidMateriaOfRequisitosCollectionRequisitos = em.merge(oldMateriaidMateriaOfRequisitosCollectionRequisitos);
                }
            }
            for (Requisitos requisitosCollection1Requisitos : materia.getRequisitosCollection1()) {
                Materia oldIdMateriaMateriaOfRequisitosCollection1Requisitos = requisitosCollection1Requisitos.getIdMateriaMateria();
                requisitosCollection1Requisitos.setIdMateriaMateria(materia);
                requisitosCollection1Requisitos = em.merge(requisitosCollection1Requisitos);
                if (oldIdMateriaMateriaOfRequisitosCollection1Requisitos != null) {
                    oldIdMateriaMateriaOfRequisitosCollection1Requisitos.getRequisitosCollection1().remove(requisitosCollection1Requisitos);
                    oldIdMateriaMateriaOfRequisitosCollection1Requisitos = em.merge(oldIdMateriaMateriaOfRequisitosCollection1Requisitos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Materia materia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia persistentMateria = em.find(Materia.class, materia.getIdMateria());
            TipoMateria idTipoMateriaOld = persistentMateria.getIdTipoMateria();
            TipoMateria idTipoMateriaNew = materia.getIdTipoMateria();
            EjeFormacion idEjeFormacionOld = persistentMateria.getIdEjeFormacion();
            EjeFormacion idEjeFormacionNew = materia.getIdEjeFormacion();
            Silabo silaboidSilaboOld = persistentMateria.getSilaboidSilabo();
            Silabo silaboidSilaboNew = materia.getSilaboidSilabo();
            Collection<Profesor> profesorCollectionOld = persistentMateria.getProfesorCollection();
            Collection<Profesor> profesorCollectionNew = materia.getProfesorCollection();
            Collection<MateriaMalla> materiaMallaCollectionOld = persistentMateria.getMateriaMallaCollection();
            Collection<MateriaMalla> materiaMallaCollectionNew = materia.getMateriaMallaCollection();
            Collection<Contenido> contenidoCollectionOld = persistentMateria.getContenidoCollection();
            Collection<Contenido> contenidoCollectionNew = materia.getContenidoCollection();
            Collection<Correquisitos> correquisitosCollectionOld = persistentMateria.getCorrequisitosCollection();
            Collection<Correquisitos> correquisitosCollectionNew = materia.getCorrequisitosCollection();
            Collection<Correquisitos> correquisitosCollection1Old = persistentMateria.getCorrequisitosCollection1();
            Collection<Correquisitos> correquisitosCollection1New = materia.getCorrequisitosCollection1();
            Collection<Requisitos> requisitosCollectionOld = persistentMateria.getRequisitosCollection();
            Collection<Requisitos> requisitosCollectionNew = materia.getRequisitosCollection();
            Collection<Requisitos> requisitosCollection1Old = persistentMateria.getRequisitosCollection1();
            Collection<Requisitos> requisitosCollection1New = materia.getRequisitosCollection1();
            List<String> illegalOrphanMessages = null;
            for (MateriaMalla materiaMallaCollectionOldMateriaMalla : materiaMallaCollectionOld) {
                if (!materiaMallaCollectionNew.contains(materiaMallaCollectionOldMateriaMalla)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MateriaMalla " + materiaMallaCollectionOldMateriaMalla + " since its materiaId field is not nullable.");
                }
            }
            for (Contenido contenidoCollectionOldContenido : contenidoCollectionOld) {
                if (!contenidoCollectionNew.contains(contenidoCollectionOldContenido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contenido " + contenidoCollectionOldContenido + " since its idMateria field is not nullable.");
                }
            }
            for (Correquisitos correquisitosCollectionOldCorrequisitos : correquisitosCollectionOld) {
                if (!correquisitosCollectionNew.contains(correquisitosCollectionOldCorrequisitos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Correquisitos " + correquisitosCollectionOldCorrequisitos + " since its materiaidMateria field is not nullable.");
                }
            }
            for (Correquisitos correquisitosCollection1OldCorrequisitos : correquisitosCollection1Old) {
                if (!correquisitosCollection1New.contains(correquisitosCollection1OldCorrequisitos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Correquisitos " + correquisitosCollection1OldCorrequisitos + " since its idMateriaMateria field is not nullable.");
                }
            }
            for (Requisitos requisitosCollectionOldRequisitos : requisitosCollectionOld) {
                if (!requisitosCollectionNew.contains(requisitosCollectionOldRequisitos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Requisitos " + requisitosCollectionOldRequisitos + " since its materiaidMateria field is not nullable.");
                }
            }
            for (Requisitos requisitosCollection1OldRequisitos : requisitosCollection1Old) {
                if (!requisitosCollection1New.contains(requisitosCollection1OldRequisitos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Requisitos " + requisitosCollection1OldRequisitos + " since its idMateriaMateria field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTipoMateriaNew != null) {
                idTipoMateriaNew = em.getReference(idTipoMateriaNew.getClass(), idTipoMateriaNew.getIdTipoMateria());
                materia.setIdTipoMateria(idTipoMateriaNew);
            }
            if (idEjeFormacionNew != null) {
                idEjeFormacionNew = em.getReference(idEjeFormacionNew.getClass(), idEjeFormacionNew.getIdEjeFormacion());
                materia.setIdEjeFormacion(idEjeFormacionNew);
            }
            if (silaboidSilaboNew != null) {
                silaboidSilaboNew = em.getReference(silaboidSilaboNew.getClass(), silaboidSilaboNew.getIdSilabo());
                materia.setSilaboidSilabo(silaboidSilaboNew);
            }
            Collection<Profesor> attachedProfesorCollectionNew = new ArrayList<Profesor>();
            for (Profesor profesorCollectionNewProfesorToAttach : profesorCollectionNew) {
                profesorCollectionNewProfesorToAttach = em.getReference(profesorCollectionNewProfesorToAttach.getClass(), profesorCollectionNewProfesorToAttach.getIdProfesor());
                attachedProfesorCollectionNew.add(profesorCollectionNewProfesorToAttach);
            }
            profesorCollectionNew = attachedProfesorCollectionNew;
            materia.setProfesorCollection(profesorCollectionNew);
            Collection<MateriaMalla> attachedMateriaMallaCollectionNew = new ArrayList<MateriaMalla>();
            for (MateriaMalla materiaMallaCollectionNewMateriaMallaToAttach : materiaMallaCollectionNew) {
                materiaMallaCollectionNewMateriaMallaToAttach = em.getReference(materiaMallaCollectionNewMateriaMallaToAttach.getClass(), materiaMallaCollectionNewMateriaMallaToAttach.getIdMATERIAMALLA());
                attachedMateriaMallaCollectionNew.add(materiaMallaCollectionNewMateriaMallaToAttach);
            }
            materiaMallaCollectionNew = attachedMateriaMallaCollectionNew;
            materia.setMateriaMallaCollection(materiaMallaCollectionNew);
            Collection<Contenido> attachedContenidoCollectionNew = new ArrayList<Contenido>();
            for (Contenido contenidoCollectionNewContenidoToAttach : contenidoCollectionNew) {
                contenidoCollectionNewContenidoToAttach = em.getReference(contenidoCollectionNewContenidoToAttach.getClass(), contenidoCollectionNewContenidoToAttach.getIdContenido());
                attachedContenidoCollectionNew.add(contenidoCollectionNewContenidoToAttach);
            }
            contenidoCollectionNew = attachedContenidoCollectionNew;
            materia.setContenidoCollection(contenidoCollectionNew);
            Collection<Correquisitos> attachedCorrequisitosCollectionNew = new ArrayList<Correquisitos>();
            for (Correquisitos correquisitosCollectionNewCorrequisitosToAttach : correquisitosCollectionNew) {
                correquisitosCollectionNewCorrequisitosToAttach = em.getReference(correquisitosCollectionNewCorrequisitosToAttach.getClass(), correquisitosCollectionNewCorrequisitosToAttach.getIdRequisito());
                attachedCorrequisitosCollectionNew.add(correquisitosCollectionNewCorrequisitosToAttach);
            }
            correquisitosCollectionNew = attachedCorrequisitosCollectionNew;
            materia.setCorrequisitosCollection(correquisitosCollectionNew);
            Collection<Correquisitos> attachedCorrequisitosCollection1New = new ArrayList<Correquisitos>();
            for (Correquisitos correquisitosCollection1NewCorrequisitosToAttach : correquisitosCollection1New) {
                correquisitosCollection1NewCorrequisitosToAttach = em.getReference(correquisitosCollection1NewCorrequisitosToAttach.getClass(), correquisitosCollection1NewCorrequisitosToAttach.getIdRequisito());
                attachedCorrequisitosCollection1New.add(correquisitosCollection1NewCorrequisitosToAttach);
            }
            correquisitosCollection1New = attachedCorrequisitosCollection1New;
            materia.setCorrequisitosCollection1(correquisitosCollection1New);
            Collection<Requisitos> attachedRequisitosCollectionNew = new ArrayList<Requisitos>();
            for (Requisitos requisitosCollectionNewRequisitosToAttach : requisitosCollectionNew) {
                requisitosCollectionNewRequisitosToAttach = em.getReference(requisitosCollectionNewRequisitosToAttach.getClass(), requisitosCollectionNewRequisitosToAttach.getIdRequisito());
                attachedRequisitosCollectionNew.add(requisitosCollectionNewRequisitosToAttach);
            }
            requisitosCollectionNew = attachedRequisitosCollectionNew;
            materia.setRequisitosCollection(requisitosCollectionNew);
            Collection<Requisitos> attachedRequisitosCollection1New = new ArrayList<Requisitos>();
            for (Requisitos requisitosCollection1NewRequisitosToAttach : requisitosCollection1New) {
                requisitosCollection1NewRequisitosToAttach = em.getReference(requisitosCollection1NewRequisitosToAttach.getClass(), requisitosCollection1NewRequisitosToAttach.getIdRequisito());
                attachedRequisitosCollection1New.add(requisitosCollection1NewRequisitosToAttach);
            }
            requisitosCollection1New = attachedRequisitosCollection1New;
            materia.setRequisitosCollection1(requisitosCollection1New);
            materia = em.merge(materia);
            if (idTipoMateriaOld != null && !idTipoMateriaOld.equals(idTipoMateriaNew)) {
                idTipoMateriaOld.getMateriaCollection().remove(materia);
                idTipoMateriaOld = em.merge(idTipoMateriaOld);
            }
            if (idTipoMateriaNew != null && !idTipoMateriaNew.equals(idTipoMateriaOld)) {
                idTipoMateriaNew.getMateriaCollection().add(materia);
                idTipoMateriaNew = em.merge(idTipoMateriaNew);
            }
            if (idEjeFormacionOld != null && !idEjeFormacionOld.equals(idEjeFormacionNew)) {
                idEjeFormacionOld.getMateriaCollection().remove(materia);
                idEjeFormacionOld = em.merge(idEjeFormacionOld);
            }
            if (idEjeFormacionNew != null && !idEjeFormacionNew.equals(idEjeFormacionOld)) {
                idEjeFormacionNew.getMateriaCollection().add(materia);
                idEjeFormacionNew = em.merge(idEjeFormacionNew);
            }
            if (silaboidSilaboOld != null && !silaboidSilaboOld.equals(silaboidSilaboNew)) {
                silaboidSilaboOld.getMateriaCollection().remove(materia);
                silaboidSilaboOld = em.merge(silaboidSilaboOld);
            }
            if (silaboidSilaboNew != null && !silaboidSilaboNew.equals(silaboidSilaboOld)) {
                silaboidSilaboNew.getMateriaCollection().add(materia);
                silaboidSilaboNew = em.merge(silaboidSilaboNew);
            }
            for (Profesor profesorCollectionOldProfesor : profesorCollectionOld) {
                if (!profesorCollectionNew.contains(profesorCollectionOldProfesor)) {
                    profesorCollectionOldProfesor.getMateriaCollection().remove(materia);
                    profesorCollectionOldProfesor = em.merge(profesorCollectionOldProfesor);
                }
            }
            for (Profesor profesorCollectionNewProfesor : profesorCollectionNew) {
                if (!profesorCollectionOld.contains(profesorCollectionNewProfesor)) {
                    profesorCollectionNewProfesor.getMateriaCollection().add(materia);
                    profesorCollectionNewProfesor = em.merge(profesorCollectionNewProfesor);
                }
            }
            for (MateriaMalla materiaMallaCollectionNewMateriaMalla : materiaMallaCollectionNew) {
                if (!materiaMallaCollectionOld.contains(materiaMallaCollectionNewMateriaMalla)) {
                    Materia oldMateriaIdOfMateriaMallaCollectionNewMateriaMalla = materiaMallaCollectionNewMateriaMalla.getMateriaId();
                    materiaMallaCollectionNewMateriaMalla.setMateriaId(materia);
                    materiaMallaCollectionNewMateriaMalla = em.merge(materiaMallaCollectionNewMateriaMalla);
                    if (oldMateriaIdOfMateriaMallaCollectionNewMateriaMalla != null && !oldMateriaIdOfMateriaMallaCollectionNewMateriaMalla.equals(materia)) {
                        oldMateriaIdOfMateriaMallaCollectionNewMateriaMalla.getMateriaMallaCollection().remove(materiaMallaCollectionNewMateriaMalla);
                        oldMateriaIdOfMateriaMallaCollectionNewMateriaMalla = em.merge(oldMateriaIdOfMateriaMallaCollectionNewMateriaMalla);
                    }
                }
            }
            for (Contenido contenidoCollectionNewContenido : contenidoCollectionNew) {
                if (!contenidoCollectionOld.contains(contenidoCollectionNewContenido)) {
                    Materia oldIdMateriaOfContenidoCollectionNewContenido = contenidoCollectionNewContenido.getIdMateria();
                    contenidoCollectionNewContenido.setIdMateria(materia);
                    contenidoCollectionNewContenido = em.merge(contenidoCollectionNewContenido);
                    if (oldIdMateriaOfContenidoCollectionNewContenido != null && !oldIdMateriaOfContenidoCollectionNewContenido.equals(materia)) {
                        oldIdMateriaOfContenidoCollectionNewContenido.getContenidoCollection().remove(contenidoCollectionNewContenido);
                        oldIdMateriaOfContenidoCollectionNewContenido = em.merge(oldIdMateriaOfContenidoCollectionNewContenido);
                    }
                }
            }
            for (Correquisitos correquisitosCollectionNewCorrequisitos : correquisitosCollectionNew) {
                if (!correquisitosCollectionOld.contains(correquisitosCollectionNewCorrequisitos)) {
                    Materia oldMateriaidMateriaOfCorrequisitosCollectionNewCorrequisitos = correquisitosCollectionNewCorrequisitos.getMateriaidMateria();
                    correquisitosCollectionNewCorrequisitos.setMateriaidMateria(materia);
                    correquisitosCollectionNewCorrequisitos = em.merge(correquisitosCollectionNewCorrequisitos);
                    if (oldMateriaidMateriaOfCorrequisitosCollectionNewCorrequisitos != null && !oldMateriaidMateriaOfCorrequisitosCollectionNewCorrequisitos.equals(materia)) {
                        oldMateriaidMateriaOfCorrequisitosCollectionNewCorrequisitos.getCorrequisitosCollection().remove(correquisitosCollectionNewCorrequisitos);
                        oldMateriaidMateriaOfCorrequisitosCollectionNewCorrequisitos = em.merge(oldMateriaidMateriaOfCorrequisitosCollectionNewCorrequisitos);
                    }
                }
            }
            for (Correquisitos correquisitosCollection1NewCorrequisitos : correquisitosCollection1New) {
                if (!correquisitosCollection1Old.contains(correquisitosCollection1NewCorrequisitos)) {
                    Materia oldIdMateriaMateriaOfCorrequisitosCollection1NewCorrequisitos = correquisitosCollection1NewCorrequisitos.getIdMateriaMateria();
                    correquisitosCollection1NewCorrequisitos.setIdMateriaMateria(materia);
                    correquisitosCollection1NewCorrequisitos = em.merge(correquisitosCollection1NewCorrequisitos);
                    if (oldIdMateriaMateriaOfCorrequisitosCollection1NewCorrequisitos != null && !oldIdMateriaMateriaOfCorrequisitosCollection1NewCorrequisitos.equals(materia)) {
                        oldIdMateriaMateriaOfCorrequisitosCollection1NewCorrequisitos.getCorrequisitosCollection1().remove(correquisitosCollection1NewCorrequisitos);
                        oldIdMateriaMateriaOfCorrequisitosCollection1NewCorrequisitos = em.merge(oldIdMateriaMateriaOfCorrequisitosCollection1NewCorrequisitos);
                    }
                }
            }
            for (Requisitos requisitosCollectionNewRequisitos : requisitosCollectionNew) {
                if (!requisitosCollectionOld.contains(requisitosCollectionNewRequisitos)) {
                    Materia oldMateriaidMateriaOfRequisitosCollectionNewRequisitos = requisitosCollectionNewRequisitos.getMateriaidMateria();
                    requisitosCollectionNewRequisitos.setMateriaidMateria(materia);
                    requisitosCollectionNewRequisitos = em.merge(requisitosCollectionNewRequisitos);
                    if (oldMateriaidMateriaOfRequisitosCollectionNewRequisitos != null && !oldMateriaidMateriaOfRequisitosCollectionNewRequisitos.equals(materia)) {
                        oldMateriaidMateriaOfRequisitosCollectionNewRequisitos.getRequisitosCollection().remove(requisitosCollectionNewRequisitos);
                        oldMateriaidMateriaOfRequisitosCollectionNewRequisitos = em.merge(oldMateriaidMateriaOfRequisitosCollectionNewRequisitos);
                    }
                }
            }
            for (Requisitos requisitosCollection1NewRequisitos : requisitosCollection1New) {
                if (!requisitosCollection1Old.contains(requisitosCollection1NewRequisitos)) {
                    Materia oldIdMateriaMateriaOfRequisitosCollection1NewRequisitos = requisitosCollection1NewRequisitos.getIdMateriaMateria();
                    requisitosCollection1NewRequisitos.setIdMateriaMateria(materia);
                    requisitosCollection1NewRequisitos = em.merge(requisitosCollection1NewRequisitos);
                    if (oldIdMateriaMateriaOfRequisitosCollection1NewRequisitos != null && !oldIdMateriaMateriaOfRequisitosCollection1NewRequisitos.equals(materia)) {
                        oldIdMateriaMateriaOfRequisitosCollection1NewRequisitos.getRequisitosCollection1().remove(requisitosCollection1NewRequisitos);
                        oldIdMateriaMateriaOfRequisitosCollection1NewRequisitos = em.merge(oldIdMateriaMateriaOfRequisitosCollection1NewRequisitos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = materia.getIdMateria();
                if (findMateria(id) == null) {
                    throw new NonexistentEntityException("The materia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia materia;
            try {
                materia = em.getReference(Materia.class, id);
                materia.getIdMateria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<MateriaMalla> materiaMallaCollectionOrphanCheck = materia.getMateriaMallaCollection();
            for (MateriaMalla materiaMallaCollectionOrphanCheckMateriaMalla : materiaMallaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the MateriaMalla " + materiaMallaCollectionOrphanCheckMateriaMalla + " in its materiaMallaCollection field has a non-nullable materiaId field.");
            }
            Collection<Contenido> contenidoCollectionOrphanCheck = materia.getContenidoCollection();
            for (Contenido contenidoCollectionOrphanCheckContenido : contenidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the Contenido " + contenidoCollectionOrphanCheckContenido + " in its contenidoCollection field has a non-nullable idMateria field.");
            }
            Collection<Correquisitos> correquisitosCollectionOrphanCheck = materia.getCorrequisitosCollection();
            for (Correquisitos correquisitosCollectionOrphanCheckCorrequisitos : correquisitosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the Correquisitos " + correquisitosCollectionOrphanCheckCorrequisitos + " in its correquisitosCollection field has a non-nullable materiaidMateria field.");
            }
            Collection<Correquisitos> correquisitosCollection1OrphanCheck = materia.getCorrequisitosCollection1();
            for (Correquisitos correquisitosCollection1OrphanCheckCorrequisitos : correquisitosCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the Correquisitos " + correquisitosCollection1OrphanCheckCorrequisitos + " in its correquisitosCollection1 field has a non-nullable idMateriaMateria field.");
            }
            Collection<Requisitos> requisitosCollectionOrphanCheck = materia.getRequisitosCollection();
            for (Requisitos requisitosCollectionOrphanCheckRequisitos : requisitosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the Requisitos " + requisitosCollectionOrphanCheckRequisitos + " in its requisitosCollection field has a non-nullable materiaidMateria field.");
            }
            Collection<Requisitos> requisitosCollection1OrphanCheck = materia.getRequisitosCollection1();
            for (Requisitos requisitosCollection1OrphanCheckRequisitos : requisitosCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the Requisitos " + requisitosCollection1OrphanCheckRequisitos + " in its requisitosCollection1 field has a non-nullable idMateriaMateria field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoMateria idTipoMateria = materia.getIdTipoMateria();
            if (idTipoMateria != null) {
                idTipoMateria.getMateriaCollection().remove(materia);
                idTipoMateria = em.merge(idTipoMateria);
            }
            EjeFormacion idEjeFormacion = materia.getIdEjeFormacion();
            if (idEjeFormacion != null) {
                idEjeFormacion.getMateriaCollection().remove(materia);
                idEjeFormacion = em.merge(idEjeFormacion);
            }
            Silabo silaboidSilabo = materia.getSilaboidSilabo();
            if (silaboidSilabo != null) {
                silaboidSilabo.getMateriaCollection().remove(materia);
                silaboidSilabo = em.merge(silaboidSilabo);
            }
            Collection<Profesor> profesorCollection = materia.getProfesorCollection();
            for (Profesor profesorCollectionProfesor : profesorCollection) {
                profesorCollectionProfesor.getMateriaCollection().remove(materia);
                profesorCollectionProfesor = em.merge(profesorCollectionProfesor);
            }
            em.remove(materia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Materia> findMateriaEntities() {
        return findMateriaEntities(true, -1, -1);
    }

    public List<Materia> findMateriaEntities(int maxResults, int firstResult) {
        return findMateriaEntities(false, maxResults, firstResult);
    }

    private List<Materia> findMateriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Materia.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Materia findMateria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Materia.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Materia> rt = cq.from(Materia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
