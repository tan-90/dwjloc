package br.edu.ifsuldeminas.dwjloc.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * File created @[4/7/2017]
 */
public class JPAUtil
{
    private static EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("dwjloc");

    public static EntityManager getEntityManager()
    {
        return managerFactory.createEntityManager();
    }

}
