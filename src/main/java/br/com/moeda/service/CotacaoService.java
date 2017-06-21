package br.com.moeda.service;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.moeda.model.Cotacao;
import br.com.moeda.model.Moeda;
import br.com.moeda.repository.CotacaoRepository;
import br.gov.bcb.pec.sgs.casosdeuso.ws.comum.WSSerieVO;
import br.gov.bcb.www3.wssgs.services.FachadaWSSGS.FachadaWSSGS;
import br.gov.bcb.www3.wssgs.services.FachadaWSSGS.FachadaWSSGSServiceLocator;

@Service
public class CotacaoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	CotacaoRepository repository;

	public Iterable<Cotacao> obterTodos() {
		Iterable<Cotacao> cotacoes = repository.findAll();
		System.out.println("********obterTodos******** " + cotacoes.toString());
		return cotacoes;
	}

	public Moeda getUltimoValorCotacao(int codigo) {
		FachadaWSSGSServiceLocator locator = new FachadaWSSGSServiceLocator();
		FachadaWSSGS fachada = null;
		WSSerieVO vo = null;
		try {
			fachada = locator.getFachadaWSSGS();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		try {
			vo = fachada.getUltimoValorVO(codigo);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		System.out.println("Serie:  "+vo.toString());
		System.out.println("Último Valor: " +vo.getUltimoValor().toString());
		
		return new Moeda();
	}

}
