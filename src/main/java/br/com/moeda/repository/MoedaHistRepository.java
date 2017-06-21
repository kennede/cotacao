package br.com.moeda.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.moeda.model.Moeda;

public interface MoedaHistRepository extends CrudRepository<Moeda, Long> {

}
