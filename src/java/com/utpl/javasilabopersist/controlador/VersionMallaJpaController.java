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
import com.utpl.javasilabopersist.entidad.Malla;
import com.utpl.javasilabopersist.entidad.VersionMalla;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class VersionMallaJpaController implements Serializable {

    public VersionMallaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VersionMalla versionMalla) {
        if (versionMalla.getMallaCollection() == null) {
            versionMalla.setMallaCollection(new ArrayList<Malla>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Malla> attachedMallaCollection = new ArrayList<Malla>();
            for (Malla mallaCollectionMallaToAttach : versionMalla.getMallaCollection()) {
                mallaCollectionMallaToAttach = em.getReference(mallaCollectionMallaToAttach.getClass(), mallaCollectionMallaToAttach.getIdMalla());
                attachedMallaCollection.add(mallaCollectionMallaToAttach);
            }
            versionMalla.setMallaCollection(attachedMallaCollection);
            em.persist(versionMalla);
            for (Malla mallaCollectionMalla : versionMalla.getMallaCollection()) {
                VersionMalla oldIdVersionMallaOfMallaCollectionMalla = mallaCollectionMalla.getIdVersionMalla();
                mallaCollectionMalla.setIdVersionMalla(versionMalla);
                mallaCollectionMalla = em.merge(mallaCollectionMalla);
                if (oldIdVersionMallaOfMallaCollectionMalla != null) {
                    oldIdVersionMallaOfMallaCollectionMalla.getMallaCollection().remove(mallaCollectionMalla);
                    oldIdVersionMallaOfMallaCollectionMalla = em.merge(oldIdVersionMallaOfMallaCollectionMalla);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VersionMalla versionMalla) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VersionMalla persistentVersionMalla = em.find(VersionMalla.class, versionMalla.getIdVersionMalla());
            Collection<Malla> mallaCollectionOld = persistentVersionMalla.getMallaCollection();
            Collection<Malla> mallaCollectionNew = versionMalla.getMallaCollection();
            Collection<Malla> attachedMallaCollectionNew = new ArrayList<Malla>();
            for (Malla mallaCollectionNewMallaToAttach : mallaCollectionNew) {
                mallaCollectionNewMallaToAttach = em.getReference(mallaCollectionNewMallaToAttach.getClass(), mallaCollectionNewMallaToAttach.getIdMalla());
                attachedMallaCollectionNew.add(mallaCollectionNewMallaToAttach);
            }
            mallaCollectionNew = attachedMallaCollectionNew;
            versionMalla.setMallaCollection(mallaCollectionNew);
            versionMalla = em.merge(versionMalla);
            for (Malla mallaCollectionOldMalla : mallaCollectionOld) {
                if (!mallaCollectionNew.contains(mallaCollectionOldMalla)) {
                    mallaCollectionOldMalla.setIdVersionMalla(null);
                    mallaCollectionOldMalla = em.merge(mallaCollectionOldMalla);
                }
            }
            for (Malla mallaCollectionNewMalla : mallaCollectionNew) {
                if (!mallaCollectionOld.contains(mallaCollectionNewMalla)) {
                    VersionMalla oldIdVersionMallaOfMallaCollectionNewMalla = mallaCollectionNewMalla.getIdVersionMalla();
                    mallaCollectionNewMalla.setIdVersionMalla(versionMalla);
                    mallaCollectionNewMalla = em.merge(mallaCollectionNewMalla);
                    if (oldIdVersionMallaOfMallaCollectionNewMalla != null && !oldIdVersionMallaOfMallaCollectionNewMalla.equals(versionMalla)) {
                        oldIdVersionMallaOfMallaCollectionNewMalla.getMallaCollection().remove(mallaCollectionNewMalla);
                        oldIdVersionMallaOfMallaCollectionNewMalla = em.merge(oldIdVersionMallaOfMallaCollectionNewMalla);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = versionMalla.getIdVersionMalla();
                if (findVersionMalla(id) == null) {
                    throw new NonexistentEntityException("The versionMalla with id " + id + " no longer exists.");
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
            VersionMalla versionMalla;
            try {
                versionMalla = em.getReference(VersionMalla.class, id);
                versionMalla.getIdVersionMalla();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The versionMalla with id " + id + " no longer exists.", enfe);
            }
            Collection<Malla> mallaCollection = versionMalla.getMallaCollection();
            for (Malla mallaCollectionMalla : mallaCollection) {
                mallaCollectionMalla.setIdVersionMalla(null);
                mallaCollectionMalla = em.merge(mallaCollectionMalla);
            }
            em.remove(versionMalla);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VersionMalla> findVersionMallaEntities() {
        return findVersionMallaEntities(true, -1, -1);
    }

    public List<VersionMalla> findVersionMallaEntities(int maxResults, int firstResult) {
        return findVersionMallaEntities(false, maxResults, firstResult);
    }

    private List<VersionMalla> findVersionMallaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VersionMalla.class));
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

    public VersionMalla findVersionMalla(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VersionMalla.class, id);
        } finally {
            em.close();
        }
    }

    public int getVersionMallaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VersionMalla> rt = cq.from(VersionMalla.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
