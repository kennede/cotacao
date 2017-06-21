package br.com.moeda.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.moeda.model.Cotacao;
import de.jollyday.Holiday;
import de.jollyday.HolidayManager;

public class FeriadosDiasUteis {

	
	public static boolean verificaDiasUteis(Calendar calendar){
		int ano = calendar.get(Calendar.YEAR);
		int mes = calendar.get(Calendar.MONTH);
		int dia = calendar.get(Calendar.DAY_OF_MONTH);
		YearMonth anoMes = YearMonth.of(ano, mes+1);
		/*
		Stream<LocalDate> todosOsDiasDoMes = 
			    Stream.iterate(anoMes.atDay(1),  data -> data.plusDays(1))
			          .limit(anoMes.lengthOfMonth());
		*/
		List<LocalDate> listaDosDiasUteisDoMes =
			    Stream.iterate(anoMes.atDay(1),  data -> data.plusDays(1))
			                                .limit(anoMes.lengthOfMonth())
			        .filter(data -> !data.getDayOfWeek().equals(DayOfWeek.SATURDAY) && 
			                            !data.getDayOfWeek().equals(DayOfWeek.SUNDAY))
			        .collect(Collectors.toList());
		
		for (LocalDate localDate : listaDosDiasUteisDoMes) {
			
			int year  = localDate.getYear();
			int month = localDate.getMonthValue();
			int day   = localDate.getDayOfMonth();
			
			if(ano == year && mes+1 == month && dia == day ){
				return true;
			}
		}
		return false;
	}
	

