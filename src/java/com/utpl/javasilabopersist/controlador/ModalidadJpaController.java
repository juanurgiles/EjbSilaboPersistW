/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.controlador;

import com.utpl.javasilabopersist.controlador.exceptions.NonexistentEntityException;
import com.utpl.javasilabopersist.entidad.Modalidad;
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
public class ModalidadJpaController implements Serializable {

    public ModalidadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Modalidad modalidad) {
        if (modalidad.getSilaboCollection() == null) {
            modalidad.setSilaboCollection(new ArrayList<Silabo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Silabo> attachedSilaboCollection = new ArrayList<Silabo>();
            for (Silabo silaboCollectionSilaboToAttach : modalidad.getSilaboCollection()) {
                silaboCollectionSilaboToAttach = em.getReference(silaboCollectionSilaboToAttach.getClass(), silaboCollectionSilaboToAttach.getIdSilabo());
                attachedSilaboCollection.add(silaboCollectionSilaboToAttach);
            }
            modalidad.setSilaboCollection(attachedSilaboCollection);
            em.persist(modalidad);
            for (Silabo silaboCollectionSilabo : modalidad.getSilaboCollection()) {
                Modalidad oldIdModalidadOfSilaboCollectionSilabo = silaboCollectionSilabo.getIdModalidad();
                silaboCollectionSilabo.setIdModalidad(modalidad);
                silaboCollectionSilabo = em.merge(silaboCollectionSilabo);
                if (oldIdModalidadOfSilaboCollectionSilabo != null) {
                    oldIdModalidadOfSilaboCollectionSilabo.getSilaboCollection().remove(silaboCollectionSilabo);
                    oldIdModalidadOfSilaboCollectionSilabo = em.merge(oldIdModalidadOfSilaboCollectionSilabo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Modalidad modalidad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Modalidad persistentModalidad = em.find(Modalidad.class, modalidad.getIdModalidad());
            Collection<Silabo> silaboCollectionOld = persistentModalidad.getSilaboCollection();
            Collection<Silabo> silaboCollectionNew = modalidad.getSilaboCollection();
            Collection<Silabo> attachedSilaboCollectionNew = new ArrayList<Silabo>();
            for (Silabo silaboCollectionNewSilaboToAttach : silaboCollectionNew) {
                silaboCollectionNewSilaboToAttach = em.getReference(silaboCollectionNewSilaboToAttach.getClass(), silaboCollectionNewSilaboToAttach.getIdSilabo());
                attachedSilaboCollectionNew.add(silaboCollectionNewSilaboToAttach);
            }
            silaboCollectionNew = attachedSilaboCollectionNew;
            modalidad.setSilaboCollection(silaboCollectionNew);
            modalidad = em.merge(modalidad);
            for (Silabo silaboCollectionOldSilabo : silaboCollectionOld) {
                if (!silaboCollectionNew.contains(silaboCollectionOldSilabo)) {
                    silaboCollectionOldSilabo.setIdModalidad(null);
                    silaboCollectionOldSilabo = em.merge(silaboCollectionOldSilabo);
                }
            }
            for (Silabo silaboCollectionNewSilabo : silaboCollectionNew) {
                if (!silaboCollectionOld.contains(silaboCollectionNewSilabo)) {
                    Modalidad oldIdModalidadOfSilaboCollectionNewSilabo = silaboCollectionNewSilabo.getIdModalidad();
                    silaboCollectionNewSilabo.setIdModalidad(modalidad);
                    silaboCollectionNewSilabo = em.merge(silaboCollectionNewSilabo);
                    if (oldIdModalidadOfSilaboCollectionNewSilabo != null && !oldIdModalidadOfSilaboCollectionNewSilabo.equals(modalidad)) {
                        oldIdModalidadOfSilaboCollectionNewSilabo.getSilaboCollection().remove(silaboCollectionNewSilabo);
                        oldIdModalidadOfSilaboCollectionNewSilabo = em.merge(oldIdModalidadOfSilaboCollectionNewSilabo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = modalidad.getIdModalidad();
                if (findModalidad(id) == null) {
                    throw new NonexistentEntityException("The modalidad with id " + id + " no longer exists.");
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
            Modalidad modalidad;
            try {
                modalidad = em.getReference(Modalidad.class, id);
                modalidad.getIdModalidad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The modalidad with id " + id + " no longer exists.", enfe);
            }
            Collection<Silabo> silaboCollection = modalidad.getSilaboCollection();
            for (Silabo silaboCollectionSilabo : silaboCollection) {
                silaboCollectionSilabo.setIdModalidad(null);
                silaboCollectionSilabo = em.merge(silaboCollectionSilabo);
            }
            em.remove(modalidad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Modalidad> findModalidadEntities() {
        return findModalidadEntities(true, -1, -1);
    }

    public List<Modalidad> findModalidadEntities(int maxResults, int firstResult) {
        return findModalidadEntities(false, maxResults, firstResult);
    }

    private List<Modalidad> findModalidadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Modalidad.class));
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

    public Modalidad findModalidad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Modalidad.class, id);
        } finally {
            em.close();
        }
    }

    public int getModalidadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Modalidad> rt = cq.from(Modalidad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
