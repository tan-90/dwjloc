package br.edu.ifsuldeminas.dwjloc.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.edu.ifsuldeminas.dwjloc.lib.LibConstantes;
import br.edu.ifsuldeminas.dwjloc.model.Grupo;
import br.edu.ifsuldeminas.dwjloc.model.Usuario;
import br.edu.ifsuldeminas.dwjloc.util.JPAUtil;

public class UsuarioDao
{
	public Usuario buscarPorLoginESenha(Usuario f)
	{
		Usuario res = null;
		
		String jpql = "SELECT f FROM Usuario f WHERE f.login = :pLogin AND f.senha = :pSenha";
		
		EntityManager manager = JPAUtil.getEntityManager();
		
		TypedQuery<Usuario> query = manager.createQuery(jpql, Usuario.class);
		query.setParameter("pLogin", f.getLogin());
		query.setParameter("pSenha", f.getSenha());
		
		try {
			res = query.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
		}
		
		manager.close();
		
		return res;
	}
	
	public List<Usuario> getByGroup(Grupo grupo)
	{
		String jpql = "SELECT c FROM Usuario c WHERE c.grupo = :pGrupo";
		EntityManager manager = JPAUtil.getEntityManager();
		TypedQuery query = manager.createQuery(jpql, Usuario.class);
		query.setParameter("pGrupo", grupo);
		
		return query.getResultList();
	}

	public boolean isAdm(Usuario usuario)
	{
		usuario = new Dao<Usuario>(Usuario.class).getById(usuario.getId());

		return usuario.getGrupo().getId() != LibConstantes.Banco.ID_GRUPO_ADMINISTRADORES;
	}
}
