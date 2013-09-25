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
import com.utpl.javasilabopersist.entidad.Periodo;
import com.utpl.javasilabopersist.entidad.Modalidad;
import com.utpl.javasilabopersist.entidad.Materia;
import java.util.ArrayList;
import java.util.Collection;
import com.utpl.javasilabopersist.entidad.Logros;
import com.utpl.javasilabopersist.entidad.Silabo;
import com.utpl.javasilabopersist.entidad.SilaboReferencia;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author root
 */
public class SilaboJpaController implements Serializable {

    public SilaboJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Silabo silabo) {
        if (silabo.getMateriaCollection() == null) {
            silabo.setMateriaCollection(new ArrayList<Materia>());
        }
        if (silabo.getLogrosCollection() == null) {
            silabo.setLogrosCollection(new ArrayList<Logros>());
        }
        if (silabo.getSilaboReferenciaCollection() == null) {
            silabo.setSilaboReferenciaCollection(new ArrayList<SilaboReferencia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Periodo idPeriodo = silabo.getIdPeriodo();
            if (idPeriodo != null) {
                idPeriodo = em.getReference(idPeriodo.getClass(), idPeriodo.getIdPeriodo());
                silabo.setIdPeriodo(idPeriodo);
            }
            Modalidad idModalidad = silabo.getIdModalidad();
            if (idModalidad != null) {
                idModalidad = em.getReference(idModalidad.getClass(), idModalidad.getIdModalidad());
                silabo.setIdModalidad(idModalidad);
            }
            Collection<Materia> attachedMateriaCollection = new ArrayList<Materia>();
            for (Materia materiaCollectionMateriaToAttach : silabo.getMateriaCollection()) {
                materiaCollectionMateriaToAttach = em.getReference(materiaCollectionMateriaToAttach.getClass(), materiaCollectionMateriaToAttach.getIdMateria());
                attachedMateriaCollection.add(materiaCollectionMateriaToAttach);
            }
            silabo.setMateriaCollection(attachedMateriaCollection);
            Collection<Logros> attachedLogrosCollection = new ArrayList<Logros>();
            for (Logros logrosCollectionLogrosToAttach : silabo.getLogrosCollection()) {
                logrosCollectionLogrosToAttach = em.getReference(logrosCollectionLogrosToAttach.getClass(), logrosCollectionLogrosToAttach.getIdLogros());
                attachedLogrosCollection.add(logrosCollectionLogrosToAttach);
            }
            silabo.setLogrosCollection(attachedLogrosCollection);
            Collection<SilaboReferencia> attachedSilaboReferenciaCollection = new ArrayList<SilaboReferencia>();
            for (SilaboReferencia silaboReferenciaCollectionSilaboReferenciaToAttach : silabo.getSilaboReferenciaCollection()) {
                silaboReferenciaCollectionSilaboReferenciaToAttach = em.getReference(silaboReferenciaCollectionSilaboReferenciaToAttach.getClass(), silaboReferenciaCollectionSilaboReferenciaToAttach.getIdSilaboReferencia());
                attachedSilaboReferenciaCollection.add(silaboReferenciaCollectionSilaboReferenciaToAttach);
            }
            silabo.setSilaboReferenciaCollection(attachedSilaboReferenciaCollection);
            em.persist(silabo);
            if (idPeriodo != null) {
                idPeriodo.getSilaboCollection().add(silabo);
                idPeriodo = em.merge(idPeriodo);
            }
            if (idModalidad != null) {
                idModalidad.getSilaboCollection().add(silabo);
                idModalidad = em.merge(idModalidad);
            }
            for (Materia materiaCollectionMateria : silabo.getMateriaCollection()) {
                Silabo oldSilaboidSilaboOfMateriaCollectionMateria = materiaCollectionMateria.getSilaboidSilabo();
                materiaCollectionMateria.setSilaboidSilabo(silabo);
                materiaCollectionMateria = em.merge(materiaCollectionMateria);
                if (oldSilaboidSilaboOfMateriaCollectionMateria != null) {
                    oldSilaboidSilaboOfMateriaCollectionMateria.getMateriaCollection().remove(materiaCollectionMateria);
                    oldSilaboidSilaboOfMateriaCollectionMateria = em.merge(oldSilaboidSilaboOfMateriaCollectionMateria);
                }
            }
            for (Logros logrosCollectionLogros : silabo.getLogrosCollection()) {
                Silabo oldIdSilaboOfLogrosCollectionLogros = logrosCollectionLogros.getIdSilabo();
                logrosCollectionLogros.setIdSilabo(silabo);
                logrosCollectionLogros = em.merge(logrosCollectionLogros);
                if (oldIdSilaboOfLogrosCollectionLogros != null) {
                    oldIdSilaboOfLogrosCollectionLogros.getLogrosCollection().remove(logrosCollectionLogros);
                    oldIdSilaboOfLogrosCollectionLogros = em.merge(oldIdSilaboOfLogrosCollectionLogros);
                }
            }
            for (SilaboReferencia silaboReferenciaCollectionSilaboReferencia : silabo.getSilaboReferenciaCollection()) {
                Silabo oldSilaboIdOfSilaboReferenciaCollectionSilaboReferencia = silaboReferenciaCollectionSilaboReferencia.getSilaboId();
                silaboReferenciaCollectionSilaboReferencia.setSilaboId(silabo);
                silaboReferenciaCollectionSilaboReferencia = em.merge(silaboReferenciaCollectionSilaboReferencia);
                if (oldSilaboIdOfSilaboReferenciaCollectionSilaboReferencia != null) {
                    oldSilaboIdOfSilaboReferenciaCollectionSilaboReferencia.getSilaboReferenciaCollection().remove(silaboReferenciaCollectionSilaboReferencia);
                    oldSilaboIdOfSilaboReferenciaCollectionSilaboReferencia = em.merge(oldSilaboIdOfSilaboReferenciaCollectionSilaboReferencia);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Silabo silabo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Silabo persistentSilabo = em.find(Silabo.class, silabo.getIdSilabo());
            Periodo idPeriodoOld = persistentSilabo.getIdPeriodo();
            Periodo idPeriodoNew = silabo.getIdPeriodo();
            Modalidad idModalidadOld = persistentSilabo.getIdModalidad();
            Modalidad idModalidadNew = silabo.getIdModalidad();
            Collection<Materia> materiaCollectionOld = persistentSilabo.getMateriaCollection();
            Collection<Materia> materiaCollectionNew = silabo.getMateriaCollection();
            Collection<Logros> logrosCollectionOld = persistentSilabo.getLogrosCollection();
            Collection<Logros> logrosCollectionNew = silabo.getLogrosCollection();
            Collection<SilaboReferencia> silaboReferenciaCollectionOld = persistentSilabo.getSilaboReferenciaCollection();
            Collection<SilaboReferencia> silaboReferenciaCollectionNew = silabo.getSilaboReferenciaCollection();
            List<String> illegalOrphanMessages = null;
            for (Materia materiaCollectionOldMateria : materiaCollectionOld) {
                if (!materiaCollectionNew.contains(materiaCollectionOldMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Materia " + materiaCollectionOldMateria + " since its silaboidSilabo field is not nullable.");
                }
            }
            for (Logros logrosCollectionOldLogros : logrosCollectionOld) {
                if (!logrosCollectionNew.contains(logrosCollectionOldLogros)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Logros " + logrosCollectionOldLogros + " since its idSilabo field is not nullable.");
                }
            }
            for (SilaboReferencia silaboReferenciaCollectionOldSilaboReferencia : silaboReferenciaCollectionOld) {
                if (!silaboReferenciaCollectionNew.contains(silaboReferenciaCollectionOldSilaboReferencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SilaboReferencia " + silaboReferenciaCollectionOldSilaboReferencia + " since its silaboId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPeriodoNew != null) {
                idPeriodoNew = em.getReference(idPeriodoNew.getClass(), idPeriodoNew.getIdPeriodo());
                silabo.setIdPeriodo(idPeriodoNew);
            }
            if (idModalidadNew != null) {
                idModalidadNew = em.getReference(idModalidadNew.getClass(), idModalidadNew.getIdModalidad());
                silabo.setIdModalidad(idModalidadNew);
            }
            Collection<Materia> attachedMateriaCollectionNew = new ArrayList<Materia>();
            for (Materia materiaCollectionNewMateriaToAttach : materiaCollectionNew) {
                materiaCollectionNewMateriaToAttach = em.getReference(materiaCollectionNewMateriaToAttach.getClass(), materiaCollectionNewMateriaToAttach.getIdMateria());
                attachedMateriaCollectionNew.add(materiaCollectionNewMateriaToAttach);
            }
            materiaCollectionNew = attachedMateriaCollectionNew;
            silabo.setMateriaCollection(materiaCollectionNew);
            Collection<Logros> attachedLogrosCollectionNew = new ArrayList<Logros>();
            for (Logros logrosCollectionNewLogrosToAttach : logrosCollectionNew) {
                logrosCollectionNewLogrosToAttach = em.getReference(logrosCollectionNewLogrosToAttach.getClass(), logrosCollectionNewLogrosToAttach.getIdLogros());
                attachedLogrosCollectionNew.add(logrosCollectionNewLogrosToAttach);
            }
            logrosCollectionNew = attachedLogrosCollectionNew;
            silabo.setLogrosCollection(logrosCollectionNew);
            Collection<SilaboReferencia> attachedSilaboReferenciaCollectionNew = new ArrayList<SilaboReferencia>();
            for (SilaboReferencia silaboReferenciaCollectionNewSilaboReferenciaToAttach : silaboReferenciaCollectionNew) {
                silaboReferenciaCollectionNewSilaboReferenciaToAttach = em.getReference(silaboReferenciaCollectionNewSilaboReferenciaToAttach.getClass(), silaboReferenciaCollectionNewSilaboReferenciaToAttach.getIdSilaboReferencia());
                attachedSilaboReferenciaCollectionNew.add(silaboReferenciaCollectionNewSilaboReferenciaToAttach);
            }
            silaboReferenciaCollectionNew = attachedSilaboReferenciaCollectionNew;
            silabo.setSilaboReferenciaCollection(silaboReferenciaCollectionNew);
            silabo = em.merge(silabo);
            if (idPeriodoOld != null && !idPeriodoOld.equals(idPeriodoNew)) {
                idPeriodoOld.getSilaboCollection().remove(silabo);
                idPeriodoOld = em.merge(idPeriodoOld);
            }
            if (idPeriodoNew != null && !idPeriodoNew.equals(idPeriodoOld)) {
                idPeriodoNew.getSilaboCollection().add(silabo);
                idPeriodoNew = em.merge(idPeriodoNew);
            }
            if (idModalidadOld != null && !idModalidadOld.equals(idModalidadNew)) {
                idModalidadOld.getSilaboCollection().remove(silabo);
                idModalidadOld = em.merge(idModalidadOld);
            }
            if (idModalidadNew != null && !idModalidadNew.equals(idModalidadOld)) {
                idModalidadNew.getSilaboCollection().add(silabo);
                idModalidadNew = em.merge(idModalidadNew);
            }
            for (Materia materiaCollectionNewMateria : materiaCollectionNew) {
                if (!materiaCollectionOld.contains(materiaCollectionNewMateria)) {
                    Silabo oldSilaboidSilaboOfMateriaCollectionNewMateria = materiaCollectionNewMateria.getSilaboidSilabo();
                    materiaCollectionNewMateria.setSilaboidSilabo(silabo);
                    materiaCollectionNewMateria = em.merge(materiaCollectionNewMateria);
                    if (oldSilaboidSilaboOfMateriaCollectionNewMateria != null && !oldSilaboidSilaboOfMateriaCollectionNewMateria.equals(silabo)) {
                        oldSilaboidSilaboOfMateriaCollectionNewMateria.getMateriaCollection().remove(materiaCollectionNewMateria);
                        oldSilaboidSilaboOfMateriaCollectionNewMateria = em.merge(oldSilaboidSilaboOfMateriaCollectionNewMateria);
                    }
                }
            }
            for (Logros logrosCollectionNewLogros : logrosCollectionNew) {
                if (!logrosCollectionOld.contains(logrosCollectionNewLogros)) {
                    Silabo oldIdSilaboOfLogrosCollectionNewLogros = logrosCollectionNewLogros.getIdSilabo();
                    logrosCollectionNewLogros.setIdSilabo(silabo);
                    logrosCollectionNewLogros = em.merge(logrosCollectionNewLogros);
                    if (oldIdSilaboOfLogrosCollectionNewLogros != null && !oldIdSilaboOfLogrosCollectionNewLogros.equals(silabo)) {
                        oldIdSilaboOfLogrosCollectionNewLogros.getLogrosCollection().remove(logrosCollectionNewLogros);
                        oldIdSilaboOfLogrosCollectionNewLogros = em.merge(oldIdSilaboOfLogrosCollectionNewLogros);
                    }
                }
            }
            for (SilaboReferencia silaboReferenciaCollectionNewSilaboReferencia : silaboReferenciaCollectionNew) {
                if (!silaboReferenciaCollectionOld.contains(silaboReferenciaCollectionNewSilaboReferencia)) {
                    Silabo oldSilaboIdOfSilaboReferenciaCollectionNewSilaboReferencia = silaboReferenciaCollectionNewSilaboReferencia.getSilaboId();
                    silaboReferenciaCollectionNewSilaboReferencia.setSilaboId(silabo);
                    silaboReferenciaCollectionNewSilaboReferencia = em.merge(silaboReferenciaCollectionNewSilaboReferencia);
                    if (oldSilaboIdOfSilaboReferenciaCollectionNewSilaboReferencia != null && !oldSilaboIdOfSilaboReferenciaCollectionNewSilaboReferencia.equals(silabo)) {
                        oldSilaboIdOfSilaboReferenciaCollectionNewSilaboReferencia.getSilaboReferenciaCollection().remove(silaboReferenciaCollectionNewSilaboReferencia);
                        oldSilaboIdOfSilaboReferenciaCollectionNewSilaboReferencia = em.merge(oldSilaboIdOfSilaboReferenciaCollectionNewSilaboReferencia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = silabo.getIdSilabo();
                if (findSilabo(id) == null) {
                    throw new NonexistentEntityException("The silabo with id " + id + " no longer exists.");
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
            Silabo silabo;
            try {
                silabo = em.getReference(Silabo.class, id);
                silabo.getIdSilabo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The silabo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Materia> materiaCollectionOrphanCheck = silabo.getMateriaCollection();
            for (Materia materiaCollectionOrphanCheckMateria : materiaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Silabo (" + silabo + ") cannot be destroyed since the Materia " + materiaCollectionOrphanCheckMateria + " in its materiaCollection field has a non-nullable silaboidSilabo field.");
            }
            Collection<Logros> logrosCollectionOrphanCheck = silabo.getLogrosCollection();
            for (Logros logrosCollectionOrphanCheckLogros : logrosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Silabo (" + silabo + ") cannot be destroyed since the Logros " + logrosCollectionOrphanCheckLogros + " in its logrosCollection field has a non-nullable idSilabo field.");
            }
            Collection<SilaboReferencia> silaboReferenciaCollectionOrphanCheck = silabo.getSilaboReferenciaCollection();
            for (SilaboReferencia silaboReferenciaCollectionOrphanCheckSilaboReferencia : silaboReferenciaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Silabo (" + silabo + ") cannot be destroyed since the SilaboReferencia " + silaboReferenciaCollectionOrphanCheckSilaboReferencia + " in its silaboReferenciaCollection field has a non-nullable silaboId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Periodo idPeriodo = silabo.getIdPeriodo();
            if (idPeriodo != null) {
                idPeriodo.getSilaboCollection().remove(silabo);
                idPeriodo = em.merge(idPeriodo);
            }
            Modalidad idModalidad = silabo.getIdModalidad();
            if (idModalidad != null) {
                idModalidad.getSilaboCollection().remove(silabo);
                idModalidad = em.merge(idModalidad);
            }
            em.remove(silabo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Silabo> findSilaboEntities() {
        return findSilaboEntities(true, -1, -1);
    }

    public List<Silabo> findSilaboEntities(int maxResults, int firstResult) {
        return findSilaboEntities(false, maxResults, firstResult);
    }

    private List<Silabo> findSilaboEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Silabo.class));
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

    public Silabo findSilabo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Silabo.class, id);
        } finally {
            em.close();
        }
    }

    public int getSilaboCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Silabo> rt = cq.from(Silabo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
