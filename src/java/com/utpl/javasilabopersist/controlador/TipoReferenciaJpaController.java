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
import com.utpl.javasilabopersist.entidad.Referencia;
import com.utpl.javasilabopersist.entidad.TipoReferencia;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class TipoReferenciaJpaController implements Serializable {

    public TipoReferenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoReferencia tipoReferencia) {
        if (tipoReferencia.getReferenciaCollection() == null) {
            tipoReferencia.setReferenciaCollection(new ArrayList<Referencia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Referencia> attachedReferenciaCollection = new ArrayList<Referencia>();
            for (Referencia referenciaCollectionReferenciaToAttach : tipoReferencia.getReferenciaCollection()) {
                referenciaCollectionReferenciaToAttach = em.getReference(referenciaCollectionReferenciaToAttach.getClass(), referenciaCollectionReferenciaToAttach.getIdReferencia());
                attachedReferenciaCollection.add(referenciaCollectionReferenciaToAttach);
            }
            tipoReferencia.setReferenciaCollection(attachedReferenciaCollection);
            em.persist(tipoReferencia);
            for (Referencia referenciaCollectionReferencia : tipoReferencia.getReferenciaCollection()) {
                TipoReferencia oldIdTipoReferenciaOfReferenciaCollectionReferencia = referenciaCollectionReferencia.getIdTipoReferencia();
                referenciaCollectionReferencia.setIdTipoReferencia(tipoReferencia);
                referenciaCollectionReferencia = em.merge(referenciaCollectionReferencia);
                if (oldIdTipoReferenciaOfReferenciaCollectionReferencia != null) {
                    oldIdTipoReferenciaOfReferenciaCollectionReferencia.getReferenciaCollection().remove(referenciaCollectionReferencia);
                    oldIdTipoReferenciaOfReferenciaCollectionReferencia = em.merge(oldIdTipoReferenciaOfReferenciaCollectionReferencia);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoReferencia tipoReferencia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoReferencia persistentTipoReferencia = em.find(TipoReferencia.class, tipoReferencia.getIdTipoReferencia());
            Collection<Referencia> referenciaCollectionOld = persistentTipoReferencia.getReferenciaCollection();
            Collection<Referencia> referenciaCollectionNew = tipoReferencia.getReferenciaCollection();
            Collection<Referencia> attachedReferenciaCollectionNew = new ArrayList<Referencia>();
            for (Referencia referenciaCollectionNewReferenciaToAttach : referenciaCollectionNew) {
                referenciaCollectionNewReferenciaToAttach = em.getReference(referenciaCollectionNewReferenciaToAttach.getClass(), referenciaCollectionNewReferenciaToAttach.getIdReferencia());
                attachedReferenciaCollectionNew.add(referenciaCollectionNewReferenciaToAttach);
            }
            referenciaCollectionNew = attachedReferenciaCollectionNew;
            tipoReferencia.setReferenciaCollection(referenciaCollectionNew);
            tipoReferencia = em.merge(tipoReferencia);
            for (Referencia referenciaCollectionOldReferencia : referenciaCollectionOld) {
                if (!referenciaCollectionNew.contains(referenciaCollectionOldReferencia)) {
                    referenciaCollectionOldReferencia.setIdTipoReferencia(null);
                    referenciaCollectionOldReferencia = em.merge(referenciaCollectionOldReferencia);
                }
            }
            for (Referencia referenciaCollectionNewReferencia : referenciaCollectionNew) {
                if (!referenciaCollectionOld.contains(referenciaCollectionNewReferencia)) {
                    TipoReferencia oldIdTipoReferenciaOfReferenciaCollectionNewReferencia = referenciaCollectionNewReferencia.getIdTipoReferencia();
                    referenciaCollectionNewReferencia.setIdTipoReferencia(tipoReferencia);
                    referenciaCollectionNewReferencia = em.merge(referenciaCollectionNewReferencia);
                    if (oldIdTipoReferenciaOfReferenciaCollectionNewReferencia != null && !oldIdTipoReferenciaOfReferenciaCollectionNewReferencia.equals(tipoReferencia)) {
                        oldIdTipoReferenciaOfReferenciaCollectionNewReferencia.getReferenciaCollection().remove(referenciaCollectionNewReferencia);
                        oldIdTipoReferenciaOfReferenciaCollectionNewReferencia = em.merge(oldIdTipoReferenciaOfReferenciaCollectionNewReferencia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoReferencia.getIdTipoReferencia();
                if (findTipoReferencia(id) == null) {
                    throw new NonexistentEntityException("The tipoReferencia with id " + id + " no longer exists.");
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
            TipoReferencia tipoReferencia;
            try {
                tipoReferencia = em.getReference(TipoReferencia.class, id);
                tipoReferencia.getIdTipoReferencia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoReferencia with id " + id + " no longer exists.", enfe);
            }
            Collection<Referencia> referenciaCollection = tipoReferencia.getReferenciaCollection();
            for (Referencia referenciaCollectionReferencia : referenciaCollection) {
                referenciaCollectionReferencia.setIdTipoReferencia(null);
                referenciaCollectionReferencia = em.merge(referenciaCollectionReferencia);
            }
            em.remove(tipoReferencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoReferencia> findTipoReferenciaEntities() {
        return findTipoReferenciaEntities(true, -1, -1);
    }

    public List<TipoReferencia> findTipoReferenciaEntities(int maxResults, int firstResult) {
        return findTipoReferenciaEntities(false, maxResults, firstResult);
    }

    private List<TipoReferencia> findTipoReferenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoReferencia.class));
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

    public TipoReferencia findTipoReferencia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoReferencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoReferenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoReferencia> rt = cq.from(TipoReferencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
