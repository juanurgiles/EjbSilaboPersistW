/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.controlador;

import com.utpl.javasilabopersist.controlador.exceptions.NonexistentEntityException;
import com.utpl.javasilabopersist.controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.utpl.javasilabopersist.entidad.Referencia;
import com.utpl.javasilabopersist.entidad.Silabo;
import com.utpl.javasilabopersist.entidad.SilaboReferencia;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class SilaboReferenciaJpaController implements Serializable {

    public SilaboReferenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SilaboReferencia silaboReferencia) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Referencia referenciaId = silaboReferencia.getReferenciaId();
            if (referenciaId != null) {
                referenciaId = em.getReference(referenciaId.getClass(), referenciaId.getIdReferencia());
                silaboReferencia.setReferenciaId(referenciaId);
            }
            Silabo silaboId = silaboReferencia.getSilaboId();
            if (silaboId != null) {
                silaboId = em.getReference(silaboId.getClass(), silaboId.getIdSilabo());
                silaboReferencia.setSilaboId(silaboId);
            }
            em.persist(silaboReferencia);
            if (referenciaId != null) {
                referenciaId.getSilaboReferenciaCollection().add(silaboReferencia);
                referenciaId = em.merge(referenciaId);
            }
            if (silaboId != null) {
                silaboId.getSilaboReferenciaCollection().add(silaboReferencia);
                silaboId = em.merge(silaboId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSilaboReferencia(silaboReferencia.getIdSilaboReferencia()) != null) {
                throw new PreexistingEntityException("SilaboReferencia " + silaboReferencia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SilaboReferencia silaboReferencia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SilaboReferencia persistentSilaboReferencia = em.find(SilaboReferencia.class, silaboReferencia.getIdSilaboReferencia());
            Referencia referenciaIdOld = persistentSilaboReferencia.getReferenciaId();
            Referencia referenciaIdNew = silaboReferencia.getReferenciaId();
            Silabo silaboIdOld = persistentSilaboReferencia.getSilaboId();
            Silabo silaboIdNew = silaboReferencia.getSilaboId();
            if (referenciaIdNew != null) {
                referenciaIdNew = em.getReference(referenciaIdNew.getClass(), referenciaIdNew.getIdReferencia());
                silaboReferencia.setReferenciaId(referenciaIdNew);
            }
            if (silaboIdNew != null) {
                silaboIdNew = em.getReference(silaboIdNew.getClass(), silaboIdNew.getIdSilabo());
                silaboReferencia.setSilaboId(silaboIdNew);
            }
            silaboReferencia = em.merge(silaboReferencia);
            if (referenciaIdOld != null && !referenciaIdOld.equals(referenciaIdNew)) {
                referenciaIdOld.getSilaboReferenciaCollection().remove(silaboReferencia);
                referenciaIdOld = em.merge(referenciaIdOld);
            }
            if (referenciaIdNew != null && !referenciaIdNew.equals(referenciaIdOld)) {
                referenciaIdNew.getSilaboReferenciaCollection().add(silaboReferencia);
                referenciaIdNew = em.merge(referenciaIdNew);
            }
            if (silaboIdOld != null && !silaboIdOld.equals(silaboIdNew)) {
                silaboIdOld.getSilaboReferenciaCollection().remove(silaboReferencia);
                silaboIdOld = em.merge(silaboIdOld);
            }
            if (silaboIdNew != null && !silaboIdNew.equals(silaboIdOld)) {
                silaboIdNew.getSilaboReferenciaCollection().add(silaboReferencia);
                silaboIdNew = em.merge(silaboIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = silaboReferencia.getIdSilaboReferencia();
                if (findSilaboReferencia(id) == null) {
                    throw new NonexistentEntityException("The silaboReferencia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SilaboReferencia silaboReferencia;
            try {
                silaboReferencia = em.getReference(SilaboReferencia.class, id);
                silaboReferencia.getIdSilaboReferencia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The silaboReferencia with id " + id + " no longer exists.", enfe);
            }
            Referencia referenciaId = silaboReferencia.getReferenciaId();
            if (referenciaId != null) {
                referenciaId.getSilaboReferenciaCollection().remove(silaboReferencia);
                referenciaId = em.merge(referenciaId);
            }
            Silabo silaboId = silaboReferencia.getSilaboId();
            if (silaboId != null) {
                silaboId.getSilaboReferenciaCollection().remove(silaboReferencia);
                silaboId = em.merge(silaboId);
            }
            em.remove(silaboReferencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SilaboReferencia> findSilaboReferenciaEntities() {
        return findSilaboReferenciaEntities(true, -1, -1);
    }

    public List<SilaboReferencia> findSilaboReferenciaEntities(int maxResults, int firstResult) {
        return findSilaboReferenciaEntities(false, maxResults, firstResult);
    }

    private List<SilaboReferencia> findSilaboReferenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SilaboReferencia.class));
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

    public SilaboReferencia findSilaboReferencia(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SilaboReferencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getSilaboReferenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SilaboReferencia> rt = cq.from(SilaboReferencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
