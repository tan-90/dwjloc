package br.edu.ifsuldeminas.dwjloc.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.edu.ifsuldeminas.dwjloc.dao.UsuarioDao;
import br.edu.ifsuldeminas.dwjloc.model.Funcionalidade;
import br.edu.ifsuldeminas.dwjloc.model.Usuario;
import br.edu.ifsuldeminas.dwjloc.util.Utils;

@ManagedBean
@ViewScoped
public class LoginController {
	private Usuario usuario = new Usuario();
			
	public String efetuarLogin(){
		usuario.setSenha(Utils.toMD5(usuario.getSenha()));
		usuario = new UsuarioDao().buscarPorLoginESenha(usuario);

		FacesContext context = FacesContext.getCurrentInstance();
		if(usuario != null)
		{
			context.getExternalContext().getSessionMap().put("usuarioLogado", usuario);
			
			// funcionalidades			
			for(Funcionalidade f : usuario.getGrupo().getFuncionalidades())
			{
				context.getExternalContext().getSessionMap().put(f.getPagina(), f);
			}
			
			return "aluguel?faces-redirect=true";
		}else
		{
			context.getExternalContext().getFlash().setKeepMessages(true);
			context.addMessage(null, new FacesMessage("Usuário ou senha inválidos."));
			return "/login?faces-redirect=true";
		}
	}
	
	public String deslogar()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().clear();
		return "/login?faces-redirect=true";		
	}
	
	// getters e setters
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioLogado()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		return (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogado");
	}

	public String getAdmDivVisibility()
	{
		if(getUsuario() == null)
		{
			return "visible";
		}
		boolean adm = new UsuarioDao().isAdm(getUsuarioLogado());

		return adm ? "visible" : "hidden";
	}

	public String renderLogout()
	{
		return getUsuarioLogado() != null ? "true" : "false";
	}

	public String renderLogin()
	{
		return getUsuarioLogado() == null ? "visibility: visible;" : "visibility: hidden;";
	}
}
