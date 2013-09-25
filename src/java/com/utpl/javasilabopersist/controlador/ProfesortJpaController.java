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
import com.utpl.javasilabopersist.entidad.TipoT;
import com.utpl.javasilabopersist.entidad.Materiat;
import com.utpl.javasilabopersist.entidad.Profesort;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class ProfesortJpaController implements Serializable {

    public ProfesortJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profesort profesort) {
        if (profesort.getMateriatCollection() == null) {
            profesort.setMateriatCollection(new ArrayList<Materiat>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoT tipoTid = profesort.getTipoTid();
            if (tipoTid != null) {
                tipoTid = em.getReference(tipoTid.getClass(), tipoTid.getId());
                profesort.setTipoTid(tipoTid);
            }
            Collection<Materiat> attachedMateriatCollection = new ArrayList<Materiat>();
            for (Materiat materiatCollectionMateriatToAttach : profesort.getMateriatCollection()) {
                materiatCollectionMateriatToAttach = em.getReference(materiatCollectionMateriatToAttach.getClass(), materiatCollectionMateriatToAttach.getId());
                attachedMateriatCollection.add(materiatCollectionMateriatToAttach);
            }
            profesort.setMateriatCollection(attachedMateriatCollection);
            em.persist(profesort);
            if (tipoTid != null) {
                tipoTid.getProfesortCollection().add(profesort);
                tipoTid = em.merge(tipoTid);
            }
            for (Materiat materiatCollectionMateriat : profesort.getMateriatCollection()) {
                Profesort oldProfesortIdOfMateriatCollectionMateriat = materiatCollectionMateriat.getProfesortId();
                materiatCollectionMateriat.setProfesortId(profesort);
                materiatCollectionMateriat = em.merge(materiatCollectionMateriat);
                if (oldProfesortIdOfMateriatCollectionMateriat != null) {
                    oldProfesortIdOfMateriatCollectionMateriat.getMateriatCollection().remove(materiatCollectionMateriat);
                    oldProfesortIdOfMateriatCollectionMateriat = em.merge(oldProfesortIdOfMateriatCollectionMateriat);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profesort profesort) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesort persistentProfesort = em.find(Profesort.class, profesort.getId());
            TipoT tipoTidOld = persistentProfesort.getTipoTid();
            TipoT tipoTidNew = profesort.getTipoTid();
            Collection<Materiat> materiatCollectionOld = persistentProfesort.getMateriatCollection();
            Collection<Materiat> materiatCollectionNew = profesort.getMateriatCollection();
            List<String> illegalOrphanMessages = null;
            for (Materiat materiatCollectionOldMateriat : materiatCollectionOld) {
                if (!materiatCollectionNew.contains(materiatCollectionOldMateriat)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Materiat " + materiatCollectionOldMateriat + " since its profesortId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipoTidNew != null) {
                tipoTidNew = em.getReference(tipoTidNew.getClass(), tipoTidNew.getId());
                profesort.setTipoTid(tipoTidNew);
            }
            Collection<Materiat> attachedMateriatCollectionNew = new ArrayList<Materiat>();
            for (Materiat materiatCollectionNewMateriatToAttach : materiatCollectionNew) {
                materiatCollectionNewMateriatToAttach = em.getReference(materiatCollectionNewMateriatToAttach.getClass(), materiatCollectionNewMateriatToAttach.getId());
                attachedMateriatCollectionNew.add(materiatCollectionNewMateriatToAttach);
            }
            materiatCollectionNew = attachedMateriatCollectionNew;
            profesort.setMateriatCollection(materiatCollectionNew);
            profesort = em.merge(profesort);
            if (tipoTidOld != null && !tipoTidOld.equals(tipoTidNew)) {
                tipoTidOld.getProfesortCollection().remove(profesort);
                tipoTidOld = em.merge(tipoTidOld);
            }
            if (tipoTidNew != null && !tipoTidNew.equals(tipoTidOld)) {
                tipoTidNew.getProfesortCollection().add(profesort);
                tipoTidNew = em.merge(tipoTidNew);
            }
            for (Materiat materiatCollectionNewMateriat : materiatCollectionNew) {
                if (!materiatCollectionOld.contains(materiatCollectionNewMateriat)) {
                    Profesort oldProfesortIdOfMateriatCollectionNewMateriat = materiatCollectionNewMateriat.getProfesortId();
                    materiatCollectionNewMateriat.setProfesortId(profesort);
                    materiatCollectionNewMateriat = em.merge(materiatCollectionNewMateriat);
                    if (oldProfesortIdOfMateriatCollectionNewMateriat != null && !oldProfesortIdOfMateriatCollectionNewMateriat.equals(profesort)) {
                        oldProfesortIdOfMateriatCollectionNewMateriat.getMateriatCollection().remove(materiatCollectionNewMateriat);
                        oldProfesortIdOfMateriatCollectionNewMateriat = em.merge(oldProfesortIdOfMateriatCollectionNewMateriat);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = profesort.getId();
                if (findProfesort(id) == null) {
                    throw new NonexistentEntityException("The profesort with id " + id + " no longer exists.");
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
            Profesort profesort;
            try {
                profesort = em.getReference(Profesort.class, id);
                profesort.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profesort with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Materiat> materiatCollectionOrphanCheck = profesort.getMateriatCollection();
            for (Materiat materiatCollectionOrphanCheckMateriat : materiatCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Profesort (" + profesort + ") cannot be destroyed since the Materiat " + materiatCollectionOrphanCheckMateriat + " in its materiatCollection field has a non-nullable profesortId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoT tipoTid = profesort.getTipoTid();
            if (tipoTid != null) {
                tipoTid.getProfesortCollection().remove(profesort);
                tipoTid = em.merge(tipoTid);
            }
            em.remove(profesort);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profesort> findProfesortEntities() {
        return findProfesortEntities(true, -1, -1);
    }

    public List<Profesort> findProfesortEntities(int maxResults, int firstResult) {
        return findProfesortEntities(false, maxResults, firstResult);
    }

    private List<Profesort> findProfesortEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profesort.class));
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

    public Profesort findProfesort(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profesort.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfesortCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profesort> rt = cq.from(Profesort.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
