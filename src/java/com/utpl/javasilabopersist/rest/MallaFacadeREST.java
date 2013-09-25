/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.rest;

import com.utpl.javasilabopersist.entidad.Malla;
import com.utpl.javasilabopersist.entidad.VersionMalla;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author root
 */
@Stateless
@Path("com.utpl.javasilabopersist.entidad.malla")
public class MallaFacadeREST extends AbstractFacade<Malla> {

    @PersistenceContext(unitName = "EjbSilaboPersistWPU")
    private EntityManager em;
    

    public MallaFacadeREST() {
        super(Malla.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Malla entity) {
        super.create(entity);
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Malla entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Malla find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Malla> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Malla> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return findRange(new int[]{from, to});
    }

//    
//    public List<Malla> buscarM(int vmalla) {
//      List<Malla> e =   em.createQuery(
//    "SELECT c FROM Malla c WHERE c.idVersionMalla LIKE :Vmalla")
//    .setParameter("Vmalla", vmalla)
//    //.setMaxResults(10)
//    .getResultList();
// 
//        return e;
//    }
    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @POST
    @Path("{campo}/{valor}")
    @Produces({"application/xml", "application/json"})
    public List<Malla> buscarMientras(@PathParam("campo")String campo , @PathParam("valor") Integer valor) {
        System.out.println(campo+"-"+valor);
        return this.buscarM(campo,valor);
    }

   
}
