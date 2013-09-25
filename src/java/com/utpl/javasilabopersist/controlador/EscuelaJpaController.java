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
import com.utpl.javasilabopersist.entidad.Facultad;
import com.utpl.javasilabopersist.entidad.Carrera;
import com.utpl.javasilabopersist.entidad.Escuela;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class EscuelaJpaController implements Serializable {

    public EscuelaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Escuela escuela) {
        if (escuela.getCarreraCollection() == null) {
            escuela.setCarreraCollection(new ArrayList<Carrera>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facultad idFacultad = escuela.getIdFacultad();
            if (idFacultad != null) {
                idFacultad = em.getReference(idFacultad.getClass(), idFacultad.getIdFacultad());
                escuela.setIdFacultad(idFacultad);
            }
            Collection<Carrera> attachedCarreraCollection = new ArrayList<Carrera>();
            for (Carrera carreraCollectionCarreraToAttach : escuela.getCarreraCollection()) {
                carreraCollectionCarreraToAttach = em.getReference(carreraCollectionCarreraToAttach.getClass(), carreraCollectionCarreraToAttach.getIdCarrera());
                attachedCarreraCollection.add(carreraCollectionCarreraToAttach);
            }
            escuela.setCarreraCollection(attachedCarreraCollection);
            em.persist(escuela);
            if (idFacultad != null) {
                idFacultad.getEscuelaCollection().add(escuela);
                idFacultad = em.merge(idFacultad);
            }
            for (Carrera carreraCollectionCarrera : escuela.getCarreraCollection()) {
                Escuela oldIdEscuelaOfCarreraCollectionCarrera = carreraCollectionCarrera.getIdEscuela();
                carreraCollectionCarrera.setIdEscuela(escuela);
                carreraCollectionCarrera = em.merge(carreraCollectionCarrera);
                if (oldIdEscuelaOfCarreraCollectionCarrera != null) {
                    oldIdEscuelaOfCarreraCollectionCarrera.getCarreraCollection().remove(carreraCollectionCarrera);
                    oldIdEscuelaOfCarreraCollectionCarrera = em.merge(oldIdEscuelaOfCarreraCollectionCarrera);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Escuela escuela) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Escuela persistentEscuela = em.find(Escuela.class, escuela.getIdEscuela());
            Facultad idFacultadOld = persistentEscuela.getIdFacultad();
            Facultad idFacultadNew = escuela.getIdFacultad();
            Collection<Carrera> carreraCollectionOld = persistentEscuela.getCarreraCollection();
            Collection<Carrera> carreraCollectionNew = escuela.getCarreraCollection();
            List<String> illegalOrphanMessages = null;
            for (Carrera carreraCollectionOldCarrera : carreraCollectionOld) {
                if (!carreraCollectionNew.contains(carreraCollectionOldCarrera)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Carrera " + carreraCollectionOldCarrera + " since its idEscuela field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idFacultadNew != null) {
                idFacultadNew = em.getReference(idFacultadNew.getClass(), idFacultadNew.getIdFacultad());
                escuela.setIdFacultad(idFacultadNew);
            }
            Collection<Carrera> attachedCarreraCollectionNew = new ArrayList<Carrera>();
            for (Carrera carreraCollectionNewCarreraToAttach : carreraCollectionNew) {
                carreraCollectionNewCarreraToAttach = em.getReference(carreraCollectionNewCarreraToAttach.getClass(), carreraCollectionNewCarreraToAttach.getIdCarrera());
                attachedCarreraCollectionNew.add(carreraCollectionNewCarreraToAttach);
            }
            carreraCollectionNew = attachedCarreraCollectionNew;
            escuela.setCarreraCollection(carreraCollectionNew);
            escuela = em.merge(escuela);
            if (idFacultadOld != null && !idFacultadOld.equals(idFacultadNew)) {
                idFacultadOld.getEscuelaCollection().remove(escuela);
                idFacultadOld = em.merge(idFacultadOld);
            }
            if (idFacultadNew != null && !idFacultadNew.equals(idFacultadOld)) {
                idFacultadNew.getEscuelaCollection().add(escuela);
                idFacultadNew = em.merge(idFacultadNew);
            }
            for (Carrera carreraCollectionNewCarrera : carreraCollectionNew) {
                if (!carreraCollectionOld.contains(carreraCollectionNewCarrera)) {
                    Escuela oldIdEscuelaOfCarreraCollectionNewCarrera = carreraCollectionNewCarrera.getIdEscuela();
                    carreraCollectionNewCarrera.setIdEscuela(escuela);
                    carreraCollectionNewCarrera = em.merge(carreraCollectionNewCarrera);
                    if (oldIdEscuelaOfCarreraCollectionNewCarrera != null && !oldIdEscuelaOfCarreraCollectionNewCarrera.equals(escuela)) {
                        oldIdEscuelaOfCarreraCollectionNewCarrera.getCarreraCollection().remove(carreraCollectionNewCarrera);
                        oldIdEscuelaOfCarreraCollectionNewCarrera = em.merge(oldIdEscuelaOfCarreraCollectionNewCarrera);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = escuela.getIdEscuela();
                if (findEscuela(id) == null) {
                    throw new NonexistentEntityException("The escuela with id " + id + " no longer exists.");
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
            Escuela escuela;
            try {
                escuela = em.getReference(Escuela.class, id);
                escuela.getIdEscuela();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The escuela with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Carrera> carreraCollectionOrphanCheck = escuela.getCarreraCollection();
            for (Carrera carreraCollectionOrphanCheckCarrera : carreraCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Escuela (" + escuela + ") cannot be destroyed since the Carrera " + carreraCollectionOrphanCheckCarrera + " in its carreraCollection field has a non-nullable idEscuela field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Facultad idFacultad = escuela.getIdFacultad();
            if (idFacultad != null) {
                idFacultad.getEscuelaCollection().remove(escuela);
                idFacultad = em.merge(idFacultad);
            }
            em.remove(escuela);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Escuela> findEscuelaEntities() {
        return findEscuelaEntities(true, -1, -1);
    }

    public List<Escuela> findEscuelaEntities(int maxResults, int firstResult) {
        return findEscuelaEntities(false, maxResults, firstResult);
    }

    private List<Escuela> findEscuelaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Escuela.class));
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

    public Escuela findEscuela(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Escuela.class, id);
        } finally {
            em.close();
        }
    }

    public int getEscuelaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Escuela> rt = cq.from(Escuela.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
