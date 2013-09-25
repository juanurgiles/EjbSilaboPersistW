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
import com.utpl.javasilabopersist.entidad.Materia;
import com.utpl.javasilabopersist.entidad.Requisitos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class RequisitosJpaController implements Serializable {

    public RequisitosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Requisitos requisitos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia materiaidMateria = requisitos.getMateriaidMateria();
            if (materiaidMateria != null) {
                materiaidMateria = em.getReference(materiaidMateria.getClass(), materiaidMateria.getIdMateria());
                requisitos.setMateriaidMateria(materiaidMateria);
            }
            Materia idMateriaMateria = requisitos.getIdMateriaMateria();
            if (idMateriaMateria != null) {
                idMateriaMateria = em.getReference(idMateriaMateria.getClass(), idMateriaMateria.getIdMateria());
                requisitos.setIdMateriaMateria(idMateriaMateria);
            }
            em.persist(requisitos);
            if (materiaidMateria != null) {
                materiaidMateria.getRequisitosCollection().add(requisitos);
                materiaidMateria = em.merge(materiaidMateria);
            }
            if (idMateriaMateria != null) {
                idMateriaMateria.getRequisitosCollection().add(requisitos);
                idMateriaMateria = em.merge(idMateriaMateria);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Requisitos requisitos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Requisitos persistentRequisitos = em.find(Requisitos.class, requisitos.getIdRequisito());
            Materia materiaidMateriaOld = persistentRequisitos.getMateriaidMateria();
            Materia materiaidMateriaNew = requisitos.getMateriaidMateria();
            Materia idMateriaMateriaOld = persistentRequisitos.getIdMateriaMateria();
            Materia idMateriaMateriaNew = requisitos.getIdMateriaMateria();
            if (materiaidMateriaNew != null) {
                materiaidMateriaNew = em.getReference(materiaidMateriaNew.getClass(), materiaidMateriaNew.getIdMateria());
                requisitos.setMateriaidMateria(materiaidMateriaNew);
            }
            if (idMateriaMateriaNew != null) {
                idMateriaMateriaNew = em.getReference(idMateriaMateriaNew.getClass(), idMateriaMateriaNew.getIdMateria());
                requisitos.setIdMateriaMateria(idMateriaMateriaNew);
            }
            requisitos = em.merge(requisitos);
            if (materiaidMateriaOld != null && !materiaidMateriaOld.equals(materiaidMateriaNew)) {
                materiaidMateriaOld.getRequisitosCollection().remove(requisitos);
                materiaidMateriaOld = em.merge(materiaidMateriaOld);
            }
            if (materiaidMateriaNew != null && !materiaidMateriaNew.equals(materiaidMateriaOld)) {
                materiaidMateriaNew.getRequisitosCollection().add(requisitos);
                materiaidMateriaNew = em.merge(materiaidMateriaNew);
            }
            if (idMateriaMateriaOld != null && !idMateriaMateriaOld.equals(idMateriaMateriaNew)) {
                idMateriaMateriaOld.getRequisitosCollection().remove(requisitos);
                idMateriaMateriaOld = em.merge(idMateriaMateriaOld);
            }
            if (idMateriaMateriaNew != null && !idMateriaMateriaNew.equals(idMateriaMateriaOld)) {
                idMateriaMateriaNew.getRequisitosCollection().add(requisitos);
                idMateriaMateriaNew = em.merge(idMateriaMateriaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = requisitos.getIdRequisito();
                if (findRequisitos(id) == null) {
                    throw new NonexistentEntityException("The requisitos with id " + id + " no longer exists.");
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
            Requisitos requisitos;
            try {
                requisitos = em.getReference(Requisitos.class, id);
                requisitos.getIdRequisito();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The requisitos with id " + id + " no longer exists.", enfe);
            }
            Materia materiaidMateria = requisitos.getMateriaidMateria();
            if (materiaidMateria != null) {
                materiaidMateria.getRequisitosCollection().remove(requisitos);
                materiaidMateria = em.merge(materiaidMateria);
            }
            Materia idMateriaMateria = requisitos.getIdMateriaMateria();
            if (idMateriaMateria != null) {
                idMateriaMateria.getRequisitosCollection().remove(requisitos);
                idMateriaMateria = em.merge(idMateriaMateria);
            }
            em.remove(requisitos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Requisitos> findRequisitosEntities() {
        return findRequisitosEntities(true, -1, -1);
    }

    public List<Requisitos> findRequisitosEntities(int maxResults, int firstResult) {
        return findRequisitosEntities(false, maxResults, firstResult);
    }

    private List<Requisitos> findRequisitosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Requisitos.class));
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

    public Requisitos findRequisitos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Requisitos.class, id);
        } finally {
            em.close();
        }
    }

    public int getRequisitosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Requisitos> rt = cq.from(Requisitos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
