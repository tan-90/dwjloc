package br.edu.ifsuldeminas.dwjloc.controller;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.edu.ifsuldeminas.dwjloc.dao.Dao;
import br.edu.ifsuldeminas.dwjloc.model.TipoFerramenta;

@ManagedBean
@ViewScoped
public class TipoFerramentaController {
	private TipoFerramenta tipoFerramenta = new TipoFerramenta();

	public TipoFerramenta getTipoFerramenta() {
		return tipoFerramenta;
	}

	public void setTipoFerramenta(TipoFerramenta tipoFerramenta) {
		this.tipoFerramenta = tipoFerramenta;
	}

	public List<TipoFerramenta> getTiposFerramentas() {
		return new Dao<TipoFerramenta>(TipoFerramenta.class).getAll();
	}

	public void carregar(TipoFerramenta tipoFerramenta) {
		this.tipoFerramenta = tipoFerramenta;
	}

	public void gravar(TipoFerramenta tipoFerramenta) {
		if (tipoFerramenta.getId() == null) {
			new Dao<TipoFerramenta>(TipoFerramenta.class).add(tipoFerramenta);
		} else {
			new Dao<TipoFerramenta>(TipoFerramenta.class).update(tipoFerramenta);
		}
		this.tipoFerramenta = new TipoFerramenta();
	}

	public void remover(TipoFerramenta tipoFerramenta) {
		try
		{
			new Dao<TipoFerramenta>(TipoFerramenta.class).remove(tipoFerramenta);
		}catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage("tipoferramenta", new FacesMessage("Não é possível remover um tipo com ferramentas salvas."));

		}
	}
}
