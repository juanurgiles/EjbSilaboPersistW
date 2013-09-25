/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utpl.javasilabopersist.servlets;

import com.utpl.javasilabopersist.entidad.Referencia;
import com.utpl.persist.beans.ReferenciaBean;
import com.utpl.persist.ejb.PersistEjb;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author root
 */

@WebServlet(name = "SilaboPersistServlet", urlPatterns = {"/SilaboPersistServlet"})
public class SilaboPersistServlet extends HttpServlet {
    ReferenciaBean referenciaBean = lookupReferenciaBeanBean();

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    PersistEjb persistEJB;
    @EJB
    ReferenciaBean referenciaBean1;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            List<Referencia> l = persistEJB.buscarTodos();
            List<Referencia> l1 = referenciaBean1.listarReferencia();
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SilaboPersistServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SilaboPersistServlet at " + request.getContextPath() + "</h1>");
            for (Referencia r : l) {
                out.println("<b>Nombre</b>"+r.getTituloReferencia()+"<br>");
            }
            out.println("<h1>Test  " + request.getContextPath() + "</h1>");
            for (Referencia r : l1) {
                out.println("<b>Nombre</b>"+r.getTituloReferencia()+"<br>");
            }
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private ReferenciaBean lookupReferenciaBeanBean() {
        try {
            Context c = new InitialContext();
            return (ReferenciaBean) c.lookup("java:global/EjbSilaboPersistW/ReferenciaBean!com.utpl.persist.beans.ReferenciaBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
