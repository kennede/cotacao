package br.com.moeda.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.moeda.model.Cotacao;

public interface CotacaoRepository extends CrudRepository<Cotacao, Long>{

}
