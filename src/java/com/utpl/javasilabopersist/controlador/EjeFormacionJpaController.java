/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.controlador;

import com.utpl.javasilabopersist.controlador.exceptions.IllegalOrphanException;
import com.utpl.javasilabopersist.controlador.exceptions.NonexistentEntityException;
import com.utpl.javasilabopersist.entidad.EjeFormacion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.utpl.javasilabopersist.entidad.Materia;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class EjeFormacionJpaController implements Serializable {

    public EjeFormacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EjeFormacion ejeFormacion) {
        if (ejeFormacion.getMateriaCollection() == null) {
            ejeFormacion.setMateriaCollection(new ArrayList<Materia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Materia> attachedMateriaCollection = new ArrayList<Materia>();
            for (Materia materiaCollectionMateriaToAttach : ejeFormacion.getMateriaCollection()) {
                materiaCollectionMateriaToAttach = em.getReference(materiaCollectionMateriaToAttach.getClass(), materiaCollectionMateriaToAttach.getIdMateria());
                attachedMateriaCollection.add(materiaCollectionMateriaToAttach);
            }
            ejeFormacion.setMateriaCollection(attachedMateriaCollection);
            em.persist(ejeFormacion);
            for (Materia materiaCollectionMateria : ejeFormacion.getMateriaCollection()) {
                EjeFormacion oldIdEjeFormacionOfMateriaCollectionMateria = materiaCollectionMateria.getIdEjeFormacion();
                materiaCollectionMateria.setIdEjeFormacion(ejeFormacion);
                materiaCollectionMateria = em.merge(materiaCollectionMateria);
                if (oldIdEjeFormacionOfMateriaCollectionMateria != null) {
                    oldIdEjeFormacionOfMateriaCollectionMateria.getMateriaCollection().remove(materiaCollectionMateria);
                    oldIdEjeFormacionOfMateriaCollectionMateria = em.merge(oldIdEjeFormacionOfMateriaCollectionMateria);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EjeFormacion ejeFormacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EjeFormacion persistentEjeFormacion = em.find(EjeFormacion.class, ejeFormacion.getIdEjeFormacion());
            Collection<Materia> materiaCollectionOld = persistentEjeFormacion.getMateriaCollection();
            Collection<Materia> materiaCollectionNew = ejeFormacion.getMateriaCollection();
            List<String> illegalOrphanMessages = null;
            for (Materia materiaCollectionOldMateria : materiaCollectionOld) {
                if (!materiaCollectionNew.contains(materiaCollectionOldMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Materia " + materiaCollectionOldMateria + " since its idEjeFormacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Materia> attachedMateriaCollectionNew = new ArrayList<Materia>();
            for (Materia materiaCollectionNewMateriaToAttach : materiaCollectionNew) {
                materiaCollectionNewMateriaToAttach = em.getReference(materiaCollectionNewMateriaToAttach.getClass(), materiaCollectionNewMateriaToAttach.getIdMateria());
                attachedMateriaCollectionNew.add(materiaCollectionNewMateriaToAttach);
            }
            materiaCollectionNew = attachedMateriaCollectionNew;
            ejeFormacion.setMateriaCollection(materiaCollectionNew);
            ejeFormacion = em.merge(ejeFormacion);
            for (Materia materiaCollectionNewMateria : materiaCollectionNew) {
                if (!materiaCollectionOld.contains(materiaCollectionNewMateria)) {
                    EjeFormacion oldIdEjeFormacionOfMateriaCollectionNewMateria = materiaCollectionNewMateria.getIdEjeFormacion();
                    materiaCollectionNewMateria.setIdEjeFormacion(ejeFormacion);
                    materiaCollectionNewMateria = em.merge(materiaCollectionNewMateria);
                    if (oldIdEjeFormacionOfMateriaCollectionNewMateria != null && !oldIdEjeFormacionOfMateriaCollectionNewMateria.equals(ejeFormacion)) {
                        oldIdEjeFormacionOfMateriaCollectionNewMateria.getMateriaCollection().remove(materiaCollectionNewMateria);
                        oldIdEjeFormacionOfMateriaCollectionNewMateria = em.merge(oldIdEjeFormacionOfMateriaCollectionNewMateria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ejeFormacion.getIdEjeFormacion();
                if (findEjeFormacion(id) == null) {
                    throw new NonexistentEntityException("The ejeFormacion with id " + id + " no longer exists.");
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
            EjeFormacion ejeFormacion;
            try {
                ejeFormacion = em.getReference(EjeFormacion.class, id);
                ejeFormacion.getIdEjeFormacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ejeFormacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Materia> materiaCollectionOrphanCheck = ejeFormacion.getMateriaCollection();
            for (Materia materiaCollectionOrphanCheckMateria : materiaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EjeFormacion (" + ejeFormacion + ") cannot be destroyed since the Materia " + materiaCollectionOrphanCheckMateria + " in its materiaCollection field has a non-nullable idEjeFormacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(ejeFormacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EjeFormacion> findEjeFormacionEntities() {
        return findEjeFormacionEntities(true, -1, -1);
    }

    public List<EjeFormacion> findEjeFormacionEntities(int maxResults, int firstResult) {
        return findEjeFormacionEntities(false, maxResults, firstResult);
    }

    private List<EjeFormacion> findEjeFormacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EjeFormacion.class));
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

    public EjeFormacion findEjeFormacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EjeFormacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getEjeFormacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EjeFormacion> rt = cq.from(EjeFormacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
