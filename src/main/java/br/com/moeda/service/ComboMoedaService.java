package br.com.moeda.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.moeda.dao.ComboMoedaDao;
import br.com.moeda.model.ComboMoeda;

@Service
public class ComboMoedaService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	ComboMoedaDao comboMoedaDao;
	
	public ComboMoeda buscarMoedaPorSigla(String sigla){
		return comboMoedaDao.buscarMoedaPorSigla(sigla);
	}
	
	public void persist(ComboMoeda comboMoeda){
		comboMoedaDao.persist(comboMoeda);
	}
	
	public void merge(ComboMoeda comboMoeda){
		comboMoedaDao.merge(comboMoeda);
	}
	
	public List<ComboMoeda> findMoedasAll(){
		return comboMoedaDao.findMoedasAll();
	}
	
	public List<ComboMoeda> buscarComboMoedaFiltros(ComboMoeda comboMoeda){
		return comboMoedaDao.buscarComboMoedaFiltros(comboMoeda);
	}
}
	