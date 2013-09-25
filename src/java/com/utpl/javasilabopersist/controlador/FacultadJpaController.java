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
import com.utpl.javasilabopersist.entidad.Escuela;
import com.utpl.javasilabopersist.entidad.Facultad;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class FacultadJpaController implements Serializable {

    public FacultadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Facultad facultad) {
        if (facultad.getEscuelaCollection() == null) {
            facultad.setEscuelaCollection(new ArrayList<Escuela>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Escuela> attachedEscuelaCollection = new ArrayList<Escuela>();
            for (Escuela escuelaCollectionEscuelaToAttach : facultad.getEscuelaCollection()) {
                escuelaCollectionEscuelaToAttach = em.getReference(escuelaCollectionEscuelaToAttach.getClass(), escuelaCollectionEscuelaToAttach.getIdEscuela());
                attachedEscuelaCollection.add(escuelaCollectionEscuelaToAttach);
            }
            facultad.setEscuelaCollection(attachedEscuelaCollection);
            em.persist(facultad);
            for (Escuela escuelaCollectionEscuela : facultad.getEscuelaCollection()) {
                Facultad oldIdFacultadOfEscuelaCollectionEscuela = escuelaCollectionEscuela.getIdFacultad();
                escuelaCollectionEscuela.setIdFacultad(facultad);
                escuelaCollectionEscuela = em.merge(escuelaCollectionEscuela);
                if (oldIdFacultadOfEscuelaCollectionEscuela != null) {
                    oldIdFacultadOfEscuelaCollectionEscuela.getEscuelaCollection().remove(escuelaCollectionEscuela);
                    oldIdFacultadOfEscuelaCollectionEscuela = em.merge(oldIdFacultadOfEscuelaCollectionEscuela);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Facultad facultad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facultad persistentFacultad = em.find(Facultad.class, facultad.getIdFacultad());
            Collection<Escuela> escuelaCollectionOld = persistentFacultad.getEscuelaCollection();
            Collection<Escuela> escuelaCollectionNew = facultad.getEscuelaCollection();
            List<String> illegalOrphanMessages = null;
            for (Escuela escuelaCollectionOldEscuela : escuelaCollectionOld) {
                if (!escuelaCollectionNew.contains(escuelaCollectionOldEscuela)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Escuela " + escuelaCollectionOldEscuela + " since its idFacultad field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Escuela> attachedEscuelaCollectionNew = new ArrayList<Escuela>();
            for (Escuela escuelaCollectionNewEscuelaToAttach : escuelaCollectionNew) {
                escuelaCollectionNewEscuelaToAttach = em.getReference(escuelaCollectionNewEscuelaToAttach.getClass(), escuelaCollectionNewEscuelaToAttach.getIdEscuela());
                attachedEscuelaCollectionNew.add(escuelaCollectionNewEscuelaToAttach);
            }
            escuelaCollectionNew = attachedEscuelaCollectionNew;
            facultad.setEscuelaCollection(escuelaCollectionNew);
            facultad = em.merge(facultad);
            for (Escuela escuelaCollectionNewEscuela : escuelaCollectionNew) {
                if (!escuelaCollectionOld.contains(escuelaCollectionNewEscuela)) {
                    Facultad oldIdFacultadOfEscuelaCollectionNewEscuela = escuelaCollectionNewEscuela.getIdFacultad();
                    escuelaCollectionNewEscuela.setIdFacultad(facultad);
                    escuelaCollectionNewEscuela = em.merge(escuelaCollectionNewEscuela);
                    if (oldIdFacultadOfEscuelaCollectionNewEscuela != null && !oldIdFacultadOfEscuelaCollectionNewEscuela.equals(facultad)) {
                        oldIdFacultadOfEscuelaCollectionNewEscuela.getEscuelaCollection().remove(escuelaCollectionNewEscuela);
                        oldIdFacultadOfEscuelaCollectionNewEscuela = em.merge(oldIdFacultadOfEscuelaCollectionNewEscuela);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = facultad.getIdFacultad();
                if (findFacultad(id) == null) {
                    throw new NonexistentEntityException("The facultad with id " + id + " no longer exists.");
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
            Facultad facultad;
            try {
                facultad = em.getReference(Facultad.class, id);
                facultad.getIdFacultad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facultad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Escuela> escuelaCollectionOrphanCheck = facultad.getEscuelaCollection();
            for (Escuela escuelaCollectionOrphanCheckEscuela : escuelaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Facultad (" + facultad + ") cannot be destroyed since the Escuela " + escuelaCollectionOrphanCheckEscuela + " in its escuelaCollection field has a non-nullable idFacultad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(facultad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Facultad> findFacultadEntities() {
        return findFacultadEntities(true, -1, -1);
    }

    public List<Facultad> findFacultadEntities(int maxResults, int firstResult) {
        return findFacultadEntities(false, maxResults, firstResult);
    }

    private List<Facultad> findFacultadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Facultad.class));
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

    public Facultad findFacultad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Facultad.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacultadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Facultad> rt = cq.from(Facultad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
