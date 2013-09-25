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
import com.utpl.javasilabopersist.entidad.Materia;
import com.utpl.javasilabopersist.entidad.TipoMateria;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class TipoMateriaJpaController implements Serializable {

    public TipoMateriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoMateria tipoMateria) {
        if (tipoMateria.getMateriaCollection() == null) {
            tipoMateria.setMateriaCollection(new ArrayList<Materia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Materia> attachedMateriaCollection = new ArrayList<Materia>();
            for (Materia materiaCollectionMateriaToAttach : tipoMateria.getMateriaCollection()) {
                materiaCollectionMateriaToAttach = em.getReference(materiaCollectionMateriaToAttach.getClass(), materiaCollectionMateriaToAttach.getIdMateria());
                attachedMateriaCollection.add(materiaCollectionMateriaToAttach);
            }
            tipoMateria.setMateriaCollection(attachedMateriaCollection);
            em.persist(tipoMateria);
            for (Materia materiaCollectionMateria : tipoMateria.getMateriaCollection()) {
                TipoMateria oldIdTipoMateriaOfMateriaCollectionMateria = materiaCollectionMateria.getIdTipoMateria();
                materiaCollectionMateria.setIdTipoMateria(tipoMateria);
                materiaCollectionMateria = em.merge(materiaCollectionMateria);
                if (oldIdTipoMateriaOfMateriaCollectionMateria != null) {
                    oldIdTipoMateriaOfMateriaCollectionMateria.getMateriaCollection().remove(materiaCollectionMateria);
                    oldIdTipoMateriaOfMateriaCollectionMateria = em.merge(oldIdTipoMateriaOfMateriaCollectionMateria);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoMateria tipoMateria) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoMateria persistentTipoMateria = em.find(TipoMateria.class, tipoMateria.getIdTipoMateria());
            Collection<Materia> materiaCollectionOld = persistentTipoMateria.getMateriaCollection();
            Collection<Materia> materiaCollectionNew = tipoMateria.getMateriaCollection();
            List<String> illegalOrphanMessages = null;
            for (Materia materiaCollectionOldMateria : materiaCollectionOld) {
                if (!materiaCollectionNew.contains(materiaCollectionOldMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Materia " + materiaCollectionOldMateria + " since its idTipoMateria field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Materia> attachedMateriaCollectionNew = new ArrayList<Materia>();
            for (Materia materiaCollectionNewMateriaToAttach : materiaCollectionNew) {
                materiaCollectionNewMateriaToAttach = em.getReference(materiaCollectionNewMateriaToAttach.getClass(), materiaCollectionNewMateriaToAttach.getIdMateria());
                attachedMateriaCollectionNew.add(materiaCollectionNewMateriaToAttach);
            }
            materiaCollectionNew = attachedMateriaCollectionNew;
            tipoMateria.setMateriaCollection(materiaCollectionNew);
            tipoMateria = em.merge(tipoMateria);
            for (Materia materiaCollectionNewMateria : materiaCollectionNew) {
                if (!materiaCollectionOld.contains(materiaCollectionNewMateria)) {
                    TipoMateria oldIdTipoMateriaOfMateriaCollectionNewMateria = materiaCollectionNewMateria.getIdTipoMateria();
                    materiaCollectionNewMateria.setIdTipoMateria(tipoMateria);
                    materiaCollectionNewMateria = em.merge(materiaCollectionNewMateria);
                    if (oldIdTipoMateriaOfMateriaCollectionNewMateria != null && !oldIdTipoMateriaOfMateriaCollectionNewMateria.equals(tipoMateria)) {
                        oldIdTipoMateriaOfMateriaCollectionNewMateria.getMateriaCollection().remove(materiaCollectionNewMateria);
                        oldIdTipoMateriaOfMateriaCollectionNewMateria = em.merge(oldIdTipoMateriaOfMateriaCollectionNewMateria);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoMateria.getIdTipoMateria();
                if (findTipoMateria(id) == null) {
                    throw new NonexistentEntityException("The tipoMateria with id " + id + " no longer exists.");
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
            TipoMateria tipoMateria;
            try {
                tipoMateria = em.getReference(TipoMateria.class, id);
                tipoMateria.getIdTipoMateria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoMateria with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Materia> materiaCollectionOrphanCheck = tipoMateria.getMateriaCollection();
            for (Materia materiaCollectionOrphanCheckMateria : materiaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoMateria (" + tipoMateria + ") cannot be destroyed since the Materia " + materiaCollectionOrphanCheckMateria + " in its materiaCollection field has a non-nullable idTipoMateria field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoMateria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoMateria> findTipoMateriaEntities() {
        return findTipoMateriaEntities(true, -1, -1);
    }

    public List<TipoMateria> findTipoMateriaEntities(int maxResults, int firstResult) {
        return findTipoMateriaEntities(false, maxResults, firstResult);
    }

    private List<TipoMateria> findTipoMateriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoMateria.class));
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

    public TipoMateria findTipoMateria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoMateria.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoMateriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoMateria> rt = cq.from(TipoMateria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
