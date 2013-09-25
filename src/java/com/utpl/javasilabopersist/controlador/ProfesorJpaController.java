/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.controlador;

import com.utpl.javasilabopersist.controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.utpl.javasilabopersist.entidad.TipoU;
import com.utpl.javasilabopersist.entidad.Materia;
import com.utpl.javasilabopersist.entidad.Profesor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class ProfesorJpaController implements Serializable {

    public ProfesorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profesor profesor) {
        if (profesor.getMateriaCollection() == null) {
            profesor.setMateriaCollection(new ArrayList<Materia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoU tipoUid = profesor.getTipoUid();
            if (tipoUid != null) {
                tipoUid = em.getReference(tipoUid.getClass(), tipoUid.getId());
                profesor.setTipoUid(tipoUid);
            }
            Collection<Materia> attachedMateriaCollection = new ArrayList<Materia>();
            for (Materia materiaCollectionMateriaToAttach : profesor.getMateriaCollection()) {
                materiaCollectionMateriaToAttach = em.getReference(materiaCollectionMateriaToAttach.getClass(), materiaCollectionMateriaToAttach.getIdMateria());
                attachedMateriaCollection.add(materiaCollectionMateriaToAttach);
            }
            profesor.setMateriaCollection(attachedMateriaCollection);
            em.persist(profesor);
            if (tipoUid != null) {
                tipoUid.getProfesorCollection().add(profesor);
                tipoUid = em.merge(tipoUid);
            }
            for (Materia materiaCollectionMateria : profesor.getMateriaCollection()) {
                materiaCollectionMateria.getProfesorCollection().add(profesor);
                materiaCollectionMateria = em.merge(materiaCollectionMateria);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profesor profesor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesor persistentProfesor = em.find(Profesor.class, profesor.getIdProfesor());
            TipoU tipoUidOld = persistentProfesor.getTipoUid();
            TipoU tipoUidNew = profesor.getTipoUid();
            Collection<Materia> materiaCollectionOld = persistentProfesor.getMateriaCollection();
            Collection<Materia> materiaCollectionNew = profesor.getMateriaCollection();
            if (tipoUidNew != null) {
                tipoUidNew = em.getReference(tipoUidNew.getClass(), tipoUidNew.getId());
                profesor.setTipoUid(tipoUidNew);
            }
            Collection<Materia> attachedMateriaCollectionNew = new ArrayList<Materia>();
            for (Materia materiaCollectionNewMateriaToAttach : materiaCollectionNew) {
                materiaCollectionNewMateriaToAttach = em.getReference(materiaCollectionNewMateriaToAttach.getClass(), materiaCollectionNewMateriaToAttach.getIdMateria());
                attachedMateriaCollectionNew.add(materiaCollectionNewMateriaToAttach);
            }
            materiaCollectionNew = attachedMateriaCollectionNew;
            profesor.setMateriaCollection(materiaCollectionNew);
            profesor = em.merge(profesor);
            if (tipoUidOld != null && !tipoUidOld.equals(tipoUidNew)) {
                tipoUidOld.getProfesorCollection().remove(profesor);
                tipoUidOld = em.merge(tipoUidOld);
            }
            if (tipoUidNew != null && !tipoUidNew.equals(tipoUidOld)) {
                tipoUidNew.getProfesorCollection().add(profesor);
                tipoUidNew = em.merge(tipoUidNew);
            }
            for (Materia materiaCollectionOldMateria : materiaCollectionOld) {
                if (!materiaCollectionNew.contains(materiaCollectionOldMateria)) {
                    materiaCollectionOldMateria.getProfesorCollection().remove(profesor);
                    materiaCollectionOldMateria = em.merge(materiaCollectionOldMateria);
                }
            }
            for (Materia materiaCollectionNewMateria : materiaCollectionNew) {
                if (!materiaCollectionOld.contains(materiaCollectionNewMateria)) {
                    materiaCollectionNewMateria.getProfesorCollection().add(profesor);
                    materiaCollectionNewMateria = em.merge(materiaCollectionNewMateria);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = profesor.getIdProfesor();
                if (findProfesor(id) == null) {
                    throw new NonexistentEntityException("The profesor with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesor profesor;
            try {
                profesor = em.getReference(Profesor.class, id);
                profesor.getIdProfesor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profesor with id " + id + " no longer exists.", enfe);
            }
            TipoU tipoUid = profesor.getTipoUid();
            if (tipoUid != null) {
                tipoUid.getProfesorCollection().remove(profesor);
                tipoUid = em.merge(tipoUid);
            }
            Collection<Materia> materiaCollection = profesor.getMateriaCollection();
            for (Materia materiaCollectionMateria : materiaCollection) {
                materiaCollectionMateria.getProfesorCollection().remove(profesor);
                materiaCollectionMateria = em.merge(materiaCollectionMateria);
            }
            em.remove(profesor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profesor> findProfesorEntities() {
        return findProfesorEntities(true, -1, -1);
    }

    public List<Profesor> findProfesorEntities(int maxResults, int firstResult) {
        return findProfesorEntities(false, maxResults, firstResult);
    }

    private List<Profesor> findProfesorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profesor.class));
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

    public Profesor findProfesor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profesor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfesorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profesor> rt = cq.from(Profesor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
