package br.edu.ifsuldeminas.dwjloc.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.edu.ifsuldeminas.dwjloc.dao.Dao;
import br.edu.ifsuldeminas.dwjloc.model.PessoaJuridica;
import br.edu.ifsuldeminas.dwjloc.model.Usuario;

@ManagedBean
@ViewScoped
public class PessoaJuridicaController extends UsuarioController
{
	private PessoaJuridica pessoaJuridica = new PessoaJuridica();

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}
	
	public List<PessoaJuridica> getPessoasJuridicas()
	{
		return new Dao<PessoaJuridica>(PessoaJuridica.class).getAll();
	}
	
	@Override
	public void carregar(Usuario pessoaJuridica)
	{
		super.carregar(pessoaJuridica);
		this.pessoaJuridica = (PessoaJuridica) pessoaJuridica;
	}
	
	@Override
	public void gravar(Usuario pessoaJuridica) {
		super.gravar(pessoaJuridica);
		this.pessoaJuridica = new PessoaJuridica();
	}
}
