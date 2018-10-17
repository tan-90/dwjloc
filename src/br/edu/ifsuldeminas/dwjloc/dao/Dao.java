package br.edu.ifsuldeminas.dwjloc.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.edu.ifsuldeminas.dwjloc.util.JPAUtil;

public class Dao<T>
{
    private final Class<T> baseClass;

    public Dao(Class<T> baseClass)
    {
        this.baseClass = baseClass;
    }

    public void add(T t)
    {
        EntityManager manager = JPAUtil.getEntityManager();
        manager.getTransaction().begin();
        manager.persist(t);
        manager.getTransaction().commit();
        manager.close();
    }

    public void remove(T t)
    {
        EntityManager manager = JPAUtil.getEntityManager();
        manager.getTransaction().begin();
        t = manager.merge(t);
        manager.remove(t);
        manager.getTransaction().commit();
        manager.close();
    }

    public void update(T t)
    {
        EntityManager manager = JPAUtil.getEntityManager();
        manager.getTransaction().begin();
        manager.merge(t);
        manager.getTransaction().commit();
        manager.close();
    }

    public List<T> getAll()
    {
        EntityManager manager = JPAUtil.getEntityManager();
        manager.getTransaction().begin();

        String jpql = "SELECT t FROM " + baseClass.getName() + " t";
        Query query = manager.createQuery(jpql);

        List<T> result = query.getResultList();

        manager.close();

        return result;
    }

    public T getById(int id)
    {
        EntityManager manager = JPAUtil.getEntityManager();
        manager.getTransaction().begin();
        T t = manager.find(baseClass, id);
        //manager.remove(t);
        manager.getTransaction().commit();
        manager.close();

        return t;
    }

    public Long getCount()
    {
        EntityManager manager = JPAUtil.getEntityManager();
        manager.getTransaction().begin();

        String jpql = "SELECT count(s) FROM " + baseClass.getName() + " s";
        Query query = manager.createQuery(jpql);

        Long result = (Long)query.getSingleResult();

        manager.close();

        return result;
    }

    // Safe transaction methods
    public void add(T t, EntityManager manager)
    {
        manager.persist(t);
    }

    public void remove(T t, EntityManager manager)
    {
        t = manager.merge(t);
        manager.remove(t);
    }

    public void update(T t, EntityManager manager)
    {
        manager.merge(t);
    }

    public List<T> getAll(EntityManager manager)
    {
        String jpql = "SELECT t FROM " + baseClass.getName() + " t";
        Query query = manager.createQuery(jpql);

        List<T> result = query.getResultList();

        return result;
    }

    public T getById(int id, EntityManager manager)
    {
        T t = manager.find(baseClass, id);

        return t;
    }

    public Long getCount(EntityManager manager)
    {
        String jpql = "SELECT count(s) FROM " + baseClass.getName() + " s";
        Query query = manager.createQuery(jpql);

        Long result = (Long)query.getSingleResult();

        return result;
    }
}