	public static boolean verificarDiasUteis2(){
		
		int ano = 2017;
		int mes = 6;
		YearMonth anoMes = YearMonth.of(ano, mes);
		
		
		Stream<LocalDate> todosOsDiasDoMes = 
			    Stream.iterate(anoMes.atDay(1),  data -> data.plusDays(1))
			          .limit(anoMes.lengthOfMonth());
		
		Stream<LocalDate> diasUteisDoMes = 
			    todosOsDiasDoMes.filter(data -> 
			        !data.getDayOfWeek().equals(DayOfWeek.SATURDAY) && 
			        !data.getDayOfWeek().equals(DayOfWeek.SUNDAY));
		

		List<LocalDate> listaDosDiasUteisDoMes =
			    Stream.iterate(anoMes.atDay(1),  data -> data.plusDays(1))
			                                .limit(anoMes.lengthOfMonth())
			        .filter(data -> !data.getDayOfWeek().equals(DayOfWeek.SATURDAY) && 
			                            !data.getDayOfWeek().equals(DayOfWeek.SUNDAY))
			        .collect(Collectors.toList());
		
		System.out.println(" List Data: "+ listaDosDiasUteisDoMes.toString());
		
			for (LocalDate localDate : listaDosDiasUteisDoMes) {
				
				int year  = localDate.getYear();
				int month = localDate.getMonthValue();
				int day   = localDate.getDayOfMonth();
				
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, month, day);
				
				System.out.println("Data: "+calendar.getTime());
				
			}
			
			
		return true;
	}
	
	
	public static Boolean holiday(Calendar calendar){
		
		HolidayManager gerenciadorDeFeriados =
				  HolidayManager.getInstance(de.jollyday.HolidayCalendar.BRAZIL);
				//Set<Holiday> feriados = gerenciadorDeFeriados.getHolidays(new DateTime().getYear());
				Set<Holiday> feriados = gerenciadorDeFeriados.getHolidays(calendar.get(Calendar.YEAR));
				//Set<org.joda.time.LocalDate> dataDosFeriados = new HashSet<org.joda.time.LocalDate>();
				for (Holiday h : feriados) {
					System.out.println(h.getDate()+" tem feriado mês holiday: "+ h.getDate().getMonthOfYear() + " tem feriado mês calendar: "+ calendar.get(Calendar.MONTH));
					if(h.getDate().monthOfYear().get() - 1 == calendar.get(Calendar.MONTH)){
						System.out.println("tem feriado holiday: "+ h.getDate() + " tem feriado calendar: "+ calendar.getTime());
						if(h.getDate().getYear() == calendar.get(Calendar.YEAR) &&
						   h.getDate().getMonthOfYear() - 1 == calendar.get(Calendar.MONTH) &&
						   h.getDate().getDayOfMonth() == calendar.get(Calendar.DAY_OF_MONTH))
							return true;
					//dataDosFeriados.add(new org.joda.time.LocalDate(h.getDate().getYear(), h.getDate().getMonthOfYear(), h.getDate().getDayOfMonth(), ISOChronology.getInstance()));
					//dataDosFeriados.add(new org.joda.time.LocalDate(h.getDate(), ISOChronology.getInstance()));
					}
				}
				
		return false;
	}
	
	
	public static boolean equalsCalendar(){
		
		Object object = null;
		Calendar calendar = Calendar.getInstance();
		
		Calendar calendar2 = Calendar.getInstance();
		
		calendar.after(calendar2);
		
		return true;
	}
	
	public static Calendar DateToCalendar(Date date, boolean setTimeToZero){ 
	    Calendar calendario = Calendar.getInstance();
	    calendario.setTime(date);
	    if(setTimeToZero){
	        calendario.set(Calendar.HOUR_OF_DAY, 0);
	        calendario.set(Calendar.MINUTE, 0);
	        calendario.set(Calendar.SECOND, 0);
	        calendario.set(Calendar.MILLISECOND, 0);
	    }
	    return calendario;
	}
	
	public static Boolean verificaDiasSabadoDomingo(Calendar calendar){
		int ano = calendar.get(Calendar.YEAR);
		int mes = calendar.get(Calendar.MONTH);
		int dia = calendar.get(Calendar.DAY_OF_MONTH);
		YearMonth anoMes = YearMonth.of(ano, mes+1);
		/*
		Stream<LocalDate> todosOsDiasDoMes = 
			    Stream.iterate(anoMes.atDay(1),  data -> data.plusDays(1))
			          .limit(anoMes.lengthOfMonth());
		*/
		List<LocalDate> listaDosDiasSabado =
			    Stream.iterate(anoMes.atDay(1),  data -> data.plusDays(1))
			                                .limit(anoMes.lengthOfMonth())
			        //.filter(data -> !data.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !data.getDayOfWeek().equals(DayOfWeek.SUNDAY))
			                                .filter(data -> !data.getDayOfWeek().equals(DayOfWeek.MONDAY) && 
			                                				!data.getDayOfWeek().equals(DayOfWeek.TUESDAY) &&
			                                				!data.getDayOfWeek().equals(DayOfWeek.WEDNESDAY) &&
			                                				!data.getDayOfWeek().equals(DayOfWeek.THURSDAY) &&
			                                				!data.getDayOfWeek().equals(DayOfWeek.FRIDAY) && 
			                                				!data.getDayOfWeek().equals(DayOfWeek.SUNDAY))                      
			        .collect(Collectors.toList());
		
		List<LocalDate> listaDosDiasDomingo =
			    Stream.iterate(anoMes.atDay(1),  data -> data.plusDays(1))
			                                .limit(anoMes.lengthOfMonth())
			        //.filter(data -> !data.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !data.getDayOfWeek().equals(DayOfWeek.SUNDAY))
			                                .filter(data -> !data.getDayOfWeek().equals(DayOfWeek.MONDAY) && 
			                                				!data.getDayOfWeek().equals(DayOfWeek.TUESDAY) &&
			                                				!data.getDayOfWeek().equals(DayOfWeek.WEDNESDAY) &&
			                                				!data.getDayOfWeek().equals(DayOfWeek.THURSDAY) &&
			                                				!data.getDayOfWeek().equals(DayOfWeek.FRIDAY) && 
			                                				!data.getDayOfWeek().equals(DayOfWeek.SATURDAY))                      
			        .collect(Collectors.toList());
		
		for (LocalDate localDate : listaDosDiasSabado) {
			
			int year  = localDate.getYear();
			int month = localDate.getMonthValue();
			int day   = localDate.getDayOfMonth();
			
			if(ano == year && mes+1 == month && dia == day ){
				return false;
			}
		}
		
		for (LocalDate localDate : listaDosDiasDomingo) {
			
			int year  = localDate.getYear();
			int month = localDate.getMonthValue();
			int day   = localDate.getDayOfMonth();
			
			if(ano == year && mes+1 == month && dia == day ){
				return true;
			}
		}
		return null;
	}
	
	
	public static Calendar retornaDiaUtil(Calendar calendar){
		
		if(!FeriadosDiasUteis.verificaDiasUteis(calendar)){
			if(!FeriadosDiasUteis.verificaDiasSabadoDomingo(calendar).equals(null)){
				//Verifica Sabado
				if(FeriadosDiasUteis.verificaDiasSabadoDomingo(calendar).equals(Boolean.FALSE))
					calendar.add(Calendar.DAY_OF_MONTH, -1);
				//Verifica Domingo
				if(FeriadosDiasUteis.verificaDiasSabadoDomingo(calendar).equals(Boolean.TRUE))
					calendar.add(Calendar.DAY_OF_MONTH, -2);
				//Verifica Feriado
				if(FeriadosDiasUteis.holiday(calendar)){
					calendar.add(Calendar.DAY_OF_MONTH, -1);
					if(FeriadosDiasUteis.verificaDiasSabadoDomingo(calendar).equals(Boolean.TRUE))
						calendar.add(Calendar.DAY_OF_MONTH, -2);
				}	
			}
		}
		
		return calendar;
	}
	

	public static boolean equals(Calendar c1, Calendar c2){
		
		if(c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
		   c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
		   c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		
/*
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, 06, 16);
		
	boolean uteis = FeriadosDiasUteis.verificaDiasUteis(calendar);
		
		//////////Dias Úteis
		
	boolean feriados = FeriadosDiasUteis.holiday(calendar);
	System.out.println("Uteis: "+ uteis + " Feriados: "+feriados);
	*/

		/*
Calendar calendar = Calendar.getInstance();
		
calendar.set(2017, 05, 16);

		Calendar futuro = Calendar.getInstance();
		
		calendar.after(futuro);
		*/
		Cotacao cotacao = new Cotacao();
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		//System.out.println("Antes: "+ calendar.getTime());
		
		cotacao.setDataCotacao(calendar);
		System.out.println("Antes: "+ cotacao.getDataCotacao().getTime());
		cotacao.getDataCotacao().add(Calendar.DAY_OF_MONTH, -3);
		//calendar.add(Calendar.DAY_OF_MONTH, -2);
		//calendar.set(2017, Calendar.APRIL, 21);
		//date = calendar.getTime();
		
		
		
		//System.out.println("Date: "+ date.toString());
		
		System.out.println("Depois: "+ cotacao.getDataCotacao().getTime());
		
		//Boolean teste =  FeriadosDiasUteis.verificaDiasSabadoDomingo(calendar);
		
		//String s = teste == null ? "Não tem nada!!!" : "tem";
		
		//System.out.println(s);
		
		Boolean h =  FeriadosDiasUteis.holiday(calendar);
		
		String feriado = h == true ? "tem feriado!!!" : "não tem";
		
		System.out.println(feriado);
		
		/*
		Calendar dateUsuario = FeriadosDiasUteis.DateToCalendar(date, true);
		
		Calendar dateNow = FeriadosDiasUteis.DateToCalendar(new Date(), true);
		
		System.out.println("Passado: "+dateUsuario.getTime());
		System.out.println("Futuro: "+dateNow.getTime());
		
		System.out.println(dateUsuario.after(dateNow));
		*/
	}
}
