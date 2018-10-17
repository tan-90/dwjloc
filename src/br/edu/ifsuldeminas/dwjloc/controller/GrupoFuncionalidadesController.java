package br.edu.ifsuldeminas.dwjloc.controller;

import br.edu.ifsuldeminas.dwjloc.dao.Dao;
import br.edu.ifsuldeminas.dwjloc.lib.LibConstantes;
import br.edu.ifsuldeminas.dwjloc.model.Funcionalidade;
import br.edu.ifsuldeminas.dwjloc.model.Grupo;
import br.edu.ifsuldeminas.dwjloc.util.JPAUtil;
import br.edu.ifsuldeminas.dwjloc.dao.GrupoDao;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class GrupoFuncionalidadesController
{
    private Integer idGrupo;
    private Integer idFuncionalidade;

    public List<Funcionalidade> getAllFuncionalidades()
    {
        return new Dao<Funcionalidade>(Funcionalidade.class).getAll();
    }

    public List<Funcionalidade> getGrupoFuncionalidades()
    {
        if(idGrupo == null || idGrupo == 0)
        {
            return new ArrayList<>();
        }

        Grupo grupo = new Dao<Grupo>(Grupo.class).getById(idGrupo);

        return grupo.getFuncionalidades();
    }

    public void adicionarFuncionalidade()
    {
        if(!validar())
        {
            return;
        }

        EntityManager manager = JPAUtil.getEntityManager();
        manager.getTransaction().begin();

        try
        {
            Grupo grupo = new Dao<Grupo>(Grupo.class).getById(idGrupo, manager);
            Funcionalidade funcionalidade = new Funcionalidade();
            funcionalidade.setId(idFuncionalidade);

            grupo.getFuncionalidades().add(funcionalidade);
            manager.getTransaction().commit();
        }catch (Exception e)
        {
            manager.getTransaction().rollback();
        }finally
        {
            manager.close();
        }
    }

    public void remover(Funcionalidade funcionalidade)
    {
        EntityManager manager = JPAUtil.getEntityManager();
        manager.getTransaction().begin();

        try
        {
            Grupo grupo = new Dao<Grupo>(Grupo.class).getById(idGrupo, manager);

            funcionalidade = new Dao<Funcionalidade>(Funcionalidade.class).getById(funcionalidade.getId(), manager);

            grupo.getFuncionalidades().remove(funcionalidade);
            manager.getTransaction().commit();
        }catch (Exception e)
        {
            manager.getTransaction().rollback();
        }finally
        {
            manager.close();
        }
    }

    public boolean validar()
    {
        boolean ok = true;

        if(idGrupo == null || idGrupo == 0)
        {
            FacesContext.getCurrentInstance().addMessage("grupofuncionalidades", new FacesMessage("Grupo obrigatório."));

            ok = false;
        }
        if(idFuncionalidade == null || idFuncionalidade == 0)
        {
            FacesContext.getCurrentInstance().addMessage("grupofuncionalidades", new FacesMessage("Funcionalidade obrigatório."));

            ok = false;
        }

        return ok;
    }

    public List<Funcionalidade> getMissingFuncionalidades(Integer idGrupo)
    {
        return new GrupoDao().getMissingFuncionalidades(idGrupo);
    }

    public Integer getIdGrupo()
    {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo)
    {
        this.idGrupo = idGrupo;
    }

    public Integer getIdFuncionalidade()
    {
        return idFuncionalidade;
    }

    public void setIdFuncionalidade(Integer idFuncionalidade)
    {
        this.idFuncionalidade = idFuncionalidade;
    }

    public String removeDisabled(Integer id)
    {
        return (id == LibConstantes.Banco.ID_GRUPO_CLIENTES || id == LibConstantes.Banco.ID_GRUPO_ADMINISTRADORES) ? " disabled" : "";

    }

}
