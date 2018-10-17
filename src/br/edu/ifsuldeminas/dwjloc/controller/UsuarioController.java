package br.edu.ifsuldeminas.dwjloc.controller;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.edu.ifsuldeminas.dwjloc.dao.Dao;
import br.edu.ifsuldeminas.dwjloc.dao.UsuarioDao;
import br.edu.ifsuldeminas.dwjloc.lib.LibConstantes;
import br.edu.ifsuldeminas.dwjloc.model.Grupo;
import br.edu.ifsuldeminas.dwjloc.model.PessoaFisica;
import br.edu.ifsuldeminas.dwjloc.model.Usuario;
import br.edu.ifsuldeminas.dwjloc.util.Utils;


@ManagedBean
@ViewScoped
public class UsuarioController
{
	protected String senhaBd;
	protected Integer idGrupo;
	
	
	public String getSenhaBd() {
		return senhaBd;
	}

	public void setSenhaBd(String senhaBd) {
		this.senhaBd = senhaBd;
	}

	public Integer getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(Integer idGrupo) {
		this.idGrupo = idGrupo;
	}

	public void gravar(Usuario usuario)
	{
		try
		{
			Grupo grupo = new Dao<Grupo>(Grupo.class).getById(idGrupo);
			usuario.setGrupo(grupo);

			if (!usuario.getSenha().equals(senhaBd))
			{
				usuario.setSenha(Utils.toMD5(usuario.getSenha()));
			}

			if (usuario.getId() == null)
			{
				new Dao<Usuario>(Usuario.class).add(usuario);
			} else
			{
				new Dao<Usuario>(Usuario.class).update(usuario);
			}
			senhaBd = "";
			idGrupo = 0;
		}catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(usuario instanceof PessoaFisica ? "pessoafisica" : "pessoajuridica", new FacesMessage("Nome de usuário já existe."));
		}
	}
	
	public void carregar(Usuario usuario)
	{
		senhaBd = usuario.getSenha();
		idGrupo = usuario.getGrupo().getId();
	}
	
	public void remover(Usuario usuario)
	{
		try
		{
			new Dao<Usuario>(Usuario.class).remove(usuario);
		}catch (Exception e)
		{
            FacesContext.getCurrentInstance().addMessage(usuario instanceof PessoaFisica ? "pessoafisica" : "pessoajuridica", new FacesMessage("Não é possível remover um usuário com locações salvas."));

        }
	}
	
	public List<Usuario> getUsuarios()
	{
		return new Dao<Usuario>(Usuario.class).getAll();
	}
	
	public List<Usuario> getClientes()
	{
		Grupo grupo = new Grupo();
		grupo.setId(LibConstantes.Banco.ID_GRUPO_CLIENTES);
		return new UsuarioDao().getByGroup(grupo);
	}
	
	public List<Grupo> getGrupos()
	{
		return new Dao<Grupo>(Grupo.class).getAll();
	}
}
