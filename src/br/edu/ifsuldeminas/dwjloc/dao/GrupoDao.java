package br.edu.ifsuldeminas.dwjloc.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.edu.ifsuldeminas.dwjloc.model.Funcionalidade;
import br.edu.ifsuldeminas.dwjloc.model.Grupo;
import br.edu.ifsuldeminas.dwjloc.util.JPAUtil;

import java.util.ArrayList;
import java.util.List;

public class GrupoDao {
	
	public Grupo getGrupoFuncionalidades (Grupo grupo) {
		Grupo resultado = null;

		try
		{
			String jpql = "SELECT g FROM Grupo g JOIN FETCH g.funcionalidades WHERE g.id = :pIdGrupo";

			EntityManager em = JPAUtil.getEntityManager();

			TypedQuery<Grupo> query = em.createQuery(jpql, Grupo.class);
			query.setParameter("pIdGrupo", grupo.getId());

			resultado = query.getSingleResult();
			em.close();
		}catch (NoResultException e)
		{
			return grupo;
		}

		return resultado;
	}


	public List<Funcionalidade> getMissingFuncionalidades(Integer idGrupo)
	{
		List<Funcionalidade> resultado = new ArrayList<>();
		if(idGrupo == null || idGrupo == 0)
		{
			return resultado;
		}

		List<Funcionalidade> funcionalidades = new Dao<Funcionalidade>(Funcionalidade.class).getAll();
		Grupo grupo = new Dao<Grupo>(Grupo.class).getById(idGrupo);
		grupo = getGrupoFuncionalidades(grupo);
		if(grupo.getFuncionalidades() == null || grupo.getFuncionalidades().size() == 0)
		{
			return funcionalidades;
		}

		for(Funcionalidade funcionalidade : funcionalidades)
		{
			boolean missing = true;
			for(Funcionalidade grupoFuncionalidade : grupo.getFuncionalidades())
			{
				if(grupoFuncionalidade.getId() == funcionalidade.getId())
				{
					missing = false;
					break;
				}
			}

			if(missing)
			{
				resultado.add(funcionalidade);
			}
		}

		return resultado;
	}
}
