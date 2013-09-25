/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.controlador;

import com.utpl.javasilabopersist.controlador.exceptions.NonexistentEntityException;
import com.utpl.javasilabopersist.controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.utpl.javasilabopersist.entidad.Ciclo;
import com.utpl.javasilabopersist.entidad.Malla;
import com.utpl.javasilabopersist.entidad.Materia;
import com.utpl.javasilabopersist.entidad.MateriaMalla;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class MateriaMallaJpaController implements Serializable {

    public MateriaMallaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MateriaMalla materiaMalla) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciclo cicloIdCiclo = materiaMalla.getCicloIdCiclo();
            if (cicloIdCiclo != null) {
                cicloIdCiclo = em.getReference(cicloIdCiclo.getClass(), cicloIdCiclo.getIdCiclo());
                materiaMalla.setCicloIdCiclo(cicloIdCiclo);
            }
            Malla mallaId = materiaMalla.getMallaId();
            if (mallaId != null) {
                mallaId = em.getReference(mallaId.getClass(), mallaId.getIdMalla());
                materiaMalla.setMallaId(mallaId);
            }
            Materia materiaId = materiaMalla.getMateriaId();
            if (materiaId != null) {
                materiaId = em.getReference(materiaId.getClass(), materiaId.getIdMateria());
                materiaMalla.setMateriaId(materiaId);
            }
            em.persist(materiaMalla);
            if (cicloIdCiclo != null) {
                cicloIdCiclo.getMateriaMallaCollection().add(materiaMalla);
                cicloIdCiclo = em.merge(cicloIdCiclo);
            }
            if (mallaId != null) {
                mallaId.getMateriaMallaCollection().add(materiaMalla);
                mallaId = em.merge(mallaId);
            }
            if (materiaId != null) {
                materiaId.getMateriaMallaCollection().add(materiaMalla);
                materiaId = em.merge(materiaId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMateriaMalla(materiaMalla.getIdMATERIAMALLA()) != null) {
                throw new PreexistingEntityException("MateriaMalla " + materiaMalla + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MateriaMalla materiaMalla) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MateriaMalla persistentMateriaMalla = em.find(MateriaMalla.class, materiaMalla.getIdMATERIAMALLA());
            Ciclo cicloIdCicloOld = persistentMateriaMalla.getCicloIdCiclo();
            Ciclo cicloIdCicloNew = materiaMalla.getCicloIdCiclo();
            Malla mallaIdOld = persistentMateriaMalla.getMallaId();
            Malla mallaIdNew = materiaMalla.getMallaId();
            Materia materiaIdOld = persistentMateriaMalla.getMateriaId();
            Materia materiaIdNew = materiaMalla.getMateriaId();
            if (cicloIdCicloNew != null) {
                cicloIdCicloNew = em.getReference(cicloIdCicloNew.getClass(), cicloIdCicloNew.getIdCiclo());
                materiaMalla.setCicloIdCiclo(cicloIdCicloNew);
            }
            if (mallaIdNew != null) {
                mallaIdNew = em.getReference(mallaIdNew.getClass(), mallaIdNew.getIdMalla());
                materiaMalla.setMallaId(mallaIdNew);
            }
            if (materiaIdNew != null) {
                materiaIdNew = em.getReference(materiaIdNew.getClass(), materiaIdNew.getIdMateria());
                materiaMalla.setMateriaId(materiaIdNew);
            }
            materiaMalla = em.merge(materiaMalla);
            if (cicloIdCicloOld != null && !cicloIdCicloOld.equals(cicloIdCicloNew)) {
                cicloIdCicloOld.getMateriaMallaCollection().remove(materiaMalla);
                cicloIdCicloOld = em.merge(cicloIdCicloOld);
            }
            if (cicloIdCicloNew != null && !cicloIdCicloNew.equals(cicloIdCicloOld)) {
                cicloIdCicloNew.getMateriaMallaCollection().add(materiaMalla);
                cicloIdCicloNew = em.merge(cicloIdCicloNew);
            }
            if (mallaIdOld != null && !mallaIdOld.equals(mallaIdNew)) {
                mallaIdOld.getMateriaMallaCollection().remove(materiaMalla);
                mallaIdOld = em.merge(mallaIdOld);
            }
            if (mallaIdNew != null && !mallaIdNew.equals(mallaIdOld)) {
                mallaIdNew.getMateriaMallaCollection().add(materiaMalla);
                mallaIdNew = em.merge(mallaIdNew);
            }
            if (materiaIdOld != null && !materiaIdOld.equals(materiaIdNew)) {
                materiaIdOld.getMateriaMallaCollection().remove(materiaMalla);
                materiaIdOld = em.merge(materiaIdOld);
            }
            if (materiaIdNew != null && !materiaIdNew.equals(materiaIdOld)) {
                materiaIdNew.getMateriaMallaCollection().add(materiaMalla);
                materiaIdNew = em.merge(materiaIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = materiaMalla.getIdMATERIAMALLA();
                if (findMateriaMalla(id) == null) {
                    throw new NonexistentEntityException("The materiaMalla with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MateriaMalla materiaMalla;
            try {
                materiaMalla = em.getReference(MateriaMalla.class, id);
                materiaMalla.getIdMATERIAMALLA();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materiaMalla with id " + id + " no longer exists.", enfe);
            }
            Ciclo cicloIdCiclo = materiaMalla.getCicloIdCiclo();
            if (cicloIdCiclo != null) {
                cicloIdCiclo.getMateriaMallaCollection().remove(materiaMalla);
                cicloIdCiclo = em.merge(cicloIdCiclo);
            }
            Malla mallaId = materiaMalla.getMallaId();
            if (mallaId != null) {
                mallaId.getMateriaMallaCollection().remove(materiaMalla);
                mallaId = em.merge(mallaId);
            }
            Materia materiaId = materiaMalla.getMateriaId();
            if (materiaId != null) {
                materiaId.getMateriaMallaCollection().remove(materiaMalla);
                materiaId = em.merge(materiaId);
            }
            em.remove(materiaMalla);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MateriaMalla> findMateriaMallaEntities() {
        return findMateriaMallaEntities(true, -1, -1);
    }

    public List<MateriaMalla> findMateriaMallaEntities(int maxResults, int firstResult) {
        return findMateriaMallaEntities(false, maxResults, firstResult);
    }

    private List<MateriaMalla> findMateriaMallaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MateriaMalla.class));
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

    public MateriaMalla findMateriaMalla(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MateriaMalla.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaMallaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MateriaMalla> rt = cq.from(MateriaMalla.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
