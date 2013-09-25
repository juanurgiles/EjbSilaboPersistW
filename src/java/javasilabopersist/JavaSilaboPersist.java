/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javasilabopersist;




import com.utpl.javasilabopersist.controlador.ReferenciaJpaController;
import com.utpl.javasilabopersist.entidad.Referencia;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author root
 */
public class JavaSilaboPersist {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EjbSilaboPersistWPU");
        ReferenciaJpaController mjc = new ReferenciaJpaController(emf);
        List<Referencia> lr = mjc.findReferenciaEntities();
        for (Referencia referencia : lr) {
            System.out.println(referencia.getTituloReferencia());
        }
        Referencia r = new Referencia();
        r.setTituloReferencia("res");
        mjc.create(r);
        
    }
}
