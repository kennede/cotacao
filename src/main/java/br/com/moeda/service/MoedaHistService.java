package br.com.moeda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.moeda.dao.MoedaHistDao;
import br.com.moeda.model.Moeda;
import br.com.moeda.model.MoedaHist;
import br.com.moeda.repository.MoedaHistRepository;

@Service
public class MoedaHistService {

	@Autowired
	MoedaHistRepository moedaRepository;
	
	@Autowired
	MoedaHistDao moedaHistDao;
	
	
	public void salvar(Moeda moeda){
		moedaRepository.save(moeda);
	}
	
	
	public Moeda encontrarMoeda(Long id){
		return moedaRepository.findOne(id);
	}

	public void persist(MoedaHist moeda){
		moedaHistDao.grava(moeda);
	}
	
	public void merge(MoedaHist moeda){
		moedaHistDao.merge(moeda);
	}

	
	public MoedaHist findOne(MoedaHist moeda){
		return moedaHistDao.findOne(moeda);
	}

	public MoedaHist getMoedaFiltros(MoedaHist moeda){
		return moedaHistDao.getMoedaFiltros(moeda);
	}
	
	public List<MoedaHist> buscarMoedaFiltros(MoedaHist moeda){
		return moedaHistDao.buscarMoedaFiltros(moeda);
	}
}
