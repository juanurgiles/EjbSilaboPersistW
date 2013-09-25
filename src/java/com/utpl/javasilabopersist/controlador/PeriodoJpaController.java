/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.controlador;

import com.utpl.javasilabopersist.controlador.exceptions.NonexistentEntityException;
import com.utpl.javasilabopersist.entidad.Periodo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.utpl.javasilabopersist.entidad.Silabo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class PeriodoJpaController implements Serializable {

    public PeriodoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Periodo periodo) {
        if (periodo.getSilaboCollection() == null) {
            periodo.setSilaboCollection(new ArrayList<Silabo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Silabo> attachedSilaboCollection = new ArrayList<Silabo>();
            for (Silabo silaboCollectionSilaboToAttach : periodo.getSilaboCollection()) {
                silaboCollectionSilaboToAttach = em.getReference(silaboCollectionSilaboToAttach.getClass(), silaboCollectionSilaboToAttach.getIdSilabo());
                attachedSilaboCollection.add(silaboCollectionSilaboToAttach);
            }
            periodo.setSilaboCollection(attachedSilaboCollection);
            em.persist(periodo);
            for (Silabo silaboCollectionSilabo : periodo.getSilaboCollection()) {
                Periodo oldIdPeriodoOfSilaboCollectionSilabo = silaboCollectionSilabo.getIdPeriodo();
                silaboCollectionSilabo.setIdPeriodo(periodo);
                silaboCollectionSilabo = em.merge(silaboCollectionSilabo);
                if (oldIdPeriodoOfSilaboCollectionSilabo != null) {
                    oldIdPeriodoOfSilaboCollectionSilabo.getSilaboCollection().remove(silaboCollectionSilabo);
                    oldIdPeriodoOfSilaboCollectionSilabo = em.merge(oldIdPeriodoOfSilaboCollectionSilabo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Periodo periodo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Periodo persistentPeriodo = em.find(Periodo.class, periodo.getIdPeriodo());
            Collection<Silabo> silaboCollectionOld = persistentPeriodo.getSilaboCollection();
            Collection<Silabo> silaboCollectionNew = periodo.getSilaboCollection();
            Collection<Silabo> attachedSilaboCollectionNew = new ArrayList<Silabo>();
            for (Silabo silaboCollectionNewSilaboToAttach : silaboCollectionNew) {
                silaboCollectionNewSilaboToAttach = em.getReference(silaboCollectionNewSilaboToAttach.getClass(), silaboCollectionNewSilaboToAttach.getIdSilabo());
                attachedSilaboCollectionNew.add(silaboCollectionNewSilaboToAttach);
            }
            silaboCollectionNew = attachedSilaboCollectionNew;
            periodo.setSilaboCollection(silaboCollectionNew);
            periodo = em.merge(periodo);
            for (Silabo silaboCollectionOldSilabo : silaboCollectionOld) {
                if (!silaboCollectionNew.contains(silaboCollectionOldSilabo)) {
                    silaboCollectionOldSilabo.setIdPeriodo(null);
                    silaboCollectionOldSilabo = em.merge(silaboCollectionOldSilabo);
                }
            }
            for (Silabo silaboCollectionNewSilabo : silaboCollectionNew) {
                if (!silaboCollectionOld.contains(silaboCollectionNewSilabo)) {
                    Periodo oldIdPeriodoOfSilaboCollectionNewSilabo = silaboCollectionNewSilabo.getIdPeriodo();
                    silaboCollectionNewSilabo.setIdPeriodo(periodo);
                    silaboCollectionNewSilabo = em.merge(silaboCollectionNewSilabo);
                    if (oldIdPeriodoOfSilaboCollectionNewSilabo != null && !oldIdPeriodoOfSilaboCollectionNewSilabo.equals(periodo)) {
                        oldIdPeriodoOfSilaboCollectionNewSilabo.getSilaboCollection().remove(silaboCollectionNewSilabo);
                        oldIdPeriodoOfSilaboCollectionNewSilabo = em.merge(oldIdPeriodoOfSilaboCollectionNewSilabo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = periodo.getIdPeriodo();
                if (findPeriodo(id) == null) {
                    throw new NonexistentEntityException("The periodo with id " + id + " no longer exists.");
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
            Periodo periodo;
            try {
                periodo = em.getReference(Periodo.class, id);
                periodo.getIdPeriodo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The periodo with id " + id + " no longer exists.", enfe);
            }
            Collection<Silabo> silaboCollection = periodo.getSilaboCollection();
            for (Silabo silaboCollectionSilabo : silaboCollection) {
                silaboCollectionSilabo.setIdPeriodo(null);
                silaboCollectionSilabo = em.merge(silaboCollectionSilabo);
            }
            em.remove(periodo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Periodo> findPeriodoEntities() {
        return findPeriodoEntities(true, -1, -1);
    }

    public List<Periodo> findPeriodoEntities(int maxResults, int firstResult) {
        return findPeriodoEntities(false, maxResults, firstResult);
    }

    private List<Periodo> findPeriodoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Periodo.class));
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

    public Periodo findPeriodo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Periodo.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeriodoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Periodo> rt = cq.from(Periodo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
