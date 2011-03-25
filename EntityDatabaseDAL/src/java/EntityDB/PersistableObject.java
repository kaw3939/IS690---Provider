/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package EntityDB;
import java.io.Serializable;
import org.hibernate.*;
/**
 * Base class for objects we want to persist to the database.
 * @author Rick Shaub
 */
public abstract class PersistableObject implements Serializable
{
    /**
     *Closes the session for this thread.  SHoulkd be called when all
        persistence operations are completed.
     */
    public static void close() {
        SessionFactory sessionFactory = SessionFactoryUtil.getInstance();
        // new AnnotationConfiguration().configure().buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        session.close();
    }
    /**
     * This method should return the ID(primary key) used by hibernate for the object.
     * @return
     * The ID(primary key) used by hibernate for the object.
     */
    abstract protected Serializable getID();

   

    /**
     * Deletes the object from the database.
     * @param load
     * Set true if the object was or may have been retrieved from another hibernate session.
     * 
     */
    public void delete(boolean load) {
        SessionFactory sessionFactory = SessionFactoryUtil.getInstance();
        // new AnnotationConfiguration().configure().buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        Object deleted = this;
        if (load) {
            deleted = session.get(this.getClass(), getID(), LockMode.UPGRADE);
        }
        session.delete(deleted);
        session.flush();
        tx.commit();
        //session.close();
    }
    /* Saves the object to the database.  This is either an insert or update
     * operation, depending on the status of the object.
     *
     */
    public void save() {
        save(false);
    }

    /**
     * Saves the object to the database.  This is either an insert or update
     * operation, depending on the status of the object.
     * @param load
     * Set true if the object was or may have been retrieved from another hibernate session.
     */
    public void save(boolean load) {
        SessionFactory sessionFactory = SessionFactoryUtil.getInstance();
        // new AnnotationConfiguration().configure().buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        Object saved = this;
        if (load) {
            saved = session.get(this.getClass(), getID(), LockMode.UPGRADE);
        }
       // session.saveOrUpdate(saved);
       // using saveOrUpdate was causing errors on update, so using merge instead.
        session.merge(saved);
        session.flush();
        tx.commit();        
    }

}
