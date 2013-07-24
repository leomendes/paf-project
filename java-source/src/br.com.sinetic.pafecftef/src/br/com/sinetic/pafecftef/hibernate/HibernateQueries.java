/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.hibernate;


import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;


/**
 *
 * @author Sinetic
 */
public class HibernateQueries {
    
    
   
    // session factory
    private SessionFactory hibSessionFactory;
    
    
    
    
    /**
     * constructor
     */
    public HibernateQueries() {
        // hibernate session factory initialization
        hibSessionFactory = new Configuration().configure().buildSessionFactory();
    }
    
    /**
     * constructor
     */
    public HibernateQueries(String configFile) {
        // hibernate session factory initialization
        hibSessionFactory = new Configuration().configure(configFile).buildSessionFactory();
    }
    
    
    /**
     * shutdown hibernate
     */
    public void shutdown() {
        
        try {
            
            this.hibSessionFactory.close();
            
        } 
        catch (Exception ex) {
            
            ex.printStackTrace();
        }
    
    }
    
    /**
     * get new hibernate session in order to run database queries
     * @return 
     */
    public Session newSession() {

        try {

            return hibSessionFactory.openSession();

        } catch (Exception ex) {

            ex.printStackTrace();

            return null;
        }
        
    }
   
  
    /**
     * update object into database
     * @param entity
     * @return 
     */
    public boolean save(Object entity) {
     
        boolean success = false;
        
        Session session = newSession();
        if (session != null) {

            synchronized (session) {
                
                success = save(session, entity);
            }
            
            session.close();
        }
        
        return success;
    }
    
    /**
     * update object into database
     * @param session 
     */
    public boolean save(Session session, Object entity) {
        
        Transaction transaction = session.beginTransaction();
        
        try {

            session.save(entity);
            transaction.commit();
            
        } catch (HibernateException ex) {
            
            transaction.rollback();
            ex.printStackTrace();
            
            return false;
        }
        
        return true;
    }
    
    /**
     * update object into database
     * @param entity
     * @return 
     */
    public boolean update(Object entity) {
     
        boolean success = false;
        
        Session session = newSession();
        if (session != null) {

            synchronized (session) {
                
                success = update(session, entity);
            }
            
            session.close();
        }
        
        return success;
    }
    
    /**
     * update object into database
     * @param session 
     */
    public boolean update(Session session, Object entity) {
        
        Transaction transaction = session.beginTransaction();
        
        try {

            session.update(entity);
            transaction.commit();
            
        } catch (HibernateException ex) {
            
            transaction.rollback();
            ex.printStackTrace();
            
            return false;
        }
        
        return true;
    }
    
    /**
     * delete database object instance
     * @param entity
     * @return 
     */
    public boolean delete(Object entity) {
        
        boolean success = false;
        
        Session session = newSession();
        if (session != null) {

            synchronized (session) {
                
                success = delete(session, entity);
            }
            
            session.close();
        }
        
        return success;
    }
    
    /**
     * update object into database
     * @param session 
     */
    public boolean delete(Session session, Object entity) {
        
        Transaction transaction = session.beginTransaction();
        
        try {

            session.delete(entity);
            transaction.commit();
            
        } catch (HibernateException ex) {
            
            transaction.rollback();
            ex.printStackTrace();
            
            return false;
        }
        
        return true;
    }
    
    /**
     * refresh entity
     * @return 
     */
    public void refresh(Object hibernateObj) {
        
        Session session = newSession();
        if (session != null) {
            
            synchronized (session) {
        
                try {
                    
                    session.refresh(hibernateObj);
                    
                } 
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            
            session.close();
           
        }
    }
    
  
   
}