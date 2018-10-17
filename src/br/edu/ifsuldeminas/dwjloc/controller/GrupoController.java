package br.edu.ifsuldeminas.dwjloc.controller;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.edu.ifsuldeminas.dwjloc.dao.Dao;
import br.edu.ifsuldeminas.dwjloc.lib.LibConstantes;
import br.edu.ifsuldeminas.dwjloc.model.Grupo;

@ManagedBean
@ViewScoped
public class GrupoController
{
	private Grupo grupo = new Grupo();

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public List<Grupo> getGrupos() {
		return new Dao<Grupo>(Grupo.class).getAll();
	}

	public void carregar(Grupo grupo) {
		if(isNotConstant(grupo.getId()))
		{
			this.grupo = grupo;
		}else
		{
			FacesContext.getCurrentInstance().addMessage("grupo", new FacesMessage("O grupo " + grupo.getNome() + " é constante e não pode ser alterado."));
		}
	}

	public void gravar(Grupo grupo) {
		if (grupo.getId() == null) {
			new Dao<Grupo>(Grupo.class).add(grupo);
		} else {
			new Dao<Grupo>(Grupo.class).update(grupo);
		}
		this.grupo = new Grupo();
	}

	public void remover(Grupo grupo) {
		if(isNotConstant(grupo.getId()))
		{
			new Dao<Grupo>(Grupo.class).remove(grupo);
		}else
		{
			FacesContext.getCurrentInstance().addMessage("grupo", new FacesMessage("O grupo " + grupo.getNome() + " é constante e não pode ser alterado."));
		}
	}
	
	public boolean isNotConstant(Integer id)
	{
		return id != LibConstantes.Banco.ID_GRUPO_CLIENTES && id != LibConstantes.Banco.ID_GRUPO_ADMINISTRADORES;
	}

	public boolean isAdministrador(Integer id)
	{
		return id == LibConstantes.Banco.ID_GRUPO_ADMINISTRADORES;
	}
	public String buttonDisabled(Integer id)
	{
		return !isNotConstant(id) ? " disabled" : "";
	}
}
