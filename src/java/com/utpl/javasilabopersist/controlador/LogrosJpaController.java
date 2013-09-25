/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.controlador;

import com.utpl.javasilabopersist.controlador.exceptions.NonexistentEntityException;
import com.utpl.javasilabopersist.entidad.Logros;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.utpl.javasilabopersist.entidad.Silabo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class LogrosJpaController implements Serializable {

    public LogrosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Logros logros) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Silabo idSilabo = logros.getIdSilabo();
            if (idSilabo != null) {
                idSilabo = em.getReference(idSilabo.getClass(), idSilabo.getIdSilabo());
                logros.setIdSilabo(idSilabo);
            }
            em.persist(logros);
            if (idSilabo != null) {
                idSilabo.getLogrosCollection().add(logros);
                idSilabo = em.merge(idSilabo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Logros logros) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Logros persistentLogros = em.find(Logros.class, logros.getIdLogros());
            Silabo idSilaboOld = persistentLogros.getIdSilabo();
            Silabo idSilaboNew = logros.getIdSilabo();
            if (idSilaboNew != null) {
                idSilaboNew = em.getReference(idSilaboNew.getClass(), idSilaboNew.getIdSilabo());
                logros.setIdSilabo(idSilaboNew);
            }
            logros = em.merge(logros);
            if (idSilaboOld != null && !idSilaboOld.equals(idSilaboNew)) {
                idSilaboOld.getLogrosCollection().remove(logros);
                idSilaboOld = em.merge(idSilaboOld);
            }
            if (idSilaboNew != null && !idSilaboNew.equals(idSilaboOld)) {
                idSilaboNew.getLogrosCollection().add(logros);
                idSilaboNew = em.merge(idSilaboNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = logros.getIdLogros();
                if (findLogros(id) == null) {
                    throw new NonexistentEntityException("The logros with id " + id + " no longer exists.");
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
            Logros logros;
            try {
                logros = em.getReference(Logros.class, id);
                logros.getIdLogros();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The logros with id " + id + " no longer exists.", enfe);
            }
            Silabo idSilabo = logros.getIdSilabo();
            if (idSilabo != null) {
                idSilabo.getLogrosCollection().remove(logros);
                idSilabo = em.merge(idSilabo);
            }
            em.remove(logros);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Logros> findLogrosEntities() {
        return findLogrosEntities(true, -1, -1);
    }

    public List<Logros> findLogrosEntities(int maxResults, int firstResult) {
        return findLogrosEntities(false, maxResults, firstResult);
    }

    private List<Logros> findLogrosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Logros.class));
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

    public Logros findLogros(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Logros.class, id);
        } finally {
            em.close();
        }
    }

    public int getLogrosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Logros> rt = cq.from(Logros.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
