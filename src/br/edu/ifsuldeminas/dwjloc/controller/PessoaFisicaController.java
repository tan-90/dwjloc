package br.edu.ifsuldeminas.dwjloc.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.edu.ifsuldeminas.dwjloc.dao.Dao;
import br.edu.ifsuldeminas.dwjloc.model.PessoaFisica;
import br.edu.ifsuldeminas.dwjloc.model.Usuario;

@ManagedBean
@ViewScoped
public class PessoaFisicaController extends UsuarioController
{
	private PessoaFisica pessoaFisica = new PessoaFisica();

	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}
	
	public List<PessoaFisica> getPessoasFisicas()
	{
		return new Dao<PessoaFisica>(PessoaFisica.class).getAll();
	}
	
	@Override
	public void carregar(Usuario pessoaFisica)
	{
		super.carregar(pessoaFisica);
		this.pessoaFisica = (PessoaFisica) pessoaFisica;
	}
	
	@Override
	public void gravar(Usuario pessoaFisica) {
		super.gravar(pessoaFisica);
		this.pessoaFisica = new PessoaFisica();
	}
}
