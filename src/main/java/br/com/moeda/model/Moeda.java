package br.com.moeda.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Moeda implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long codMoeda;
	private String tipo;
	private String nome;
	private String sigla;
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Calendar data;
	@Column(name = "taxaCompra",precision=19, scale=8)
	private BigDecimal taxaCompra;
	@Column(name = "taxaVenda",precision=19, scale=8)
	private BigDecimal taxaVenda;
	@Column(name = "paridadeCompra",precision=19, scale=8)
	private BigDecimal paridadeCompra;
	@Column(name = "paridadeVenda",precision=19, scale=8)
	private BigDecimal paridadeVenda;
	private BigDecimal valor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCodMoeda() {
		return codMoeda;
	}

	public void setCodMoeda(Long codMoeda) {
		this.codMoeda = codMoeda;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getTaxaCompra() {
		return taxaCompra;
	}

	public void setTaxaCompra(BigDecimal taxaCompra) {
		this.taxaCompra = taxaCompra;
	}

	public BigDecimal getTaxaVenda() {
		return taxaVenda;
	}

	public void setTaxaVenda(BigDecimal taxaVenda) {
		this.taxaVenda = taxaVenda;
	}

	public BigDecimal getParidadeCompra() {
		return paridadeCompra;
	}

	public void setParidadeCompra(BigDecimal paridadeCompra) {
		this.paridadeCompra = paridadeCompra;
	}

	public BigDecimal getParidadeVenda() {
		return paridadeVenda;
	}

	public void setParidadeVenda(BigDecimal paridadeVenda) {
		this.paridadeVenda = paridadeVenda;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Calendar getData() {
		return this.data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "Moeda [id=" + id + ", codMoeda=" + codMoeda + ", tipo=" + tipo + ", nome=" + nome + ", sigla=" + sigla
				+ ", dataCotacao=" + data + ", taxaCompra=" + taxaCompra + ", taxaVenda=" + taxaVenda
				+ ", paridadeCompra=" + paridadeCompra + ", paridadeVenda=" + paridadeVenda + ", valor=" + valor + "]";
	}

}
