/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.persist.beans;

import com.utpl.javasilabopersist.entidad.Referencia;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 *
 * @author root
 */

@Stateful
public class ReferenciaBean {
private int counter = 0;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
 @PersistenceUnit
    EntityManagerFactory emf;
    public List<Referencia> listarReferencia() {
        return emf.createEntityManager().createNamedQuery("Referencia.findAll").getResultList();
    }

}
