/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.controlador;

import com.utpl.javasilabopersist.controlador.exceptions.NonexistentEntityException;
import com.utpl.javasilabopersist.entidad.Materiat;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.utpl.javasilabopersist.entidad.Profesort;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class MateriatJpaController implements Serializable {

    public MateriatJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Materiat materiat) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesort profesortId = materiat.getProfesortId();
            if (profesortId != null) {
                profesortId = em.getReference(profesortId.getClass(), profesortId.getId());
                materiat.setProfesortId(profesortId);
            }
            em.persist(materiat);
            if (profesortId != null) {
                profesortId.getMateriatCollection().add(materiat);
                profesortId = em.merge(profesortId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Materiat materiat) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materiat persistentMateriat = em.find(Materiat.class, materiat.getId());
            Profesort profesortIdOld = persistentMateriat.getProfesortId();
            Profesort profesortIdNew = materiat.getProfesortId();
            if (profesortIdNew != null) {
                profesortIdNew = em.getReference(profesortIdNew.getClass(), profesortIdNew.getId());
                materiat.setProfesortId(profesortIdNew);
            }
            materiat = em.merge(materiat);
            if (profesortIdOld != null && !profesortIdOld.equals(profesortIdNew)) {
                profesortIdOld.getMateriatCollection().remove(materiat);
                profesortIdOld = em.merge(profesortIdOld);
            }
            if (profesortIdNew != null && !profesortIdNew.equals(profesortIdOld)) {
                profesortIdNew.getMateriatCollection().add(materiat);
                profesortIdNew = em.merge(profesortIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = materiat.getId();
                if (findMateriat(id) == null) {
                    throw new NonexistentEntityException("The materiat with id " + id + " no longer exists.");
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
            Materiat materiat;
            try {
                materiat = em.getReference(Materiat.class, id);
                materiat.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materiat with id " + id + " no longer exists.", enfe);
            }
            Profesort profesortId = materiat.getProfesortId();
            if (profesortId != null) {
                profesortId.getMateriatCollection().remove(materiat);
                profesortId = em.merge(profesortId);
            }
            em.remove(materiat);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Materiat> findMateriatEntities() {
        return findMateriatEntities(true, -1, -1);
    }

    public List<Materiat> findMateriatEntities(int maxResults, int firstResult) {
        return findMateriatEntities(false, maxResults, firstResult);
    }

    private List<Materiat> findMateriatEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Materiat.class));
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

    public Materiat findMateriat(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Materiat.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriatCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Materiat> rt = cq.from(Materiat.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
