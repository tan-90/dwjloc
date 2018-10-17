package br.edu.ifsuldeminas.dwjloc.controller;

import br.edu.ifsuldeminas.dwjloc.dao.Dao;
import br.edu.ifsuldeminas.dwjloc.dao.DaoFerramenta;
import br.edu.ifsuldeminas.dwjloc.dao.DaoFerramentaAluguel;
import br.edu.ifsuldeminas.dwjloc.lib.LibConstantes;
import br.edu.ifsuldeminas.dwjloc.model.*;
import br.edu.ifsuldeminas.dwjloc.util.JPAUtil;
import com.mysql.jdbc.util.TimezoneDump;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@ManagedBean
@ViewScoped
public class AluguelController
{
    private Integer idCliente;
    private Integer idTipo;
    private Integer quantidade;

    private Calendar dataLocacao = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));
    private Calendar dataDevolucao = Calendar.getInstance();

    public void adicionarLocacao()
    {
        if(!validar())
        {
            return;
        }

        // Lista de ferramentas disponíveis
        EntityManager manager = JPAUtil.getEntityManager();
        manager.getTransaction().begin();

        try
        {
            Usuario usuario = new Dao<Usuario>(Usuario.class).getById(idCliente, manager);

            TipoFerramenta tipo = new Dao<TipoFerramenta>(TipoFerramenta.class).getById(idTipo, manager);
            EstadoFerramenta estadoDisponivel = new Dao<EstadoFerramenta>(EstadoFerramenta.class).getById(LibConstantes.Banco.ID_ESTADO_DISPONIVEL, manager);
            EstadoFerramenta estadoAlugado = new Dao<EstadoFerramenta>(EstadoFerramenta.class).getById(LibConstantes.Banco.ID_ESTADO_ALUGADO, manager);

            List<Ferramenta> disponiveis = new DaoFerramenta().getByTipoAndEstado(tipo, estadoDisponivel, manager);

            List<Ferramenta> ferramentas = disponiveis.subList(0, quantidade);

            for (Ferramenta ferramenta : ferramentas)
            {
                FerramentaAluguel locacao = new FerramentaAluguel();
                locacao.setPago(false);
                locacao.setEntregue(false);
                dataLocacao.set(Calendar.HOUR_OF_DAY, 0);
                dataDevolucao.set(Calendar.HOUR_OF_DAY, 0);
                locacao.setDataLocacao(dataLocacao);
                locacao.setPrazoDevolucao(dataDevolucao);
                locacao.setFerramenta(ferramenta);
                locacao.setUsuario(usuario);

                locacao.setAcrescimo(0.0f);
                locacao.setDesconto(0.0f);

                locacao.setMulta(false);

                ferramenta.setEstado(estadoAlugado);

                locacao.setValorDiario(locacao.getFerramenta().getPrecoAluguel());

                new Dao<FerramentaAluguel>(FerramentaAluguel.class).add(locacao, manager);
            }

            manager.getTransaction().commit();
        }catch (Exception e)
        {
            FacesContext.getCurrentInstance().addMessage("aluguel", new FacesMessage("Falha na locação."));
            manager.getTransaction().rollback();
        }finally
        {
            manager.close();
        }
    }

    public boolean validar()
    {
        boolean ok = true;

        if(idCliente == null || idCliente == 0)
        {
            FacesContext.getCurrentInstance().addMessage("aluguel", new FacesMessage("Cliente obrigatório."));

            ok = false;
        }
        if(idTipo == null || idTipo == 0)
        {
            FacesContext.getCurrentInstance().addMessage("aluguel", new FacesMessage("Tipo obrigatório."));

            ok = false;
        }
        if(quantidade == null || quantidade == 0)
        {
            FacesContext.getCurrentInstance().addMessage("aluguel", new FacesMessage("Quantidade obrigatória."));

            ok = false;
        }
        if(dataLocacao.compareTo(dataDevolucao) > 0)
        {
            FacesContext.getCurrentInstance().addMessage("aluguel", new FacesMessage("A data de locação deve ser anterior ou igual à data de devolução."));

            ok = false;
        }
        return ok;
    }

    public void remover(FerramentaAluguel locacao)
    {
        EntityManager manager = JPAUtil.getEntityManager();
        manager.getTransaction().begin();
        try
        {
            EstadoFerramenta estadoDisponivel = new Dao<EstadoFerramenta>(EstadoFerramenta.class).getById(LibConstantes.Banco.ID_ESTADO_DISPONIVEL, manager);

            locacao = new Dao<FerramentaAluguel>(FerramentaAluguel.class).getById(locacao.getId(), manager);
            locacao.getFerramenta().setEstado(estadoDisponivel);

            new Dao<FerramentaAluguel>(FerramentaAluguel.class).remove(locacao, manager);

            manager.getTransaction().commit();
        }catch (Exception e)
        {
            manager.getTransaction().rollback();
        }finally
        {
            manager.close();
        }
    }

    public void entregar(FerramentaAluguel locacao)
    {
        EntityManager manager = JPAUtil.getEntityManager();
        manager.getTransaction().begin();

        Calendar dataAtual = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));
        dataAtual.set(Calendar.HOUR_OF_DAY, 0);

        try
        {
            locacao = new Dao<FerramentaAluguel>(FerramentaAluguel.class).getById(locacao.getId(), manager);

            locacao.setDataDevolucao(dataAtual);
            locacao.setAcrescimo(getAcrescimo(locacao));
            locacao.setDesconto(getDesconto(locacao));
            locacao.setEntregue(true);

            locacao.setFerramenta(new Dao<Ferramenta>(Ferramenta.class).getById(locacao.getFerramenta().getId(), manager));

            EstadoFerramenta estado = new EstadoFerramenta();
            estado.setId(locacao.getMulta() ? LibConstantes.Banco.ID_ESTADO_DANIFICADO : LibConstantes.Banco.ID_ESTADO_DISPONIVEL);
            locacao.getFerramenta().setEstado(estado);

            manager.getTransaction().commit();
        }catch (Exception e)
        {
            manager.getTransaction().rollback();
        }finally
        {
            manager.close();
        }
    }

    public void pagar(FerramentaAluguel locacao)
    {
        EntityManager manager = JPAUtil.getEntityManager();
        manager.getTransaction().begin();

        try
        {
            locacao = new Dao<FerramentaAluguel>(FerramentaAluguel.class).getById(locacao.getId(), manager);
            locacao.setPago(true);

            manager.getTransaction().commit();
        }catch (Exception e)
        {
            manager.getTransaction().rollback();
        }finally
        {
            manager.close();
        }
    }

    public Float getAcrescimo(FerramentaAluguel locacao)
    {
        if(locacao.getEntregue())
        {
            return locacao.getAcrescimo();
        }

        Calendar dataFinal = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));

        Calendar dataInicial = locacao.getDataLocacao();
        Calendar prazoDevolucao = locacao.getPrazoDevolucao();

        dataInicial.set(Calendar.HOUR_OF_DAY, 0);
        dataFinal.set(Calendar.HOUR_OF_DAY, 0);
        prazoDevolucao.set(Calendar.HOUR_OF_DAY, 0);

        Float acrescimo = 0.0f;

        if(dataFinal.compareTo(prazoDevolucao) > 0)
        {
            long atraso = (dataFinal.getTimeInMillis() - prazoDevolucao.getTimeInMillis()) / (24 * 60 * 60 * 1000);
            acrescimo += atraso * locacao.getValorDiario() * (1 + LibConstantes.Financeiro.ACRESCIMO_ATRASO);
        }

        if(locacao.getMulta())
        {
            acrescimo += locacao.getFerramenta().getPreco();
        }

        return acrescimo;
    }

    public Float getDesconto(FerramentaAluguel locacao)
    {
        if(locacao.getEntregue())
        {
           return locacao.getDesconto();
        }

        Calendar dataFinal = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));

        Calendar dataInicial = locacao.getDataLocacao();
        Calendar prazoDevolucao = locacao.getPrazoDevolucao();

        dataInicial.set(Calendar.HOUR_OF_DAY, 0);
        dataFinal.set(Calendar.HOUR_OF_DAY, 0);
        prazoDevolucao.set(Calendar.HOUR_OF_DAY, 0);

        Float desconto = 0.0f;

        if(dataFinal.compareTo(prazoDevolucao) < 0)
        {
            long adiantamento = (prazoDevolucao.getTimeInMillis() - dataFinal.getTimeInMillis()) / (24 * 60 * 60 * 1000);
            desconto += adiantamento * locacao.getValorDiario();
        }

        return desconto;
    }

    public Float getValorTotal(FerramentaAluguel locacao)
    {
        Calendar dataFinal;
        if(locacao.getEntregue())
        {
            dataFinal = locacao.getDataDevolucao();
        }else
        {
            dataFinal = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));
        }

        Calendar dataInicial = locacao.getDataLocacao();
        Calendar prazoDevolucao = locacao.getPrazoDevolucao();

        dataInicial.set(Calendar.HOUR_OF_DAY, 0);
        dataFinal.set(Calendar.HOUR_OF_DAY, 0);
        prazoDevolucao.set(Calendar.HOUR_OF_DAY, 0);

        Float valorAluguel = 0.0f;

        long tempoLocacao = (prazoDevolucao.getTimeInMillis() - dataInicial.getTimeInMillis()) / (24 * 60 * 60 * 1000);
        tempoLocacao = Math.max(tempoLocacao, 1);

        valorAluguel += tempoLocacao * locacao.getValorDiario();

        valorAluguel += getAcrescimo(locacao);
        valorAluguel -= getDesconto(locacao);

        return valorAluguel;
    }

    public List<Integer> getQuantidadeDisponivel()
    {
        if(idTipo == null)
        {
            return new ArrayList<>();
        }

        TipoFerramenta tipo = new Dao<TipoFerramenta>(TipoFerramenta.class).getById(idTipo);
        EstadoFerramenta estado = new Dao<EstadoFerramenta>(EstadoFerramenta.class).getById(LibConstantes.Banco.ID_ESTADO_DISPONIVEL);

        Integer quantidade = new DaoFerramenta().getByTipoAndEstado(tipo, estado).size();
        List<Integer> quantidades = new ArrayList<>(quantidade);
        for(int i = 1; i <= quantidade; i++)
        {
            quantidades.add(i);
        }
        return quantidades;
    }

    public List<FerramentaAluguel> getPendencias()
    {
        if(idCliente == null)
        {
            return new ArrayList<>();
        }

        return new DaoFerramentaAluguel().getPendencias(idCliente);
    }

    public List<FerramentaAluguel> getHistorico()
    {
        if(idCliente == null)
        {
            return new ArrayList<>();
        }

        return new DaoFerramentaAluguel().getHistorico(idCliente);
    }

    public void flipMulta(FerramentaAluguel locacao)
    {
        EntityManager manager = JPAUtil.getEntityManager();
        manager.getTransaction().begin();

        try
        {
            locacao = new Dao<FerramentaAluguel>(FerramentaAluguel.class).getById(locacao.getId(), manager);
            locacao.setMulta(!locacao.getMulta());
            manager.getTransaction().commit();
        }catch (Exception e)
        {
            manager.getTransaction().rollback();
        }finally
        {
            manager.close();
        }
    }

    public String getEntregueLabel(FerramentaAluguel locacao)
    {
        return locacao.getEntregue() ? "Entregue" : "Entregar";
    }

    public boolean getEntregueState(FerramentaAluguel locacao)
    {
        return locacao.getEntregue();
    }

    public String getPagoLabel(FerramentaAluguel locacao)
    {
        return locacao.getPago() ? "Pago" : "Pagar";
    }

    public boolean getPagoState(FerramentaAluguel locacao)
    {
        return locacao.getPago();
    }

    public Integer getIdCliente()
    {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente)
    {
        this.idCliente = idCliente;
    }

    public Integer getIdTipo()
    {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo)
    {
        this.idTipo = idTipo;
    }

    public Integer getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade)
    {
        this.quantidade = quantidade;
    }

    public Calendar getDataLocacao()
    {
        return dataLocacao;
    }

    public void setDataLocacao(Calendar dataLocacao)
    {
        this.dataLocacao = dataLocacao;
    }

    public Calendar getDataDevolucao()
    {
        return dataDevolucao;
    }

    public void setDataDevolucao(Calendar dataDevolucao)
    {
        this.dataDevolucao = dataDevolucao;
    }

    @Override public String toString()
    {
        return "AluguelController{" + "idCliente=" + idCliente + ", idTipo=" + idTipo + ", quantidade=" + quantidade + ", dataLocacao=" + dataLocacao.getTime() + ", dataDevolucao=" + dataDevolucao.getTime() + '}';
    }
}
