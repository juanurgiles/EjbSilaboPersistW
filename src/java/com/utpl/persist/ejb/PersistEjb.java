/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.persist.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 *
 * @author root
 */
@Stateless
public class PersistEjb {

    @PersistenceUnit
    EntityManagerFactory emf;
    public List buscarTodos(){
        return emf.createEntityManager().createNamedQuery("Referencia.findAll").getResultList();
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

}
