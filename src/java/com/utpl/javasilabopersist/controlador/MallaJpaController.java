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
import com.utpl.javasilabopersist.entidad.VersionMalla;
import com.utpl.javasilabopersist.entidad.Carrera;
import com.utpl.javasilabopersist.entidad.Malla;
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
public class MallaJpaController implements Serializable {

    public MallaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Malla malla) {
        if (malla.getMateriaMallaCollection() == null) {
            malla.setMateriaMallaCollection(new ArrayList<MateriaMalla>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VersionMalla idVersionMalla = malla.getIdVersionMalla();
            if (idVersionMalla != null) {
                idVersionMalla = em.getReference(idVersionMalla.getClass(), idVersionMalla.getIdVersionMalla());
                malla.setIdVersionMalla(idVersionMalla);
            }
            Carrera idCarrera = malla.getIdCarrera();
            if (idCarrera != null) {
                idCarrera = em.getReference(idCarrera.getClass(), idCarrera.getIdCarrera());
                malla.setIdCarrera(idCarrera);
            }
            Collection<MateriaMalla> attachedMateriaMallaCollection = new ArrayList<MateriaMalla>();
            for (MateriaMalla materiaMallaCollectionMateriaMallaToAttach : malla.getMateriaMallaCollection()) {
                materiaMallaCollectionMateriaMallaToAttach = em.getReference(materiaMallaCollectionMateriaMallaToAttach.getClass(), materiaMallaCollectionMateriaMallaToAttach.getIdMATERIAMALLA());
                attachedMateriaMallaCollection.add(materiaMallaCollectionMateriaMallaToAttach);
            }
            malla.setMateriaMallaCollection(attachedMateriaMallaCollection);
            em.persist(malla);
            if (idVersionMalla != null) {
                idVersionMalla.getMallaCollection().add(malla);
                idVersionMalla = em.merge(idVersionMalla);
            }
            if (idCarrera != null) {
                idCarrera.getMallaCollection().add(malla);
                idCarrera = em.merge(idCarrera);
            }
            for (MateriaMalla materiaMallaCollectionMateriaMalla : malla.getMateriaMallaCollection()) {
                Malla oldMallaIdOfMateriaMallaCollectionMateriaMalla = materiaMallaCollectionMateriaMalla.getMallaId();
                materiaMallaCollectionMateriaMalla.setMallaId(malla);
                materiaMallaCollectionMateriaMalla = em.merge(materiaMallaCollectionMateriaMalla);
                if (oldMallaIdOfMateriaMallaCollectionMateriaMalla != null) {
                    oldMallaIdOfMateriaMallaCollectionMateriaMalla.getMateriaMallaCollection().remove(materiaMallaCollectionMateriaMalla);
                    oldMallaIdOfMateriaMallaCollectionMateriaMalla = em.merge(oldMallaIdOfMateriaMallaCollectionMateriaMalla);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Malla malla) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Malla persistentMalla = em.find(Malla.class, malla.getIdMalla());
            VersionMalla idVersionMallaOld = persistentMalla.getIdVersionMalla();
            VersionMalla idVersionMallaNew = malla.getIdVersionMalla();
            Carrera idCarreraOld = persistentMalla.getIdCarrera();
            Carrera idCarreraNew = malla.getIdCarrera();
            Collection<MateriaMalla> materiaMallaCollectionOld = persistentMalla.getMateriaMallaCollection();
            Collection<MateriaMalla> materiaMallaCollectionNew = malla.getMateriaMallaCollection();
            List<String> illegalOrphanMessages = null;
            for (MateriaMalla materiaMallaCollectionOldMateriaMalla : materiaMallaCollectionOld) {
                if (!materiaMallaCollectionNew.contains(materiaMallaCollectionOldMateriaMalla)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MateriaMalla " + materiaMallaCollectionOldMateriaMalla + " since its mallaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idVersionMallaNew != null) {
                idVersionMallaNew = em.getReference(idVersionMallaNew.getClass(), idVersionMallaNew.getIdVersionMalla());
                malla.setIdVersionMalla(idVersionMallaNew);
            }
            if (idCarreraNew != null) {
                idCarreraNew = em.getReference(idCarreraNew.getClass(), idCarreraNew.getIdCarrera());
                malla.setIdCarrera(idCarreraNew);
            }
            Collection<MateriaMalla> attachedMateriaMallaCollectionNew = new ArrayList<MateriaMalla>();
            for (MateriaMalla materiaMallaCollectionNewMateriaMallaToAttach : materiaMallaCollectionNew) {
                materiaMallaCollectionNewMateriaMallaToAttach = em.getReference(materiaMallaCollectionNewMateriaMallaToAttach.getClass(), materiaMallaCollectionNewMateriaMallaToAttach.getIdMATERIAMALLA());
                attachedMateriaMallaCollectionNew.add(materiaMallaCollectionNewMateriaMallaToAttach);
            }
            materiaMallaCollectionNew = attachedMateriaMallaCollectionNew;
            malla.setMateriaMallaCollection(materiaMallaCollectionNew);
            malla = em.merge(malla);
            if (idVersionMallaOld != null && !idVersionMallaOld.equals(idVersionMallaNew)) {
                idVersionMallaOld.getMallaCollection().remove(malla);
                idVersionMallaOld = em.merge(idVersionMallaOld);
            }
            if (idVersionMallaNew != null && !idVersionMallaNew.equals(idVersionMallaOld)) {
                idVersionMallaNew.getMallaCollection().add(malla);
                idVersionMallaNew = em.merge(idVersionMallaNew);
            }
            if (idCarreraOld != null && !idCarreraOld.equals(idCarreraNew)) {
                idCarreraOld.getMallaCollection().remove(malla);
                idCarreraOld = em.merge(idCarreraOld);
            }
            if (idCarreraNew != null && !idCarreraNew.equals(idCarreraOld)) {
                idCarreraNew.getMallaCollection().add(malla);
                idCarreraNew = em.merge(idCarreraNew);
            }
            for (MateriaMalla materiaMallaCollectionNewMateriaMalla : materiaMallaCollectionNew) {
                if (!materiaMallaCollectionOld.contains(materiaMallaCollectionNewMateriaMalla)) {
                    Malla oldMallaIdOfMateriaMallaCollectionNewMateriaMalla = materiaMallaCollectionNewMateriaMalla.getMallaId();
                    materiaMallaCollectionNewMateriaMalla.setMallaId(malla);
                    materiaMallaCollectionNewMateriaMalla = em.merge(materiaMallaCollectionNewMateriaMalla);
                    if (oldMallaIdOfMateriaMallaCollectionNewMateriaMalla != null && !oldMallaIdOfMateriaMallaCollectionNewMateriaMalla.equals(malla)) {
                        oldMallaIdOfMateriaMallaCollectionNewMateriaMalla.getMateriaMallaCollection().remove(materiaMallaCollectionNewMateriaMalla);
                        oldMallaIdOfMateriaMallaCollectionNewMateriaMalla = em.merge(oldMallaIdOfMateriaMallaCollectionNewMateriaMalla);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = malla.getIdMalla();
                if (findMalla(id) == null) {
                    throw new NonexistentEntityException("The malla with id " + id + " no longer exists.");
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
            Malla malla;
            try {
                malla = em.getReference(Malla.class, id);
                malla.getIdMalla();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The malla with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<MateriaMalla> materiaMallaCollectionOrphanCheck = malla.getMateriaMallaCollection();
            for (MateriaMalla materiaMallaCollectionOrphanCheckMateriaMalla : materiaMallaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Malla (" + malla + ") cannot be destroyed since the MateriaMalla " + materiaMallaCollectionOrphanCheckMateriaMalla + " in its materiaMallaCollection field has a non-nullable mallaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            VersionMalla idVersionMalla = malla.getIdVersionMalla();
            if (idVersionMalla != null) {
                idVersionMalla.getMallaCollection().remove(malla);
                idVersionMalla = em.merge(idVersionMalla);
            }
            Carrera idCarrera = malla.getIdCarrera();
            if (idCarrera != null) {
                idCarrera.getMallaCollection().remove(malla);
                idCarrera = em.merge(idCarrera);
            }
            em.remove(malla);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Malla> findMallaEntities() {
        return findMallaEntities(true, -1, -1);
    }

    public List<Malla> findMallaEntities(int maxResults, int firstResult) {
        return findMallaEntities(false, maxResults, firstResult);
    }

    private List<Malla> findMallaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Malla.class));
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

    public Malla findMalla(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Malla.class, id);
        } finally {
            em.close();
        }
    }

    public int getMallaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Malla> rt = cq.from(Malla.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
