package br.com.moeda.model;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Cotacao {

	@Id
	@GeneratedValue
	private Long id;
	private Integer codigo;
	private String nome;
	
	private String cotacaoDe;
	private String cotacaoPara;
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Calendar dataCotacao;
	
	private BigDecimal valorCotacao;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCotacaoDe() {
		return cotacaoDe;
	}
	public void setCotacaoDe(String cotacaoDe) {
		this.cotacaoDe = cotacaoDe;
	}
	public String getCotacaoPara() {
		return cotacaoPara;
	}
	public void setCotacaoPara(String cotacaoPara) {
		this.cotacaoPara = cotacaoPara;
	}
	public Calendar getDataCotacao() {
		return this.dataCotacao;
	}
	public void setDataCotacao(Calendar dataCotacao) {
		this.dataCotacao = dataCotacao;
	}
	public BigDecimal getValorCotacao() {
		return valorCotacao;
	}
	public void setValorCotacao(BigDecimal valorCotacao) {
		this.valorCotacao = valorCotacao;
	}
	@Override
	public String toString() {
		return "Cotacao [id=" + id + ", codigo=" + codigo + ", nome=" + nome + ", cotacaoDe=" + cotacaoDe
				+ ", cotacaoPara=" + cotacaoPara + ", dataCotacao=" + dataCotacao + ", valorCotacao=" + valorCotacao + "]";
	}

	
}
