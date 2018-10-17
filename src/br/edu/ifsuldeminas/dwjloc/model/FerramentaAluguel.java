package br.edu.ifsuldeminas.dwjloc.model;

import com.sun.javafx.beans.IDProperty;

import javax.persistence.*;
import java.util.Calendar;

@Entity
public class FerramentaAluguel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Calendar dataLocacao;
    @Temporal(TemporalType.DATE)
    private Calendar prazoDevolucao;
    @Temporal(TemporalType.DATE)
    private Calendar dataDevolucao;

    @ManyToOne
    private Ferramenta ferramenta;

    @ManyToOne
    private Usuario usuario;

    private Float valorDiario;

    private Float acrescimo;
    private Float desconto;

    private Boolean multa;

    private Boolean entregue;
    private Boolean pago;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Calendar getDataLocacao()
    {
        return dataLocacao;
    }

    public void setDataLocacao(Calendar dataLocacao)
    {
        this.dataLocacao = dataLocacao;
    }

    public Calendar getPrazoDevolucao()
    {
        return prazoDevolucao;
    }

    public void setPrazoDevolucao(Calendar prazoDevolucao)
    {
        this.prazoDevolucao = prazoDevolucao;
    }

    public Ferramenta getFerramenta()
    {
        return ferramenta;
    }

    public void setFerramenta(Ferramenta ferramenta)
    {
        this.ferramenta = ferramenta;
    }

    public Usuario getUsuario()
    {
        return usuario;
    }

    public void setUsuario(Usuario usuario)
    {
        this.usuario = usuario;
    }

    public Boolean getEntregue()
    {
        return entregue;
    }

    public void setEntregue(Boolean entregue)
    {
        this.entregue = entregue;
    }

    public Boolean getPago()
    {
        return pago;
    }

    public void setPago(Boolean pago)
    {
        this.pago = pago;
    }

    public Float getAcrescimo()
    {
        return acrescimo;
    }

    public void setAcrescimo(Float acrescimo)
    {
        this.acrescimo = acrescimo;
    }

    public Float getDesconto()
    {
        return desconto;
    }

    public void setDesconto(Float desconto)
    {
        this.desconto = desconto;
    }

    public Boolean getMulta()
    {
        return multa;
    }

    public void setMulta(Boolean multa)
    {
        this.multa = multa;
    }

    public Calendar getDataDevolucao()
    {
        return dataDevolucao;
    }

    public void setDataDevolucao(Calendar dataDevolucao)
    {
        this.dataDevolucao = dataDevolucao;
    }

    public Float getValorDiario()
    {
        return valorDiario;
    }

    public void setValorDiario(Float valorDiario)
    {
        this.valorDiario = valorDiario;
    }
}
