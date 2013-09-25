/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.controlador;

import com.utpl.javasilabopersist.controlador.exceptions.NonexistentEntityException;
import com.utpl.javasilabopersist.entidad.Carrera;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.utpl.javasilabopersist.entidad.Escuela;
import com.utpl.javasilabopersist.entidad.Malla;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class CarreraJpaController implements Serializable {

    public CarreraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Carrera carrera) {
        if (carrera.getMallaCollection() == null) {
            carrera.setMallaCollection(new ArrayList<Malla>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Escuela idEscuela = carrera.getIdEscuela();
            if (idEscuela != null) {
                idEscuela = em.getReference(idEscuela.getClass(), idEscuela.getIdEscuela());
                carrera.setIdEscuela(idEscuela);
            }
            Collection<Malla> attachedMallaCollection = new ArrayList<Malla>();
            for (Malla mallaCollectionMallaToAttach : carrera.getMallaCollection()) {
                mallaCollectionMallaToAttach = em.getReference(mallaCollectionMallaToAttach.getClass(), mallaCollectionMallaToAttach.getIdMalla());
                attachedMallaCollection.add(mallaCollectionMallaToAttach);
            }
            carrera.setMallaCollection(attachedMallaCollection);
            em.persist(carrera);
            if (idEscuela != null) {
                idEscuela.getCarreraCollection().add(carrera);
                idEscuela = em.merge(idEscuela);
            }
            for (Malla mallaCollectionMalla : carrera.getMallaCollection()) {
                Carrera oldIdCarreraOfMallaCollectionMalla = mallaCollectionMalla.getIdCarrera();
                mallaCollectionMalla.setIdCarrera(carrera);
                mallaCollectionMalla = em.merge(mallaCollectionMalla);
                if (oldIdCarreraOfMallaCollectionMalla != null) {
                    oldIdCarreraOfMallaCollectionMalla.getMallaCollection().remove(mallaCollectionMalla);
                    oldIdCarreraOfMallaCollectionMalla = em.merge(oldIdCarreraOfMallaCollectionMalla);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Carrera carrera) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carrera persistentCarrera = em.find(Carrera.class, carrera.getIdCarrera());
            Escuela idEscuelaOld = persistentCarrera.getIdEscuela();
            Escuela idEscuelaNew = carrera.getIdEscuela();
            Collection<Malla> mallaCollectionOld = persistentCarrera.getMallaCollection();
            Collection<Malla> mallaCollectionNew = carrera.getMallaCollection();
            if (idEscuelaNew != null) {
                idEscuelaNew = em.getReference(idEscuelaNew.getClass(), idEscuelaNew.getIdEscuela());
                carrera.setIdEscuela(idEscuelaNew);
            }
            Collection<Malla> attachedMallaCollectionNew = new ArrayList<Malla>();
            for (Malla mallaCollectionNewMallaToAttach : mallaCollectionNew) {
                mallaCollectionNewMallaToAttach = em.getReference(mallaCollectionNewMallaToAttach.getClass(), mallaCollectionNewMallaToAttach.getIdMalla());
                attachedMallaCollectionNew.add(mallaCollectionNewMallaToAttach);
            }
            mallaCollectionNew = attachedMallaCollectionNew;
            carrera.setMallaCollection(mallaCollectionNew);
            carrera = em.merge(carrera);
            if (idEscuelaOld != null && !idEscuelaOld.equals(idEscuelaNew)) {
                idEscuelaOld.getCarreraCollection().remove(carrera);
                idEscuelaOld = em.merge(idEscuelaOld);
            }
            if (idEscuelaNew != null && !idEscuelaNew.equals(idEscuelaOld)) {
                idEscuelaNew.getCarreraCollection().add(carrera);
                idEscuelaNew = em.merge(idEscuelaNew);
            }
            for (Malla mallaCollectionOldMalla : mallaCollectionOld) {
                if (!mallaCollectionNew.contains(mallaCollectionOldMalla)) {
                    mallaCollectionOldMalla.setIdCarrera(null);
                    mallaCollectionOldMalla = em.merge(mallaCollectionOldMalla);
                }
            }
            for (Malla mallaCollectionNewMalla : mallaCollectionNew) {
                if (!mallaCollectionOld.contains(mallaCollectionNewMalla)) {
                    Carrera oldIdCarreraOfMallaCollectionNewMalla = mallaCollectionNewMalla.getIdCarrera();
                    mallaCollectionNewMalla.setIdCarrera(carrera);
                    mallaCollectionNewMalla = em.merge(mallaCollectionNewMalla);
                    if (oldIdCarreraOfMallaCollectionNewMalla != null && !oldIdCarreraOfMallaCollectionNewMalla.equals(carrera)) {
                        oldIdCarreraOfMallaCollectionNewMalla.getMallaCollection().remove(mallaCollectionNewMalla);
                        oldIdCarreraOfMallaCollectionNewMalla = em.merge(oldIdCarreraOfMallaCollectionNewMalla);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = carrera.getIdCarrera();
                if (findCarrera(id) == null) {
                    throw new NonexistentEntityException("The carrera with id " + id + " no longer exists.");
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
            Carrera carrera;
            try {
                carrera = em.getReference(Carrera.class, id);
                carrera.getIdCarrera();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carrera with id " + id + " no longer exists.", enfe);
            }
            Escuela idEscuela = carrera.getIdEscuela();
            if (idEscuela != null) {
                idEscuela.getCarreraCollection().remove(carrera);
                idEscuela = em.merge(idEscuela);
            }
            Collection<Malla> mallaCollection = carrera.getMallaCollection();
            for (Malla mallaCollectionMalla : mallaCollection) {
                mallaCollectionMalla.setIdCarrera(null);
                mallaCollectionMalla = em.merge(mallaCollectionMalla);
            }
            em.remove(carrera);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Carrera> findCarreraEntities() {
        return findCarreraEntities(true, -1, -1);
    }

    public List<Carrera> findCarreraEntities(int maxResults, int firstResult) {
        return findCarreraEntities(false, maxResults, firstResult);
    }

    private List<Carrera> findCarreraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Carrera.class));
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

    public Carrera findCarrera(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Carrera.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarreraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Carrera> rt = cq.from(Carrera.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
