package br.edu.ifsuldeminas.dwjloc.controller;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.edu.ifsuldeminas.dwjloc.dao.Dao;
import br.edu.ifsuldeminas.dwjloc.dao.DaoFerramenta;
import br.edu.ifsuldeminas.dwjloc.lib.LibConstantes;
import br.edu.ifsuldeminas.dwjloc.model.EstadoFerramenta;
import br.edu.ifsuldeminas.dwjloc.model.Ferramenta;
import br.edu.ifsuldeminas.dwjloc.model.PessoaFisica;
import br.edu.ifsuldeminas.dwjloc.model.TipoFerramenta;

@ManagedBean
@ViewScoped
public class FerramentaController
{
	private Ferramenta ferramenta = new Ferramenta();
	
	private Integer idTipo;
	private Integer idEstado;

	public Ferramenta getFerramenta() {
		return ferramenta;
	}

	public void setFerramenta(Ferramenta ferramenta) {
		this.ferramenta = ferramenta;
	}
	
	public Integer getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(Integer idTipo) {
		this.idTipo = idTipo;
	}

	public Integer getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}

	public List<Ferramenta> getFerramentas()
	{
		return new Dao<Ferramenta>(Ferramenta.class).getAll();
	}
	
	public void carregar(Ferramenta ferramenta)
	{
		this.ferramenta = ferramenta;
		idTipo = ferramenta.getTipo().getId();
		idEstado = ferramenta.getEstado().getId();
	}
	
	public void gravar()
	{
		TipoFerramenta tipo = new Dao<TipoFerramenta>(TipoFerramenta.class).getById(idTipo);
		EstadoFerramenta estado = new Dao<EstadoFerramenta>(EstadoFerramenta.class).getById(idEstado);
		
		ferramenta.setTipo(tipo);
		ferramenta.setEstado(estado);
		
		if(ferramenta.getId() == null)
		{
			new Dao<Ferramenta>(Ferramenta.class).add(ferramenta);			
		}else
		{
			new Dao<Ferramenta>(Ferramenta.class).update(ferramenta);
		}
		this.ferramenta = new Ferramenta();
		idTipo = 0;
		idEstado = 0;
	}

	public List<Ferramenta> getPorTipoEstado(TipoFerramenta tipo, EstadoFerramenta estado)
	{
		return new DaoFerramenta().getByTipoAndEstado(tipo, estado);
	}
	
	public void remover(Ferramenta ferramenta)
	{
		try
		{
			new Dao<Ferramenta>(Ferramenta.class).remove(ferramenta);
		}catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage("ferramenta", new FacesMessage("Não é possível remover uma ferramenta com locações salvas."));
		}
	}

	public String updateButtonDisabled(Ferramenta ferramenta)
	{
		ferramenta = new Dao<Ferramenta>(Ferramenta.class).getById(ferramenta.getId());

		return ferramenta.getEstado().getId() == LibConstantes.Banco.ID_ESTADO_ALUGADO ? " disabled" : "";
	}
}
