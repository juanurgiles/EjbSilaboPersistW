/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.rest;

import com.utpl.javasilabopersist.entidad.TipoReferencia;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
@Path("com.utpl.javasilabopersist.entidad.tiporeferencia")
public class TipoReferenciaFacadeREST extends AbstractFacade<TipoReferencia> {
    @PersistenceContext(unitName = "EjbSilaboPersistWPU")
    private EntityManager em;

    public TipoReferenciaFacadeREST() {
        super(TipoReferencia.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(TipoReferencia entity) {
        super.create(entity);
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(TipoReferencia entity) {
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
    public TipoReferencia find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<TipoReferencia> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<TipoReferencia> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

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
    
}
