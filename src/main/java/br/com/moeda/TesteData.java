package br.com.moeda;

import java.sql.Date;
import java.util.Calendar;

public class TesteData {

	public static void main(String[] args) {
		
		
		
		String data = "30/05/2017";
		
		String dt[] = data.split("/");
		
		int dia = Integer.parseInt(dt[0]);
		int mes = Integer.parseInt(dt[1]);
		int ano = Integer.parseInt(dt[2]);
		
		Calendar calendario =  
			      new Calendar.Builder()  
			        .setDate(ano, mes-1, dia)  
			        .build();
		
		System.out.println(calendario);
		System.out.println(calendario.toString());
		
		
		
		Date date = new Date(calendario.getTimeInMillis());
		
		System.out.println(date);
		
	}
	
}
