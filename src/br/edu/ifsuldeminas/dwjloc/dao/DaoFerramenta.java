package br.edu.ifsuldeminas.dwjloc.dao;

import br.edu.ifsuldeminas.dwjloc.model.EstadoFerramenta;
import br.edu.ifsuldeminas.dwjloc.model.Ferramenta;
import br.edu.ifsuldeminas.dwjloc.model.TipoFerramenta;
import br.edu.ifsuldeminas.dwjloc.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by vinic on 6/9/2017.
 */

public class DaoFerramenta
{
    public Ferramenta setEstado(Ferramenta ferramenta, EstadoFerramenta estado)
    {
        EntityManager manager = JPAUtil.getEntityManager();
        manager.getTransaction().begin();
        ferramenta = manager.find(Ferramenta.class, ferramenta.getId());
        ferramenta.setEstado(estado);
        manager.getTransaction().commit();
        manager.close();
        return ferramenta;
    }

    public List<Ferramenta> getByEstado(EstadoFerramenta estado)
    {
        String jpql = "SELECT f FROM Ferramenta f WHERE f.estado = :pEstado";
        EntityManager manager = JPAUtil.getEntityManager();

        Query query = manager.createQuery(jpql);
        query.setParameter("pEstado", estado);

        return query.getResultList();
    }

    public List<Ferramenta> getByTipo(TipoFerramenta tipo)
    {
        String jpql = "SELECT f FROM Ferramenta f WHERE f.tipo = :pTipo";
        EntityManager manager = JPAUtil.getEntityManager();

        Query query = manager.createQuery(jpql);
        query.setParameter("pTipo", tipo);

        return query.getResultList();
    }

    public List<Ferramenta> getByTipoAndEstado(TipoFerramenta tipo, EstadoFerramenta estado)
    {
        String jpql = "SELECT f FROM Ferramenta f WHERE f.tipo = :pTipo AND f.estado = :pEstado";
        EntityManager manager = JPAUtil.getEntityManager();

        Query query = manager.createQuery(jpql);
        query.setParameter("pTipo", tipo);
        query.setParameter("pEstado", estado);

        return query.getResultList();
    }

    public List<Ferramenta> getByTipoAndEstado(TipoFerramenta tipo, EstadoFerramenta estado, EntityManager manager)
    {
        String jpql = "SELECT f FROM Ferramenta f WHERE f.tipo = :pTipo AND f.estado = :pEstado";

        Query query = manager.createQuery(jpql);
        query.setParameter("pTipo", tipo);
        query.setParameter("pEstado", estado);

        return query.getResultList();
    }
}
