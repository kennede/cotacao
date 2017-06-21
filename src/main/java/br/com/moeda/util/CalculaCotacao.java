package br.com.moeda.util;


import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.moeda.infra.FileSaver;
import br.com.moeda.model.Cotacao;
import br.com.moeda.model.Moeda;
import br.com.moeda.model.MoedaHist;
import br.com.moeda.model.ResultadoCotacao;
import br.com.moeda.service.MoedaHistService;
import br.com.moeda.service.MoedaService;

@Service
public class CalculaCotacao {

	@Autowired
	MoedaService moedaService;
	
	@Autowired
	MoedaHistService moedaHistService;
	
	@Autowired
	FileSaver fileSaver;
	
	public static java.sql.Date tratarData(String data){

		String dt[] = data.split("/");
		int dia = Integer.parseInt(dt[0]);
		int mes = Integer.parseInt(dt[1]);
		int ano = Integer.parseInt(dt[2]);

		Calendar calendario =  
			      new Calendar.Builder()  
			        .setDate(ano, mes-1, dia)  
			        .build();
		
		java.sql.Date date = new java.sql.Date(calendario.getTimeInMillis());
		return date;
	}
	
	public static Calendar tratarDataCalendar(String data){

		String dt[] = data.split("/");
		int dia = Integer.parseInt(dt[0]);
		int mes = Integer.parseInt(dt[1]);
		int ano = Integer.parseInt(dt[2]);

		Calendar calendario =  
			      new Calendar.Builder()  
			        .setDate(ano, mes-1, dia)  
			        .build();
		
		
		return calendario;
	}

	
	public Iterable<ResultadoCotacao> cotacao(Cotacao cotacao) throws Exception{

		if(cotacao.getDataCotacao().after(FeriadosDiasUteis.DateToCalendar(new Date(), true)))
			throw new Exception("Não há cotação para o dia posterior");
		//Setando data para um dia útil
		cotacao.setDataCotacao(FeriadosDiasUteis.retornaDiaUtil(cotacao.getDataCotacao()));

		List<ResultadoCotacao> resultados = new ArrayList<>();
		Moeda moeda = new Moeda();
		moeda.setData(cotacao.getDataCotacao());
		
		String ano = String.valueOf(moeda.getData().get(Calendar.YEAR));
		String mes = String.valueOf(moeda.getData().get(Calendar.MONTH)+1);
		String dia = String.valueOf(moeda.getData().get(Calendar.DAY_OF_MONTH));
		
		mes = mes.length() == 1 ? "0"+mes: mes;
		String data = ano+mes+dia;
		
		//Cotação Atual
		if(FeriadosDiasUteis.equals(FeriadosDiasUteis.DateToCalendar(new Date(), true), cotacao.getDataCotacao())){
			if(moedaService.buscarMoedaFiltros(moeda).isEmpty())
				fileSaver.gravarArquivoCotacao("arquivos-cotacao", data, true);
			
			if(cotacao.getCotacaoDe().equals("USD")){
				 resultados.add(this.cotacaoDolarDe(cotacao));
				 return resultados;
			}
			
			if(!cotacao.getCotacaoDe().equals("USD")){
				resultados.add(this.cotacaoDiferentesDe(cotacao));
				return resultados;
			}

		}	
		
		//Cotação Histórico
		if(cotacao.getDataCotacao().before(FeriadosDiasUteis.DateToCalendar(new Date(), true))){
			MoedaHist moedaHist = new MoedaHist();
			moedaHist.setData(cotacao.getDataCotacao());
			if(moedaHistService.buscarMoedaFiltros(moedaHist).isEmpty())
				 fileSaver.gravarArquivoCotacao("arquivos-cotacao", data, false);
			 
			if(cotacao.getCotacaoDe().equals("USD")){
				 resultados.add(this.cotacaoDolarDeHist(cotacao));
				 return resultados;
			}
			
			if(!cotacao.getCotacaoDe().equals("USD")){
				resultados.add(this.cotacaoDiferentesDeHist(cotacao));
				return resultados;
			}

		}
		
		
				
		return new ArrayList<ResultadoCotacao>();
	}
	
	
	private ResultadoCotacao cotacaoDolarDe(Cotacao cotacao){

		ResultadoCotacao resultadoCotacao = new ResultadoCotacao();
		BigDecimal resultado = null;
		Moeda mDe = new Moeda();
		mDe.setSigla(cotacao.getCotacaoDe());
		
		Moeda mPara = new Moeda();
		mPara.setSigla(cotacao.getCotacaoPara());
		Moeda moedaDe = null;
		Moeda moedaPara = null;
		if(cotacao.getCotacaoDe().equals("USD")){
			//mPara
			moedaDe = moedaService.getMoedaFiltros(mDe);
			moedaPara = moedaService.getMoedaFiltros(mPara);
			if(moedaPara != null && moedaPara.getTipo().equals("A")){
				
				if(cotacao.getCotacaoPara().equals("BRL"))
					resultado = cotacao.getValorCotacao().divide(moedaDe.getTaxaCompra(),MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				else	
					resultado = cotacao.getValorCotacao().multiply(moedaPara.getParidadeCompra());
				
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
			
			if(moedaPara != null && moedaPara.getTipo().equals("B")){
				mDe.setSigla(cotacao.getCotacaoDe());
				moedaDe = moedaService.getMoedaFiltros(mDe);
				resultado = cotacao.getValorCotacao().multiply(moedaDe.getTaxaCompra());

				resultado = resultado.divide(moedaPara.getTaxaCompra(),MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
		}
		return new ResultadoCotacao();
	}
	
	private ResultadoCotacao cotacaoDiferentesDe(Cotacao cotacao){

		ResultadoCotacao resultadoCotacao = new ResultadoCotacao();
		BigDecimal resultado = null;
		//BigDecimal resultado1 = null;
		//BigDecimal resultado2 = null;
		Moeda mDe = new Moeda();
		mDe.setSigla(cotacao.getCotacaoDe());
		
		Moeda mPara = new Moeda();
		mPara.setSigla(cotacao.getCotacaoPara());
		Moeda moedaDe = null;
		Moeda moedaPara = null;
		
		moedaDe = moedaService.getMoedaFiltros(mDe);
		moedaPara = moedaService.getMoedaFiltros(mPara);
		
		//Moeda de Cambio
		Moeda mCambio = new Moeda();
		mCambio.setSigla("USD");
		Moeda moedaCambio = moedaService.getMoedaFiltros(mCambio);
		
		if(moedaDe.getTipo().trim().equals("A")){
			
			//Calcular Moeda Nacional Brasileira
			if((moedaDe.getSigla().trim().equals("BRL") && moedaPara.getSigla().equals("USD")) || (moedaDe.getSigla().trim().equals("BRL") && moedaPara.getTipo().equals("B"))  ){
				resultado = cotacao.getValorCotacao().divide(moedaPara.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
	
			//Calcular Moeda Nacional Brasileira
			if((moedaDe.getSigla().trim().equals("BRL") && !moedaPara.getSigla().equals("USD") && moedaPara.getTipo().trim().equals("A")) ){
				resultado = cotacao.getValorCotacao().divide(moedaCambio.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultado = resultado.multiply(moedaPara.getParidadeCompra());
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
	
			
			//Moedas "De" do tipo A para Tipo B "Para"
			if((!moedaDe.getSigla().trim().equals("BRL") && moedaPara.getSigla().trim().equals("USD")) || (!moedaDe.getSigla().trim().equals("BRL") && moedaPara.getTipo().trim().equals("B")) ){
				resultado = cotacao.getValorCotacao().multiply(moedaCambio.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultado = resultado.divide(moedaPara.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultado = resultado.divide(moedaDe.getParidadeCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
			
			if(!moedaPara.getSigla().trim().equals("USD") || !moedaPara.getTipo().trim().equals("B") ){
				resultado = cotacao.getValorCotacao().divide(moedaDe.getParidadeCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultado = resultado.multiply(moedaPara.getParidadeCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
		
		}
		
		if(moedaDe.getTipo().trim().equals("B")){
			
			if(moedaPara.getTipo().trim().equals("B") || moedaPara.getSigla().trim().equals("USD")){
				resultado = cotacao.getValorCotacao().multiply(moedaDe.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultado = resultado.divide(moedaPara.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
			
			if(moedaPara.getSigla().trim().equals("BRL")){
				resultado = cotacao.getValorCotacao().multiply(moedaDe.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
			
			resultado = cotacao.getValorCotacao().multiply(moedaDe.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
			resultado = resultado.divide(moedaCambio.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
			resultado = resultado.multiply(moedaPara.getParidadeCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
			resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
			resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
			resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
			resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
			
			return resultadoCotacao;
			
		}
		return new ResultadoCotacao();
	}

	public static String converterValorMoeda(BigDecimal decimal){
		//DecimalFormat formatBig = new java.text.DecimalFormat("¤ #,###,##0.00");
		DecimalFormat formatBig = new java.text.DecimalFormat("#,###,##0.00");
		String valor = formatBig.format(decimal);
		System.out.println("Valor: "+ valor);
		
		return valor;
	}
	
	private ResultadoCotacao cotacaoDolarDeHist(Cotacao cotacao){

		ResultadoCotacao resultadoCotacao = new ResultadoCotacao();
		BigDecimal resultado = null;
		MoedaHist mDe = new MoedaHist();
		mDe.setSigla(cotacao.getCotacaoDe());
		mDe.setData(cotacao.getDataCotacao());
		
		MoedaHist mPara = new MoedaHist();
		mPara.setSigla(cotacao.getCotacaoPara());
		mPara.setData(cotacao.getDataCotacao());
		
		MoedaHist moedaDe = null;
		MoedaHist moedaPara = null;
		if(cotacao.getCotacaoDe().equals("USD")){
			//mPara
			moedaDe = moedaHistService.getMoedaFiltros(mDe);
			moedaPara = moedaHistService.getMoedaFiltros(mPara);
			if(moedaPara != null && moedaPara.getTipo().equals("A")){
				
				if(cotacao.getCotacaoPara().equals("BRL"))
					resultado = cotacao.getValorCotacao().divide(moedaDe.getTaxaCompra(),MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				else	
					resultado = cotacao.getValorCotacao().multiply(moedaPara.getParidadeCompra());
				
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
			
			if(moedaPara != null && moedaPara.getTipo().equals("B")){
				mDe.setSigla(cotacao.getCotacaoDe());
				moedaDe = moedaHistService.getMoedaFiltros(mDe);
				resultado = cotacao.getValorCotacao().multiply(moedaDe.getTaxaCompra());

				resultado = resultado.divide(moedaPara.getTaxaCompra(),MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
		}
		return new ResultadoCotacao();
	}
	
	private ResultadoCotacao cotacaoDiferentesDeHist(Cotacao cotacao){

		ResultadoCotacao resultadoCotacao = new ResultadoCotacao();
		BigDecimal resultado = null;
		//BigDecimal resultado1 = null;
		//BigDecimal resultado2 = null;
		MoedaHist mDe = new MoedaHist();
		mDe.setSigla(cotacao.getCotacaoDe());
		mDe.setData(cotacao.getDataCotacao());
		
		MoedaHist mPara = new MoedaHist();
		mPara.setSigla(cotacao.getCotacaoPara());
		mPara.setData(cotacao.getDataCotacao());
		
		MoedaHist moedaDe = null;
		MoedaHist moedaPara = null;
		
		moedaDe = moedaHistService.getMoedaFiltros(mDe);
		moedaPara = moedaHistService.getMoedaFiltros(mPara);
		
		//Moeda de Cambio
		MoedaHist mCambio = new MoedaHist();
		mCambio.setSigla("USD");
		mCambio.setData(cotacao.getDataCotacao());
		MoedaHist moedaCambio = moedaHistService.getMoedaFiltros(mCambio);
		
		if(moedaDe.getTipo().trim().equals("A")){
			
			//Calcular Moeda Nacional Brasileira
			if((moedaDe.getSigla().trim().equals("BRL") && moedaPara.getSigla().equals("USD")) || (moedaDe.getSigla().trim().equals("BRL") && moedaPara.getTipo().equals("B"))  ){
				resultado = cotacao.getValorCotacao().divide(moedaPara.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
	
			//Calcular Moeda Nacional Brasileira
			if((moedaDe.getSigla().trim().equals("BRL") && !moedaPara.getSigla().equals("USD") && moedaPara.getTipo().trim().equals("A")) ){
				resultado = cotacao.getValorCotacao().divide(moedaCambio.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultado = resultado.multiply(moedaPara.getParidadeCompra());
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
	
			
			//Moedas "De" do tipo A para Tipo B "Para"
			if((!moedaDe.getSigla().trim().equals("BRL") && moedaPara.getSigla().trim().equals("USD")) || (!moedaDe.getSigla().trim().equals("BRL") && moedaPara.getTipo().trim().equals("B")) ){
				resultado = cotacao.getValorCotacao().multiply(moedaCambio.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultado = resultado.divide(moedaPara.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultado = resultado.divide(moedaDe.getParidadeCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
			
			if(!moedaPara.getSigla().trim().equals("USD") || !moedaPara.getTipo().trim().equals("B") ){
				resultado = cotacao.getValorCotacao().divide(moedaDe.getParidadeCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultado = resultado.multiply(moedaPara.getParidadeCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
		
		}
		
		if(moedaDe.getTipo().trim().equals("B")){
			
			if(moedaPara.getTipo().trim().equals("B") || moedaPara.getSigla().trim().equals("USD")){
				resultado = cotacao.getValorCotacao().multiply(moedaDe.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultado = resultado.divide(moedaPara.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
			
			if(moedaPara.getSigla().trim().equals("BRL")){
				resultado = cotacao.getValorCotacao().multiply(moedaDe.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
				
				resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
				resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
				resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
				resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
				return resultadoCotacao;
			}
			
			resultado = cotacao.getValorCotacao().multiply(moedaDe.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
			resultado = resultado.divide(moedaCambio.getTaxaCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
			resultado = resultado.multiply(moedaPara.getParidadeCompra(), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_EVEN);
			resultadoCotacao.setDe(moedaDe.getNome() +"/"+ moedaDe.getSigla() + " ("+moedaDe.getCodMoeda()+")" );
			resultadoCotacao.setPara(moedaPara.getNome() +"/"+ moedaPara.getSigla() + " ("+moedaPara.getCodMoeda()+")" );
			resultadoCotacao.setConverterValor(cotacao.getValorCotacao());
			resultadoCotacao.setResultadoValor(CalculaCotacao.converterValorMoeda(resultado));
			
			return resultadoCotacao;
			
		}
		return new ResultadoCotacao();
	}


}
