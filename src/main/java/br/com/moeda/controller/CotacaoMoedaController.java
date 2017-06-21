package br.com.moeda.controller;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.moeda.infra.FileSaver;
import br.com.moeda.model.ComboMoeda;
import br.com.moeda.model.Cotacao;
import br.com.moeda.service.ComboMoedaService;
import br.com.moeda.service.CotacaoService;
import br.com.moeda.util.CalculaCotacao;
import br.gov.bcb.pec.sgs.casosdeuso.ws.comum.WSSerieVO;
import br.gov.bcb.www3.wssgs.services.FachadaWSSGS.FachadaWSSGS;
import br.gov.bcb.www3.wssgs.services.FachadaWSSGS.FachadaWSSGSServiceLocator;

@Controller
public class CotacaoMoedaController {

	@Autowired
	CotacaoService service;
	@Autowired
	ComboMoedaService comboMoedaService;
	
	@Autowired
	CalculaCotacao calculaCotacao;

	@Autowired
	private FileSaver fileSaver;
	
	@RequestMapping("/")
	String index() {
		System.out.println("**********passou********");
		
		
		try {
			fileSaver.cargaComboMoeda("arquivos-cotacao");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Iterable<Cotacao> cotacoes = service.obterTodos();
		//List<ComboMoeda> cotacoes =  comboMoedaService.findMoedasAll();
		//Iterable<ComboMoeda> cotacoes =  comboMoedaService.findMoedasAll();
		System.out.println("********index*******" );
		return "index";
	}

	@RequestMapping("/resultado")
	public String resultado(Model model) {

		/*
		try {
			fileSaver.gravarArquivoCotacao("arquivos-cotacao");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
//		this.getExecutarServico();
		//Iterable<Cotacao> cotacoes = service.obterTodos();
		Iterable<ComboMoeda> cotacoes =  comboMoedaService.findMoedasAll();
		model.addAttribute("cotacoes", cotacoes);
		// model.addAttribute("cotacoes", cotacoes);
		System.out.println("*******cotacao.html*********");
		return "cotacao";
	}

	@RequestMapping(value = "cotar", method = RequestMethod.POST)
	public String cotar(Model model, Cotacao cotacao) throws Exception {
		//fileSaver.gravarArquivoCotacao("arquivos-cotacao");
		//fileSaver.gravarCotacao("arquivos-cotacao");
		System.out.println("Cotação: "+ cotacao.toString());
		
		try {
			model.addAttribute("resultado", calculaCotacao.cotacao(cotacao));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Não há cotação para o dia posterior");
		}
		//this.getExecutarServico();
		return this.resultado(model);
		//return "cotacao";
	}

	public void getExecutarServico() {

		// System.out.println("codigo: "+ codigo);
		// System.out.println("valor: "+ valor);
		// service.salvar(convidado);
		// service.getUltimoValorCotacao(moeda.getMoedaDe());
		// new EmailService().enviar(convidado.getNome(), convidado.getEmail());

		// org.apache.axis.EngineConfiguration config =
		// EngineConfigurationFactoryFinder.newFactory().getClientEngineConfig();

		// EngineConfigurationFactoryFinder finder = new Eng
		FachadaWSSGS fachada = null;
		WSSerieVO vo = null;
		FachadaWSSGSServiceLocator locator = new FachadaWSSGSServiceLocator();
		// FachadaWSSGSServiceLocator locator = new
		// FachadaWSSGSServiceLocator();
		
		try {
			fachada = locator.getFachadaWSSGS();
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//fachada = new FachadaWSSGSProxy();

		try {
			vo = fachada.getUltimoValorVO(21620);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		System.out.println("Serie:  " + vo.toString());
		System.out.println("Último Valor: " + vo.getUltimoValor().toString());

	}

	
	@ExceptionHandler(Exception.class)
	public String handleException(HttpServletRequest req, Exception exception, Model model){
	     model.addAttribute("errorMessage", "Favor inserir a data do dia ou anterior." );
	     return "error";
	}
}
