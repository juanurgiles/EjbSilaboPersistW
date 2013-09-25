/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.controlador;

import com.utpl.javasilabopersist.controlador.exceptions.IllegalOrphanException;
import com.utpl.javasilabopersist.controlador.exceptions.NonexistentEntityException;
import com.utpl.javasilabopersist.entidad.Referencia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.utpl.javasilabopersist.entidad.TipoReferencia;
import com.utpl.javasilabopersist.entidad.SilaboReferencia;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class ReferenciaJpaController implements Serializable {

    public ReferenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Referencia referencia) {
        if (referencia.getSilaboReferenciaCollection() == null) {
            referencia.setSilaboReferenciaCollection(new ArrayList<SilaboReferencia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoReferencia idTipoReferencia = referencia.getIdTipoReferencia();
            if (idTipoReferencia != null) {
                idTipoReferencia = em.getReference(idTipoReferencia.getClass(), idTipoReferencia.getIdTipoReferencia());
                referencia.setIdTipoReferencia(idTipoReferencia);
            }
            Collection<SilaboReferencia> attachedSilaboReferenciaCollection = new ArrayList<SilaboReferencia>();
            for (SilaboReferencia silaboReferenciaCollectionSilaboReferenciaToAttach : referencia.getSilaboReferenciaCollection()) {
                silaboReferenciaCollectionSilaboReferenciaToAttach = em.getReference(silaboReferenciaCollectionSilaboReferenciaToAttach.getClass(), silaboReferenciaCollectionSilaboReferenciaToAttach.getIdSilaboReferencia());
                attachedSilaboReferenciaCollection.add(silaboReferenciaCollectionSilaboReferenciaToAttach);
            }
            referencia.setSilaboReferenciaCollection(attachedSilaboReferenciaCollection);
            em.persist(referencia);
            if (idTipoReferencia != null) {
                idTipoReferencia.getReferenciaCollection().add(referencia);
                idTipoReferencia = em.merge(idTipoReferencia);
            }
            for (SilaboReferencia silaboReferenciaCollectionSilaboReferencia : referencia.getSilaboReferenciaCollection()) {
                Referencia oldReferenciaIdOfSilaboReferenciaCollectionSilaboReferencia = silaboReferenciaCollectionSilaboReferencia.getReferenciaId();
                silaboReferenciaCollectionSilaboReferencia.setReferenciaId(referencia);
                silaboReferenciaCollectionSilaboReferencia = em.merge(silaboReferenciaCollectionSilaboReferencia);
                if (oldReferenciaIdOfSilaboReferenciaCollectionSilaboReferencia != null) {
                    oldReferenciaIdOfSilaboReferenciaCollectionSilaboReferencia.getSilaboReferenciaCollection().remove(silaboReferenciaCollectionSilaboReferencia);
                    oldReferenciaIdOfSilaboReferenciaCollectionSilaboReferencia = em.merge(oldReferenciaIdOfSilaboReferenciaCollectionSilaboReferencia);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Referencia referencia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Referencia persistentReferencia = em.find(Referencia.class, referencia.getIdReferencia());
            TipoReferencia idTipoReferenciaOld = persistentReferencia.getIdTipoReferencia();
            TipoReferencia idTipoReferenciaNew = referencia.getIdTipoReferencia();
            Collection<SilaboReferencia> silaboReferenciaCollectionOld = persistentReferencia.getSilaboReferenciaCollection();
            Collection<SilaboReferencia> silaboReferenciaCollectionNew = referencia.getSilaboReferenciaCollection();
            List<String> illegalOrphanMessages = null;
            for (SilaboReferencia silaboReferenciaCollectionOldSilaboReferencia : silaboReferenciaCollectionOld) {
                if (!silaboReferenciaCollectionNew.contains(silaboReferenciaCollectionOldSilaboReferencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SilaboReferencia " + silaboReferenciaCollectionOldSilaboReferencia + " since its referenciaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTipoReferenciaNew != null) {
                idTipoReferenciaNew = em.getReference(idTipoReferenciaNew.getClass(), idTipoReferenciaNew.getIdTipoReferencia());
                referencia.setIdTipoReferencia(idTipoReferenciaNew);
            }
            Collection<SilaboReferencia> attachedSilaboReferenciaCollectionNew = new ArrayList<SilaboReferencia>();
            for (SilaboReferencia silaboReferenciaCollectionNewSilaboReferenciaToAttach : silaboReferenciaCollectionNew) {
                silaboReferenciaCollectionNewSilaboReferenciaToAttach = em.getReference(silaboReferenciaCollectionNewSilaboReferenciaToAttach.getClass(), silaboReferenciaCollectionNewSilaboReferenciaToAttach.getIdSilaboReferencia());
                attachedSilaboReferenciaCollectionNew.add(silaboReferenciaCollectionNewSilaboReferenciaToAttach);
            }
            silaboReferenciaCollectionNew = attachedSilaboReferenciaCollectionNew;
            referencia.setSilaboReferenciaCollection(silaboReferenciaCollectionNew);
            referencia = em.merge(referencia);
            if (idTipoReferenciaOld != null && !idTipoReferenciaOld.equals(idTipoReferenciaNew)) {
                idTipoReferenciaOld.getReferenciaCollection().remove(referencia);
                idTipoReferenciaOld = em.merge(idTipoReferenciaOld);
            }
            if (idTipoReferenciaNew != null && !idTipoReferenciaNew.equals(idTipoReferenciaOld)) {
                idTipoReferenciaNew.getReferenciaCollection().add(referencia);
                idTipoReferenciaNew = em.merge(idTipoReferenciaNew);
            }
            for (SilaboReferencia silaboReferenciaCollectionNewSilaboReferencia : silaboReferenciaCollectionNew) {
                if (!silaboReferenciaCollectionOld.contains(silaboReferenciaCollectionNewSilaboReferencia)) {
                    Referencia oldReferenciaIdOfSilaboReferenciaCollectionNewSilaboReferencia = silaboReferenciaCollectionNewSilaboReferencia.getReferenciaId();
                    silaboReferenciaCollectionNewSilaboReferencia.setReferenciaId(referencia);
                    silaboReferenciaCollectionNewSilaboReferencia = em.merge(silaboReferenciaCollectionNewSilaboReferencia);
                    if (oldReferenciaIdOfSilaboReferenciaCollectionNewSilaboReferencia != null && !oldReferenciaIdOfSilaboReferenciaCollectionNewSilaboReferencia.equals(referencia)) {
                        oldReferenciaIdOfSilaboReferenciaCollectionNewSilaboReferencia.getSilaboReferenciaCollection().remove(silaboReferenciaCollectionNewSilaboReferencia);
                        oldReferenciaIdOfSilaboReferenciaCollectionNewSilaboReferencia = em.merge(oldReferenciaIdOfSilaboReferenciaCollectionNewSilaboReferencia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = referencia.getIdReferencia();
                if (findReferencia(id) == null) {
                    throw new NonexistentEntityException("The referencia with id " + id + " no longer exists.");
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
            Referencia referencia;
            try {
                referencia = em.getReference(Referencia.class, id);
                referencia.getIdReferencia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The referencia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<SilaboReferencia> silaboReferenciaCollectionOrphanCheck = referencia.getSilaboReferenciaCollection();
            for (SilaboReferencia silaboReferenciaCollectionOrphanCheckSilaboReferencia : silaboReferenciaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Referencia (" + referencia + ") cannot be destroyed since the SilaboReferencia " + silaboReferenciaCollectionOrphanCheckSilaboReferencia + " in its silaboReferenciaCollection field has a non-nullable referenciaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoReferencia idTipoReferencia = referencia.getIdTipoReferencia();
            if (idTipoReferencia != null) {
                idTipoReferencia.getReferenciaCollection().remove(referencia);
                idTipoReferencia = em.merge(idTipoReferencia);
            }
            em.remove(referencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Referencia> findReferenciaEntities() {
        return findReferenciaEntities(true, -1, -1);
    }

    public List<Referencia> findReferenciaEntities(int maxResults, int firstResult) {
        return findReferenciaEntities(false, maxResults, firstResult);
    }

    private List<Referencia> findReferenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Referencia.class));
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

    public Referencia findReferencia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Referencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getReferenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Referencia> rt = cq.from(Referencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
