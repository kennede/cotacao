package br.com.moeda.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

@Entity(name="combo_moeda")
public class ComboMoeda implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer codigo;
	private String nome;
	private String sigla;
	private Integer codPais;
	private String pais;
	private String tipo;
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Calendar data; 
	
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
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public Integer getCodPais() {
		return codPais;
	}
	public void setCodPais(Integer codPais) {
		this.codPais = codPais;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public Calendar getData() {
		return data;
	}
	public void setData(Calendar data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ComboMoeda [id=" + id + ", codigo=" + codigo + ", nome=" + nome + ", sigla=" + sigla + ", codPais="
				+ codPais + ", pais=" + pais + ", tipo=" + tipo + "]";
	}
	
}
