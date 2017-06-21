package br.com.moeda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"br.com.moeda.*"})
@EntityScan(basePackages = "br.com.moeda.model")
//@ComponentScan(basePackages = {"br.gov.bcb.*"})
public class Configuracao {

	public static void main(String[] args) {
		
		SpringApplication.run(Configuracao.class, args);
	}
	
}
