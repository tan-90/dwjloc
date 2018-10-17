package br.edu.ifsuldeminas.dwjloc.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Ferramenta
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String codigo;
	
	private float preco;
	private float precoAluguel;
	
	@OneToOne(fetch=FetchType.EAGER)
	private TipoFerramenta tipo;
	@OneToOne(fetch=FetchType.EAGER)
	private EstadoFerramenta estado;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public TipoFerramenta getTipo() {
		return tipo;
	}
	public void setTipo(TipoFerramenta tipo) {
		this.tipo = tipo;
	}
	public EstadoFerramenta getEstado() {
		return estado;
	}
	public void setEstado(EstadoFerramenta estado) {
		this.estado = estado;
	}
	public float getPreco() {
		return preco;
	}
	public void setPreco(float preco) {
		this.preco = preco;
	}
	public float getPrecoAluguel() {
		return precoAluguel;
	}
	public void setPrecoAluguel(float precoAluguel) {
		this.precoAluguel = precoAluguel;
	}
	@Override
	public String toString() {
		return "Ferramenta [id=" + id + ", codigo=" + codigo + ", preco=" + preco + ", precoAluguel=" + precoAluguel
				+ ", tipo=" + tipo.getId() + ", estado=" + estado.getId() + "]";
	}
	
}
