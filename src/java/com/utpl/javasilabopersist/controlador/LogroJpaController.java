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
import com.utpl.javasilabopersist.entidad.Contenido;
import com.utpl.javasilabopersist.entidad.Logro;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class LogroJpaController implements Serializable {

    public LogroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Logro logro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contenido idContenido = logro.getIdContenido();
            if (idContenido != null) {
                idContenido = em.getReference(idContenido.getClass(), idContenido.getIdContenido());
                logro.setIdContenido(idContenido);
            }
            em.persist(logro);
            if (idContenido != null) {
                idContenido.getLogroCollection().add(logro);
                idContenido = em.merge(idContenido);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Logro logro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Logro persistentLogro = em.find(Logro.class, logro.getIdLogro());
            Contenido idContenidoOld = persistentLogro.getIdContenido();
            Contenido idContenidoNew = logro.getIdContenido();
            if (idContenidoNew != null) {
                idContenidoNew = em.getReference(idContenidoNew.getClass(), idContenidoNew.getIdContenido());
                logro.setIdContenido(idContenidoNew);
            }
            logro = em.merge(logro);
            if (idContenidoOld != null && !idContenidoOld.equals(idContenidoNew)) {
                idContenidoOld.getLogroCollection().remove(logro);
                idContenidoOld = em.merge(idContenidoOld);
            }
            if (idContenidoNew != null && !idContenidoNew.equals(idContenidoOld)) {
                idContenidoNew.getLogroCollection().add(logro);
                idContenidoNew = em.merge(idContenidoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = logro.getIdLogro();
                if (findLogro(id) == null) {
                    throw new NonexistentEntityException("The logro with id " + id + " no longer exists.");
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
            Logro logro;
            try {
                logro = em.getReference(Logro.class, id);
                logro.getIdLogro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The logro with id " + id + " no longer exists.", enfe);
            }
            Contenido idContenido = logro.getIdContenido();
            if (idContenido != null) {
                idContenido.getLogroCollection().remove(logro);
                idContenido = em.merge(idContenido);
            }
            em.remove(logro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Logro> findLogroEntities() {
        return findLogroEntities(true, -1, -1);
    }

    public List<Logro> findLogroEntities(int maxResults, int firstResult) {
        return findLogroEntities(false, maxResults, firstResult);
    }

    private List<Logro> findLogroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Logro.class));
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

    public Logro findLogro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Logro.class, id);
        } finally {
            em.close();
        }
    }

    public int getLogroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Logro> rt = cq.from(Logro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
