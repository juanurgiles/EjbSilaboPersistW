/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.controlador;

import com.utpl.javasilabopersist.controlador.exceptions.NonexistentEntityException;
import com.utpl.javasilabopersist.entidad.Correquisitos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.utpl.javasilabopersist.entidad.Materia;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class CorrequisitosJpaController implements Serializable {

    public CorrequisitosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Correquisitos correquisitos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia materiaidMateria = correquisitos.getMateriaidMateria();
            if (materiaidMateria != null) {
                materiaidMateria = em.getReference(materiaidMateria.getClass(), materiaidMateria.getIdMateria());
                correquisitos.setMateriaidMateria(materiaidMateria);
            }
            Materia idMateriaMateria = correquisitos.getIdMateriaMateria();
            if (idMateriaMateria != null) {
                idMateriaMateria = em.getReference(idMateriaMateria.getClass(), idMateriaMateria.getIdMateria());
                correquisitos.setIdMateriaMateria(idMateriaMateria);
            }
            em.persist(correquisitos);
            if (materiaidMateria != null) {
                materiaidMateria.getCorrequisitosCollection().add(correquisitos);
                materiaidMateria = em.merge(materiaidMateria);
            }
            if (idMateriaMateria != null) {
                idMateriaMateria.getCorrequisitosCollection().add(correquisitos);
                idMateriaMateria = em.merge(idMateriaMateria);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Correquisitos correquisitos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Correquisitos persistentCorrequisitos = em.find(Correquisitos.class, correquisitos.getIdRequisito());
            Materia materiaidMateriaOld = persistentCorrequisitos.getMateriaidMateria();
            Materia materiaidMateriaNew = correquisitos.getMateriaidMateria();
            Materia idMateriaMateriaOld = persistentCorrequisitos.getIdMateriaMateria();
            Materia idMateriaMateriaNew = correquisitos.getIdMateriaMateria();
            if (materiaidMateriaNew != null) {
                materiaidMateriaNew = em.getReference(materiaidMateriaNew.getClass(), materiaidMateriaNew.getIdMateria());
                correquisitos.setMateriaidMateria(materiaidMateriaNew);
            }
            if (idMateriaMateriaNew != null) {
                idMateriaMateriaNew = em.getReference(idMateriaMateriaNew.getClass(), idMateriaMateriaNew.getIdMateria());
                correquisitos.setIdMateriaMateria(idMateriaMateriaNew);
            }
            correquisitos = em.merge(correquisitos);
            if (materiaidMateriaOld != null && !materiaidMateriaOld.equals(materiaidMateriaNew)) {
                materiaidMateriaOld.getCorrequisitosCollection().remove(correquisitos);
                materiaidMateriaOld = em.merge(materiaidMateriaOld);
            }
            if (materiaidMateriaNew != null && !materiaidMateriaNew.equals(materiaidMateriaOld)) {
                materiaidMateriaNew.getCorrequisitosCollection().add(correquisitos);
                materiaidMateriaNew = em.merge(materiaidMateriaNew);
            }
            if (idMateriaMateriaOld != null && !idMateriaMateriaOld.equals(idMateriaMateriaNew)) {
                idMateriaMateriaOld.getCorrequisitosCollection().remove(correquisitos);
                idMateriaMateriaOld = em.merge(idMateriaMateriaOld);
            }
            if (idMateriaMateriaNew != null && !idMateriaMateriaNew.equals(idMateriaMateriaOld)) {
                idMateriaMateriaNew.getCorrequisitosCollection().add(correquisitos);
                idMateriaMateriaNew = em.merge(idMateriaMateriaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = correquisitos.getIdRequisito();
                if (findCorrequisitos(id) == null) {
                    throw new NonexistentEntityException("The correquisitos with id " + id + " no longer exists.");
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
            Correquisitos correquisitos;
            try {
                correquisitos = em.getReference(Correquisitos.class, id);
                correquisitos.getIdRequisito();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The correquisitos with id " + id + " no longer exists.", enfe);
            }
            Materia materiaidMateria = correquisitos.getMateriaidMateria();
            if (materiaidMateria != null) {
                materiaidMateria.getCorrequisitosCollection().remove(correquisitos);
                materiaidMateria = em.merge(materiaidMateria);
            }
            Materia idMateriaMateria = correquisitos.getIdMateriaMateria();
            if (idMateriaMateria != null) {
                idMateriaMateria.getCorrequisitosCollection().remove(correquisitos);
                idMateriaMateria = em.merge(idMateriaMateria);
            }
            em.remove(correquisitos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Correquisitos> findCorrequisitosEntities() {
        return findCorrequisitosEntities(true, -1, -1);
    }

    public List<Correquisitos> findCorrequisitosEntities(int maxResults, int firstResult) {
        return findCorrequisitosEntities(false, maxResults, firstResult);
    }

    private List<Correquisitos> findCorrequisitosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Correquisitos.class));
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

    public Correquisitos findCorrequisitos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Correquisitos.class, id);
        } finally {
            em.close();
        }
    }

    public int getCorrequisitosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Correquisitos> rt = cq.from(Correquisitos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
