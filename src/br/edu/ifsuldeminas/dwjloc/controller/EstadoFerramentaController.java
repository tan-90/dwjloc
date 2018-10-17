package br.edu.ifsuldeminas.dwjloc.controller;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.edu.ifsuldeminas.dwjloc.dao.Dao;
import br.edu.ifsuldeminas.dwjloc.lib.LibConstantes;
import br.edu.ifsuldeminas.dwjloc.model.EstadoFerramenta;

@ManagedBean
@ViewScoped
public class EstadoFerramentaController
{
	private EstadoFerramenta estadoFerramenta = new EstadoFerramenta();

	public EstadoFerramenta getEstadoFerramenta() {
		return estadoFerramenta;
	}

	public void setEstadoFerramenta(EstadoFerramenta estadoFerramenta) {
		this.estadoFerramenta = estadoFerramenta;
	}
	
	public List<EstadoFerramenta> getEstadosFerramentas()
	{
		return new Dao<EstadoFerramenta>(EstadoFerramenta.class).getAll();
	}
	
	public void carregar(EstadoFerramenta estadoFerramenta)
	{
		if(isNotConstant(estadoFerramenta.getId()))
			this.estadoFerramenta = estadoFerramenta;
		else
			FacesContext.getCurrentInstance().addMessage("estadoferramenta", new FacesMessage("O estado " + estadoFerramenta.getNome() + " é constante e não pode ser alterado."));

	}
	
	public void gravar(EstadoFerramenta estadoFerramenta)
	{
		if(estadoFerramenta.getId() == null)
		{
			new Dao<EstadoFerramenta>(EstadoFerramenta.class).add(estadoFerramenta);			
		}else
		{
			new Dao<EstadoFerramenta>(EstadoFerramenta.class).update(estadoFerramenta);
		}
		this.estadoFerramenta = new EstadoFerramenta();
	}
	
	public void remover(EstadoFerramenta estadoFerramenta)
	{
		if(isNotConstant(estadoFerramenta.getId()))
			new Dao<EstadoFerramenta>(EstadoFerramenta.class).remove(estadoFerramenta);
		else
			FacesContext.getCurrentInstance().addMessage("estadoferramenta", new FacesMessage("O estado " + estadoFerramenta.getNome() + " é constante e não pode ser alterado."));

	}

	public boolean isNotConstant(Integer id)
	{
		return id != LibConstantes.Banco.ID_ESTADO_DISPONIVEL && id != LibConstantes.Banco.ID_ESTADO_ALUGADO && id != LibConstantes.Banco.ID_ESTADO_DANIFICADO;
	}

	public String buttonDisabled(Integer id)
	{
		return !isNotConstant(id) ? " disabled" : "";
	}
}
