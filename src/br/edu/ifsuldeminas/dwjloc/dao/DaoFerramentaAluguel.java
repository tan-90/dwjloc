package br.edu.ifsuldeminas.dwjloc.dao;

import br.edu.ifsuldeminas.dwjloc.model.Ferramenta;
import br.edu.ifsuldeminas.dwjloc.model.FerramentaAluguel;
import br.edu.ifsuldeminas.dwjloc.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class DaoFerramentaAluguel
{
    public List<FerramentaAluguel> getPendencias(Integer idCliente)
    {
        String jpql = "SELECT f FROM FerramentaAluguel f WHERE f.usuario.id = :pUsuarioId AND (f.pago = :pPago OR f.entregue = :pEntregue)";

        EntityManager manager = JPAUtil.getEntityManager();
        Query query = manager.createQuery(jpql);
        query.setParameter("pUsuarioId", idCliente);
        query.setParameter("pPago", false);
        query.setParameter("pEntregue", false);
        List<FerramentaAluguel> result = query.getResultList();
        manager.close();

        return result;
    }

    public List<FerramentaAluguel> getHistorico(Integer idCliente)
    {
        String jpql = "SELECT f FROM FerramentaAluguel f WHERE f.usuario.id = :pUsuarioId AND (f.pago = :pPago AND f.entregue = :pEntregue)";

        EntityManager manager = JPAUtil.getEntityManager();
        Query query = manager.createQuery(jpql);
        query.setParameter("pUsuarioId", idCliente);
        query.setParameter("pPago", true);
        query.setParameter("pEntregue", true);
        List<FerramentaAluguel> result = query.getResultList();
        manager.close();

        return result;
    }
}
