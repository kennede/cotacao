package br.com.moeda.model;

import java.math.BigDecimal;

public class ResultadoCotacao {

	private String de;
	private String para;
	
	private BigDecimal converterValor;
	private String resultadoValor;
	private String dataCotacaoUtilizada;
	public String getDe() {
		return de;
	}
	public void setDe(String de) {
		this.de = de;
	}
	public String getPara() {
		return para;
	}
	public void setPara(String para) {
		this.para = para;
	}
	public String getResultadoValor() {
		return this.resultadoValor;
	}
	public void setResultadoValor(String resultadoValor) {
		this.resultadoValor = resultadoValor;
	}
	public String getDataCotacaoUtilizada() {
		return dataCotacaoUtilizada;
	}
	public void setDataCotacaoUtilizada(String dataCotacaoUtilizada) {
		this.dataCotacaoUtilizada = dataCotacaoUtilizada;
	}
	public BigDecimal getConverterValor() {
		return converterValor;
	}
	public void setConverterValor(BigDecimal converterValor) {
		this.converterValor = converterValor;
	}
	@Override
	public String toString() {
		return "ResultadoCotacao [de=" + de + ", para=" + para + ", converterValor=" + converterValor
				+ ", resultadoValor=" + resultadoValor + ", dataCotacaoUtilizada=" + dataCotacaoUtilizada + "]";
	}

	
	
}
