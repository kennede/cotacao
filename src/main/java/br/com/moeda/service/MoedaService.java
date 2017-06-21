package br.com.moeda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.moeda.dao.MoedaDao;
import br.com.moeda.model.Moeda;
import br.com.moeda.repository.MoedaRepository;

@Service
public class MoedaService {

	@Autowired
	MoedaRepository moedaRepository;
	
	@Autowired
	MoedaDao moedaDao;
	
	
	public void salvar(Moeda moeda){
		moedaRepository.save(moeda);
	}
	
	
	public Moeda encontrarMoeda(Long id){
		return moedaRepository.findOne(id);
	}

	public void persist(Moeda moeda){
		moedaDao.grava(moeda);
	}
	
	public void merge(Moeda moeda){
		moedaDao.merge(moeda);
	}

	
	public Moeda findOne(Moeda moeda){
		return moedaDao.findOne(moeda);
	}

	public Moeda getMoedaFiltros(Moeda moeda){
		return moedaDao.getMoedaFiltros(moeda);
	}
	
	public List<Moeda> buscarMoedaFiltros(Moeda moeda){
		return moedaDao.buscarMoedaFiltros(moeda);
	}
}
