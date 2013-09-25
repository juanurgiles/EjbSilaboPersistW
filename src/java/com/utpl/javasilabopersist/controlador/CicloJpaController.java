/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.controlador;

import com.utpl.javasilabopersist.controlador.exceptions.NonexistentEntityException;
import com.utpl.javasilabopersist.entidad.Ciclo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.utpl.javasilabopersist.entidad.MateriaMalla;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class CicloJpaController implements Serializable {

    public CicloJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ciclo ciclo) {
        if (ciclo.getMateriaMallaCollection() == null) {
            ciclo.setMateriaMallaCollection(new ArrayList<MateriaMalla>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<MateriaMalla> attachedMateriaMallaCollection = new ArrayList<MateriaMalla>();
            for (MateriaMalla materiaMallaCollectionMateriaMallaToAttach : ciclo.getMateriaMallaCollection()) {
                materiaMallaCollectionMateriaMallaToAttach = em.getReference(materiaMallaCollectionMateriaMallaToAttach.getClass(), materiaMallaCollectionMateriaMallaToAttach.getIdMATERIAMALLA());
                attachedMateriaMallaCollection.add(materiaMallaCollectionMateriaMallaToAttach);
            }
            ciclo.setMateriaMallaCollection(attachedMateriaMallaCollection);
            em.persist(ciclo);
            for (MateriaMalla materiaMallaCollectionMateriaMalla : ciclo.getMateriaMallaCollection()) {
                Ciclo oldCicloIdCicloOfMateriaMallaCollectionMateriaMalla = materiaMallaCollectionMateriaMalla.getCicloIdCiclo();
                materiaMallaCollectionMateriaMalla.setCicloIdCiclo(ciclo);
                materiaMallaCollectionMateriaMalla = em.merge(materiaMallaCollectionMateriaMalla);
                if (oldCicloIdCicloOfMateriaMallaCollectionMateriaMalla != null) {
                    oldCicloIdCicloOfMateriaMallaCollectionMateriaMalla.getMateriaMallaCollection().remove(materiaMallaCollectionMateriaMalla);
                    oldCicloIdCicloOfMateriaMallaCollectionMateriaMalla = em.merge(oldCicloIdCicloOfMateriaMallaCollectionMateriaMalla);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ciclo ciclo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciclo persistentCiclo = em.find(Ciclo.class, ciclo.getIdCiclo());
            Collection<MateriaMalla> materiaMallaCollectionOld = persistentCiclo.getMateriaMallaCollection();
            Collection<MateriaMalla> materiaMallaCollectionNew = ciclo.getMateriaMallaCollection();
            Collection<MateriaMalla> attachedMateriaMallaCollectionNew = new ArrayList<MateriaMalla>();
            for (MateriaMalla materiaMallaCollectionNewMateriaMallaToAttach : materiaMallaCollectionNew) {
                materiaMallaCollectionNewMateriaMallaToAttach = em.getReference(materiaMallaCollectionNewMateriaMallaToAttach.getClass(), materiaMallaCollectionNewMateriaMallaToAttach.getIdMATERIAMALLA());
                attachedMateriaMallaCollectionNew.add(materiaMallaCollectionNewMateriaMallaToAttach);
            }
            materiaMallaCollectionNew = attachedMateriaMallaCollectionNew;
            ciclo.setMateriaMallaCollection(materiaMallaCollectionNew);
            ciclo = em.merge(ciclo);
            for (MateriaMalla materiaMallaCollectionOldMateriaMalla : materiaMallaCollectionOld) {
                if (!materiaMallaCollectionNew.contains(materiaMallaCollectionOldMateriaMalla)) {
                    materiaMallaCollectionOldMateriaMalla.setCicloIdCiclo(null);
                    materiaMallaCollectionOldMateriaMalla = em.merge(materiaMallaCollectionOldMateriaMalla);
                }
            }
            for (MateriaMalla materiaMallaCollectionNewMateriaMalla : materiaMallaCollectionNew) {
                if (!materiaMallaCollectionOld.contains(materiaMallaCollectionNewMateriaMalla)) {
                    Ciclo oldCicloIdCicloOfMateriaMallaCollectionNewMateriaMalla = materiaMallaCollectionNewMateriaMalla.getCicloIdCiclo();
                    materiaMallaCollectionNewMateriaMalla.setCicloIdCiclo(ciclo);
                    materiaMallaCollectionNewMateriaMalla = em.merge(materiaMallaCollectionNewMateriaMalla);
                    if (oldCicloIdCicloOfMateriaMallaCollectionNewMateriaMalla != null && !oldCicloIdCicloOfMateriaMallaCollectionNewMateriaMalla.equals(ciclo)) {
                        oldCicloIdCicloOfMateriaMallaCollectionNewMateriaMalla.getMateriaMallaCollection().remove(materiaMallaCollectionNewMateriaMalla);
                        oldCicloIdCicloOfMateriaMallaCollectionNewMateriaMalla = em.merge(oldCicloIdCicloOfMateriaMallaCollectionNewMateriaMalla);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ciclo.getIdCiclo();
                if (findCiclo(id) == null) {
                    throw new NonexistentEntityException("The ciclo with id " + id + " no longer exists.");
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
            Ciclo ciclo;
            try {
                ciclo = em.getReference(Ciclo.class, id);
                ciclo.getIdCiclo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ciclo with id " + id + " no longer exists.", enfe);
            }
            Collection<MateriaMalla> materiaMallaCollection = ciclo.getMateriaMallaCollection();
            for (MateriaMalla materiaMallaCollectionMateriaMalla : materiaMallaCollection) {
                materiaMallaCollectionMateriaMalla.setCicloIdCiclo(null);
                materiaMallaCollectionMateriaMalla = em.merge(materiaMallaCollectionMateriaMalla);
            }
            em.remove(ciclo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ciclo> findCicloEntities() {
        return findCicloEntities(true, -1, -1);
    }

    public List<Ciclo> findCicloEntities(int maxResults, int firstResult) {
        return findCicloEntities(false, maxResults, firstResult);
    }

    private List<Ciclo> findCicloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ciclo.class));
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

    public Ciclo findCiclo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ciclo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCicloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ciclo> rt = cq.from(Ciclo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
