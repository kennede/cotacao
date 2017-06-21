package br.com.moeda.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.moeda.model.Moeda;

public interface MoedaRepository extends CrudRepository<Moeda, Long> {

}
