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
import com.utpl.javasilabopersist.entidad.Profesort;
import com.utpl.javasilabopersist.entidad.TipoT;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class TipoTJpaController implements Serializable {

    public TipoTJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoT tipoT) {
        if (tipoT.getProfesortCollection() == null) {
            tipoT.setProfesortCollection(new ArrayList<Profesort>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Profesort> attachedProfesortCollection = new ArrayList<Profesort>();
            for (Profesort profesortCollectionProfesortToAttach : tipoT.getProfesortCollection()) {
                profesortCollectionProfesortToAttach = em.getReference(profesortCollectionProfesortToAttach.getClass(), profesortCollectionProfesortToAttach.getId());
                attachedProfesortCollection.add(profesortCollectionProfesortToAttach);
            }
            tipoT.setProfesortCollection(attachedProfesortCollection);
            em.persist(tipoT);
            for (Profesort profesortCollectionProfesort : tipoT.getProfesortCollection()) {
                TipoT oldTipoTidOfProfesortCollectionProfesort = profesortCollectionProfesort.getTipoTid();
                profesortCollectionProfesort.setTipoTid(tipoT);
                profesortCollectionProfesort = em.merge(profesortCollectionProfesort);
                if (oldTipoTidOfProfesortCollectionProfesort != null) {
                    oldTipoTidOfProfesortCollectionProfesort.getProfesortCollection().remove(profesortCollectionProfesort);
                    oldTipoTidOfProfesortCollectionProfesort = em.merge(oldTipoTidOfProfesortCollectionProfesort);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoT tipoT) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoT persistentTipoT = em.find(TipoT.class, tipoT.getId());
            Collection<Profesort> profesortCollectionOld = persistentTipoT.getProfesortCollection();
            Collection<Profesort> profesortCollectionNew = tipoT.getProfesortCollection();
            List<String> illegalOrphanMessages = null;
            for (Profesort profesortCollectionOldProfesort : profesortCollectionOld) {
                if (!profesortCollectionNew.contains(profesortCollectionOldProfesort)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Profesort " + profesortCollectionOldProfesort + " since its tipoTid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Profesort> attachedProfesortCollectionNew = new ArrayList<Profesort>();
            for (Profesort profesortCollectionNewProfesortToAttach : profesortCollectionNew) {
                profesortCollectionNewProfesortToAttach = em.getReference(profesortCollectionNewProfesortToAttach.getClass(), profesortCollectionNewProfesortToAttach.getId());
                attachedProfesortCollectionNew.add(profesortCollectionNewProfesortToAttach);
            }
            profesortCollectionNew = attachedProfesortCollectionNew;
            tipoT.setProfesortCollection(profesortCollectionNew);
            tipoT = em.merge(tipoT);
            for (Profesort profesortCollectionNewProfesort : profesortCollectionNew) {
                if (!profesortCollectionOld.contains(profesortCollectionNewProfesort)) {
                    TipoT oldTipoTidOfProfesortCollectionNewProfesort = profesortCollectionNewProfesort.getTipoTid();
                    profesortCollectionNewProfesort.setTipoTid(tipoT);
                    profesortCollectionNewProfesort = em.merge(profesortCollectionNewProfesort);
                    if (oldTipoTidOfProfesortCollectionNewProfesort != null && !oldTipoTidOfProfesortCollectionNewProfesort.equals(tipoT)) {
                        oldTipoTidOfProfesortCollectionNewProfesort.getProfesortCollection().remove(profesortCollectionNewProfesort);
                        oldTipoTidOfProfesortCollectionNewProfesort = em.merge(oldTipoTidOfProfesortCollectionNewProfesort);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoT.getId();
                if (findTipoT(id) == null) {
                    throw new NonexistentEntityException("The tipoT with id " + id + " no longer exists.");
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
            TipoT tipoT;
            try {
                tipoT = em.getReference(TipoT.class, id);
                tipoT.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoT with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Profesort> profesortCollectionOrphanCheck = tipoT.getProfesortCollection();
            for (Profesort profesortCollectionOrphanCheckProfesort : profesortCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoT (" + tipoT + ") cannot be destroyed since the Profesort " + profesortCollectionOrphanCheckProfesort + " in its profesortCollection field has a non-nullable tipoTid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoT);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoT> findTipoTEntities() {
        return findTipoTEntities(true, -1, -1);
    }

    public List<TipoT> findTipoTEntities(int maxResults, int firstResult) {
        return findTipoTEntities(false, maxResults, firstResult);
    }

    private List<TipoT> findTipoTEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoT.class));
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

    public TipoT findTipoT(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoT.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoTCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoT> rt = cq.from(TipoT.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
