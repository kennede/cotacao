package br.com.moeda.infra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.moeda.model.ComboMoeda;
import br.com.moeda.model.Moeda;
import br.com.moeda.model.MoedaHist;
import br.com.moeda.service.ComboMoedaService;
import br.com.moeda.service.MoedaHistService;
import br.com.moeda.service.MoedaService;
import br.com.moeda.util.CalculaCotacao;
import br.com.moeda.util.FeriadosDiasUteis;

@Component
public class FileSaver {

	@Autowired
	MoedaService mService;
	
	@Autowired
	MoedaHistService mHistService;
	
	@Autowired
	ComboMoedaService comboMoedaService;
	
	@Autowired
	private HttpServletRequest  request;
	
	public void gravarArquivoCotacao(String baseFolder, String data, boolean decisao) throws IOException {

		/*try {
			String realPath = request.getServletContext().getRealPath("/"+baseFolder);
	        String path = realPath + "/" + file.getOriginalFilename();
	        file.transferTo(new File(path));
	        return baseFolder + "/" + file.getOriginalFilename();
		} catch ( IOException e) {
			throw new RuntimeException(e);
		}*/
		
		//String servidor =  request.getServletContext().getRealPath("/"+baseFolder);
		String servidor =  request.getServletContext().getRealPath("/");
		
		URL	url = new URL("http://www4.bcb.gov.br/Download/fechamento/"+data+".csv");
			
		String composto = servidor + "\\"+data+".csv";
		InputStream is = url.openStream();
		
		FileOutputStream fos = new FileOutputStream(composto);
				 
		int bytes = 0;
		 
			while ((bytes = is.read()) != -1) {
			 fos.write(bytes);
			 }
		
			is.close();
			fos.close();
			
			is= null;
			fos = null;

			this.gravarCotacao(baseFolder, data, decisao);
	}
	
	private void gravarCotacao(String baseFolder, String data, boolean decisao){
		Scanner scan = null;
		//String servidor =  request.getServletContext().getRealPath("/"+baseFolder);
		String servidor =  request.getServletContext().getRealPath("/");
		String composto = servidor + "\\"+data+".csv";
		File arquivo = new File(composto);
		try( InputStream in = new FileInputStream(arquivo) ){
		   scan = new Scanner(in);
		  while( scan.hasNext() ){
			  if(scan.hasNextLine()){
			    String teste [] =  scan.nextLine().split(";");

			    if(decisao)
			    	this.salvar(teste);
			    else
			    	this.salvarHist(teste);
			    
			  }
		  }
		  
		  in.close();
		  scan.close();
		}catch(IOException ex){
		  ex.printStackTrace();
		}
	
	}
	
	private void salvar(String [] cotacao){
		Moeda md = null;
		Moeda moeda = new Moeda();
		
		String dt     = cotacao[0]; //Data
		String cMoeda = cotacao[1]; //codMoeda
		String tp     = cotacao[2]; //tipo
		String m 	  = cotacao[3]; //moeda
		String tc     = cotacao[4].replaceAll(",", "."); //taxaCompra
		String tv     = cotacao[5].replaceAll(",", "."); //taxaVenda
		String pc     = cotacao[6].replaceAll(",", "."); //paridadeCompra
		String pv     = cotacao[7].replaceAll(",", "."); //paridadeVenda
		
		moeda.setData(CalculaCotacao.tratarDataCalendar(dt.trim()));
		moeda.setCodMoeda(new Long(cMoeda.trim()));
		moeda.setTipo(tp.trim());
		moeda.setSigla(m.trim());
		moeda.setTaxaCompra(new BigDecimal(tc).setScale(6, RoundingMode.HALF_EVEN));
		moeda.setTaxaVenda(new BigDecimal(tv).setScale(6, RoundingMode.HALF_EVEN));
		moeda.setParidadeCompra(new BigDecimal(pc).setScale(6, RoundingMode.HALF_EVEN));
		moeda.setParidadeVenda(new BigDecimal(pv).setScale(6, RoundingMode.HALF_EVEN));
		
		ComboMoeda c = comboMoedaService.buscarMoedaPorSigla(m.trim());
		moeda.setNome(c.getNome());
		
		md = mService.findOne(moeda);
		if(md != null && md.getCodMoeda() != null && md.getCodMoeda().longValue() > 0 ){
			moeda.setId(md.getId());
			mService.merge(moeda);
			return;
		}
		mService.persist(moeda);
		
	}

	
	private void salvarHist(String [] cotacao){

		MoedaHist moeda = new MoedaHist();
		String dt     = cotacao[0]; //Data
		String cMoeda = cotacao[1]; //codMoeda
		String tp     = cotacao[2]; //tipo
		String m 	  = cotacao[3]; //moeda
		String tc     = cotacao[4].replaceAll(",", "."); //taxaCompra
		String tv     = cotacao[5].replaceAll(",", "."); //taxaVenda
		String pc     = cotacao[6].replaceAll(",", "."); //paridadeCompra
		String pv     = cotacao[7].replaceAll(",", "."); //paridadeVenda
		
		moeda.setData(CalculaCotacao.tratarDataCalendar(dt.trim()));
		moeda.setCodMoeda(new Long(cMoeda.trim()));
		moeda.setTipo(tp.trim());
		moeda.setSigla(m.trim());
		moeda.setTaxaCompra(new BigDecimal(tc).setScale(6, RoundingMode.HALF_EVEN));
		moeda.setTaxaVenda(new BigDecimal(tv).setScale(6, RoundingMode.HALF_EVEN));
		moeda.setParidadeCompra(new BigDecimal(pc).setScale(6, RoundingMode.HALF_EVEN));
		moeda.setParidadeVenda(new BigDecimal(pv).setScale(6, RoundingMode.HALF_EVEN));

		//md = mHistService.findOne(moeda);
		/*
		if(md != null && md.getCodMoeda() != null && md.getCodMoeda().longValue() > 0 ){
			moeda.setId(md.getId());
			mService.merge(moeda);
			return;
		}
		*/
		ComboMoeda c = comboMoedaService.buscarMoedaPorSigla(m.trim());
		moeda.setNome(c.getNome());
		mHistService.persist(moeda);
		
	}
	
