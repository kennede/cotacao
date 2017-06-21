package br.com.moeda;

import java.math.BigDecimal;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryRounding;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

import org.javamoney.moneta.Money;
import org.javamoney.moneta.convert.ExchangeRateType;

public class CotacaoMoeda {

	public static void main(String[] args) {
/*		
		CurrencyUnit dolar = Monetary.getCurrency("USD");
		 
		CurrencyUnit real = Monetary.getCurrency("BRL");
		 
		MonetaryAmount valorDoProdutoDolar = Money.of(50, dolar);
		 
		MonetaryAmount taxaCorreios = Money.of(12, real);
		
		ExchangeRateProvider provider =   MonetaryConversions.getExchangeRateProvider(ExchangeRateType.ECB);
		
		CurrencyConversion conversionToBRL = provider.getCurrencyConversion(real);
		
		MonetaryAmount valorProdutoEmReal = conversionToBRL.apply(valorDoProdutoDolar);
		
		
		//analisar o código
		//MonetaryAmount valorTotal = valorProdutoEmReal.add(taxaCorreios);
		
		MonetaryRounding defaultRounding = Monetary.getDefaultRounding();
		 
		System.out.println("valor convertido: "+valorProdutoEmReal.with(defaultRounding));
		
		MonetaryAmountFormat germanyFormat = MonetaryFormats.getAmountFormat(Locale.GERMANY);
		 
		System.out.println("Formatar:"+ germanyFormat.format(valorDoProdutoDolar));
	*/
		
		CotacaoMoeda cotacaoMoeda = new CotacaoMoeda();
		cotacaoMoeda.cotacao("EUR", "BRL", new BigDecimal("10.00"));
		
	}
	
	
	public void cotacao(String moedaDe, String moedaPara, BigDecimal valor){
		
		CurrencyUnit de = Monetary.getCurrency(moedaDe);
		 
		CurrencyUnit para = Monetary.getCurrency(moedaPara);
		 
		MonetaryAmount valorDoProdutoDe = Money.of(valor, de);
		 	
		ExchangeRateProvider provider =   MonetaryConversions.getExchangeRateProvider(ExchangeRateType.ECB);
		
		CurrencyConversion conversionToPara = provider.getCurrencyConversion(para);
		
		MonetaryAmount valorProdutoEmPara = conversionToPara.apply(valorDoProdutoDe);
		
		MonetaryRounding defaultRounding = Monetary.getDefaultRounding();
		System.out.println("Resultado da conversão: "+moedaDe +" "+valor.toString()); 
		System.out.println("Resultado da conversão: "+valorProdutoEmPara.with(defaultRounding));
		
	}
}
