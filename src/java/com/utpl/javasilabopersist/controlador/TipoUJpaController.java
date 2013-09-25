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
import com.utpl.javasilabopersist.entidad.Profesor;
import com.utpl.javasilabopersist.entidad.TipoU;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class TipoUJpaController implements Serializable {

    public TipoUJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoU tipoU) {
        if (tipoU.getProfesorCollection() == null) {
            tipoU.setProfesorCollection(new ArrayList<Profesor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Profesor> attachedProfesorCollection = new ArrayList<Profesor>();
            for (Profesor profesorCollectionProfesorToAttach : tipoU.getProfesorCollection()) {
                profesorCollectionProfesorToAttach = em.getReference(profesorCollectionProfesorToAttach.getClass(), profesorCollectionProfesorToAttach.getIdProfesor());
                attachedProfesorCollection.add(profesorCollectionProfesorToAttach);
            }
            tipoU.setProfesorCollection(attachedProfesorCollection);
            em.persist(tipoU);
            for (Profesor profesorCollectionProfesor : tipoU.getProfesorCollection()) {
                TipoU oldTipoUidOfProfesorCollectionProfesor = profesorCollectionProfesor.getTipoUid();
                profesorCollectionProfesor.setTipoUid(tipoU);
                profesorCollectionProfesor = em.merge(profesorCollectionProfesor);
                if (oldTipoUidOfProfesorCollectionProfesor != null) {
                    oldTipoUidOfProfesorCollectionProfesor.getProfesorCollection().remove(profesorCollectionProfesor);
                    oldTipoUidOfProfesorCollectionProfesor = em.merge(oldTipoUidOfProfesorCollectionProfesor);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoU tipoU) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoU persistentTipoU = em.find(TipoU.class, tipoU.getId());
            Collection<Profesor> profesorCollectionOld = persistentTipoU.getProfesorCollection();
            Collection<Profesor> profesorCollectionNew = tipoU.getProfesorCollection();
            List<String> illegalOrphanMessages = null;
            for (Profesor profesorCollectionOldProfesor : profesorCollectionOld) {
                if (!profesorCollectionNew.contains(profesorCollectionOldProfesor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Profesor " + profesorCollectionOldProfesor + " since its tipoUid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Profesor> attachedProfesorCollectionNew = new ArrayList<Profesor>();
            for (Profesor profesorCollectionNewProfesorToAttach : profesorCollectionNew) {
                profesorCollectionNewProfesorToAttach = em.getReference(profesorCollectionNewProfesorToAttach.getClass(), profesorCollectionNewProfesorToAttach.getIdProfesor());
                attachedProfesorCollectionNew.add(profesorCollectionNewProfesorToAttach);
            }
            profesorCollectionNew = attachedProfesorCollectionNew;
            tipoU.setProfesorCollection(profesorCollectionNew);
            tipoU = em.merge(tipoU);
            for (Profesor profesorCollectionNewProfesor : profesorCollectionNew) {
                if (!profesorCollectionOld.contains(profesorCollectionNewProfesor)) {
                    TipoU oldTipoUidOfProfesorCollectionNewProfesor = profesorCollectionNewProfesor.getTipoUid();
                    profesorCollectionNewProfesor.setTipoUid(tipoU);
                    profesorCollectionNewProfesor = em.merge(profesorCollectionNewProfesor);
                    if (oldTipoUidOfProfesorCollectionNewProfesor != null && !oldTipoUidOfProfesorCollectionNewProfesor.equals(tipoU)) {
                        oldTipoUidOfProfesorCollectionNewProfesor.getProfesorCollection().remove(profesorCollectionNewProfesor);
                        oldTipoUidOfProfesorCollectionNewProfesor = em.merge(oldTipoUidOfProfesorCollectionNewProfesor);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoU.getId();
                if (findTipoU(id) == null) {
                    throw new NonexistentEntityException("The tipoU with id " + id + " no longer exists.");
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
            TipoU tipoU;
            try {
                tipoU = em.getReference(TipoU.class, id);
                tipoU.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoU with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Profesor> profesorCollectionOrphanCheck = tipoU.getProfesorCollection();
            for (Profesor profesorCollectionOrphanCheckProfesor : profesorCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoU (" + tipoU + ") cannot be destroyed since the Profesor " + profesorCollectionOrphanCheckProfesor + " in its profesorCollection field has a non-nullable tipoUid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoU);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoU> findTipoUEntities() {
        return findTipoUEntities(true, -1, -1);
    }

    public List<TipoU> findTipoUEntities(int maxResults, int firstResult) {
        return findTipoUEntities(false, maxResults, firstResult);
    }

    private List<TipoU> findTipoUEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoU.class));
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

    public TipoU findTipoU(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoU.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoUCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoU> rt = cq.from(TipoU.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