	public void cargaComboMoeda(String baseFolder) throws IOException{
		
		ComboMoeda comboMoeda = new ComboMoeda();
		comboMoeda.setData(FeriadosDiasUteis.retornaDiaUtil(FeriadosDiasUteis.DateToCalendar(new Date(), true)));

		if(comboMoedaService.buscarComboMoedaFiltros(comboMoeda).isEmpty()){
			
			String ano = String.valueOf(comboMoeda.getData().get(Calendar.YEAR));
			String mes = String.valueOf(comboMoeda.getData().get(Calendar.MONTH)+1);
			String dia = String.valueOf(comboMoeda.getData().get(Calendar.DAY_OF_MONTH));
			
			mes = mes.length() == 1 ? "0"+mes: mes;
			
			String data = ano+mes+dia;
			
			//String servidor =  request.getServletContext().getRealPath("/"+baseFolder);
			String servidor =  request.getServletContext().getRealPath("/");
			
			URL	url = new URL("http://www4.bcb.gov.br/Download/fechamento/M"+data+".csv");
				
			String composto = servidor + "\\M"+data+".csv";
			InputStream is = url.openStream();
			
			FileOutputStream fos = new FileOutputStream(composto);
					 
			int bytes = 0;
			 
				while ((bytes = is.read()) != -1) {
				 fos.write(bytes);
				 }
			
				is.close();
				fos.close();
				is= null;
				fos = null;
				
				this.gravarComboMoeda(baseFolder, data);
		}
	}
	
	private void gravarComboMoeda(String baseFolder,String data){

		Scanner scan = null;
		//String servidor =  request.getServletContext().getRealPath("/"+baseFolder);
		String servidor =  request.getServletContext().getRealPath("/");
		String composto = servidor + "\\M"+data+".csv";
		File arquivo = new File(composto);
		try( InputStream in = new FileInputStream(arquivo) ){
		   scan = new Scanner(in);
		  while( scan.hasNext() ){
			  if(scan.hasNextLine()){
			    String teste [] =  scan.nextLine().split(";");
			    	this.salvarComboMoeda(teste);
			  }
		  }
		  
		  in.close();
		  scan.close();
		}catch(IOException ex){
		  ex.printStackTrace();
		}
	}
	
	private void salvarComboMoeda(String comboM[]){

		if(comboM.length > 6){
			return;
		}
		ComboMoeda comboMoeda = new ComboMoeda();
		Integer codigo = Integer.valueOf(comboM[0]);
		String nome = comboM[1];
		String sigla = comboM[2];
		Integer codPais = comboM[3].trim().equals("") ? 0 :  Integer.valueOf(comboM[3]);
		String pais = comboM[4];
		String tipo = comboM[5];
		
		comboMoeda.setCodigo(codigo);
		comboMoeda.setNome(nome.trim());
		comboMoeda.setSigla(sigla.trim());
		comboMoeda.setCodPais(codPais);
		comboMoeda.setPais(pais.trim());
		comboMoeda.setTipo(tipo.trim());
		comboMoeda.setData(FeriadosDiasUteis.retornaDiaUtil(FeriadosDiasUteis.DateToCalendar(new Date(), true)));

		ComboMoeda c = comboMoedaService.buscarMoedaPorSigla(sigla);
		if(c != null && c.getCodigo() != null && c.getCodigo().intValue() > 0){
			comboMoeda.setId(c.getId());
			comboMoedaService.merge(comboMoeda);
			this.atualizarNomeMoeda(comboMoeda);
			return;
		}
		comboMoedaService.persist(comboMoeda);
	}
	
	private void atualizarNomeMoeda(ComboMoeda comboMoeda){
		Moeda moeda = new Moeda();
		moeda.setSigla(comboMoeda.getSigla());
		Moeda m = mService.findOne(moeda);
		if(m != null && m.getId() != null && m.getId().intValue() > 0 && (m.getNome() == null || m.getNome().equals(""))){
			m.setNome(comboMoeda.getNome());
			mService.merge(m);
		}
	}
}
