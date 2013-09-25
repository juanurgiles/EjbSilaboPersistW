/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.controlador;

import com.utpl.javasilabopersist.controlador.exceptions.NonexistentEntityException;
import com.utpl.javasilabopersist.entidad.Contenido;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.utpl.javasilabopersist.entidad.Materia;
import com.utpl.javasilabopersist.entidad.Logro;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class ContenidoJpaController implements Serializable {

    public ContenidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contenido contenido) {
        if (contenido.getLogroCollection() == null) {
            contenido.setLogroCollection(new ArrayList<Logro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia idMateria = contenido.getIdMateria();
            if (idMateria != null) {
                idMateria = em.getReference(idMateria.getClass(), idMateria.getIdMateria());
                contenido.setIdMateria(idMateria);
            }
            Collection<Logro> attachedLogroCollection = new ArrayList<Logro>();
            for (Logro logroCollectionLogroToAttach : contenido.getLogroCollection()) {
                logroCollectionLogroToAttach = em.getReference(logroCollectionLogroToAttach.getClass(), logroCollectionLogroToAttach.getIdLogro());
                attachedLogroCollection.add(logroCollectionLogroToAttach);
            }
            contenido.setLogroCollection(attachedLogroCollection);
            em.persist(contenido);
            if (idMateria != null) {
                idMateria.getContenidoCollection().add(contenido);
                idMateria = em.merge(idMateria);
            }
            for (Logro logroCollectionLogro : contenido.getLogroCollection()) {
                Contenido oldIdContenidoOfLogroCollectionLogro = logroCollectionLogro.getIdContenido();
                logroCollectionLogro.setIdContenido(contenido);
                logroCollectionLogro = em.merge(logroCollectionLogro);
                if (oldIdContenidoOfLogroCollectionLogro != null) {
                    oldIdContenidoOfLogroCollectionLogro.getLogroCollection().remove(logroCollectionLogro);
                    oldIdContenidoOfLogroCollectionLogro = em.merge(oldIdContenidoOfLogroCollectionLogro);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contenido contenido) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contenido persistentContenido = em.find(Contenido.class, contenido.getIdContenido());
            Materia idMateriaOld = persistentContenido.getIdMateria();
            Materia idMateriaNew = contenido.getIdMateria();
            Collection<Logro> logroCollectionOld = persistentContenido.getLogroCollection();
            Collection<Logro> logroCollectionNew = contenido.getLogroCollection();
            if (idMateriaNew != null) {
                idMateriaNew = em.getReference(idMateriaNew.getClass(), idMateriaNew.getIdMateria());
                contenido.setIdMateria(idMateriaNew);
            }
            Collection<Logro> attachedLogroCollectionNew = new ArrayList<Logro>();
            for (Logro logroCollectionNewLogroToAttach : logroCollectionNew) {
                logroCollectionNewLogroToAttach = em.getReference(logroCollectionNewLogroToAttach.getClass(), logroCollectionNewLogroToAttach.getIdLogro());
                attachedLogroCollectionNew.add(logroCollectionNewLogroToAttach);
            }
            logroCollectionNew = attachedLogroCollectionNew;
            contenido.setLogroCollection(logroCollectionNew);
            contenido = em.merge(contenido);
            if (idMateriaOld != null && !idMateriaOld.equals(idMateriaNew)) {
                idMateriaOld.getContenidoCollection().remove(contenido);
                idMateriaOld = em.merge(idMateriaOld);
            }
            if (idMateriaNew != null && !idMateriaNew.equals(idMateriaOld)) {
                idMateriaNew.getContenidoCollection().add(contenido);
                idMateriaNew = em.merge(idMateriaNew);
            }
            for (Logro logroCollectionOldLogro : logroCollectionOld) {
                if (!logroCollectionNew.contains(logroCollectionOldLogro)) {
                    logroCollectionOldLogro.setIdContenido(null);
                    logroCollectionOldLogro = em.merge(logroCollectionOldLogro);
                }
            }
            for (Logro logroCollectionNewLogro : logroCollectionNew) {
                if (!logroCollectionOld.contains(logroCollectionNewLogro)) {
                    Contenido oldIdContenidoOfLogroCollectionNewLogro = logroCollectionNewLogro.getIdContenido();
                    logroCollectionNewLogro.setIdContenido(contenido);
                    logroCollectionNewLogro = em.merge(logroCollectionNewLogro);
                    if (oldIdContenidoOfLogroCollectionNewLogro != null && !oldIdContenidoOfLogroCollectionNewLogro.equals(contenido)) {
                        oldIdContenidoOfLogroCollectionNewLogro.getLogroCollection().remove(logroCollectionNewLogro);
                        oldIdContenidoOfLogroCollectionNewLogro = em.merge(oldIdContenidoOfLogroCollectionNewLogro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = contenido.getIdContenido();
                if (findContenido(id) == null) {
                    throw new NonexistentEntityException("The contenido with id " + id + " no longer exists.");
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
            Contenido contenido;
            try {
                contenido = em.getReference(Contenido.class, id);
                contenido.getIdContenido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contenido with id " + id + " no longer exists.", enfe);
            }
            Materia idMateria = contenido.getIdMateria();
            if (idMateria != null) {
                idMateria.getContenidoCollection().remove(contenido);
                idMateria = em.merge(idMateria);
            }
            Collection<Logro> logroCollection = contenido.getLogroCollection();
            for (Logro logroCollectionLogro : logroCollection) {
                logroCollectionLogro.setIdContenido(null);
                logroCollectionLogro = em.merge(logroCollectionLogro);
            }
            em.remove(contenido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Contenido> findContenidoEntities() {
        return findContenidoEntities(true, -1, -1);
    }

    public List<Contenido> findContenidoEntities(int maxResults, int firstResult) {
        return findContenidoEntities(false, maxResults, firstResult);
    }

    private List<Contenido> findContenidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contenido.class));
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

    public Contenido findContenido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contenido.class, id);
        } finally {
            em.close();
        }
    }

    public int getContenidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contenido> rt = cq.from(Contenido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
