package br.com.moeda.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import br.com.moeda.model.Moeda;

@Repository
@Transactional
public class MoedaDao {

	@PersistenceContext
	private EntityManager manager;
	
	public void grava(Moeda moeda){
		manager.persist(moeda);
	}
	
	public void merge(Moeda moeda){
		manager.merge(moeda);
	}
	
	public Moeda findOne (Moeda moeda){
		List<Moeda> moedas = manager.createQuery("select m from Moeda m where m.sigla = :pSigla", Moeda.class).setParameter("pSigla", moeda.getSigla()).getResultList();
		if(moedas != null && moedas.size() > 0)
			return moedas.get(0);
		
		return new Moeda();
	}
	
	public Moeda getMoedaFiltros(Moeda moeda){
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Moeda> query = builder.createQuery(Moeda.class);
		Root<Moeda> from = query.from(Moeda.class);
		
		Predicate predicate = builder.and();
		
		if(moeda.getCodMoeda() != null && moeda.getCodMoeda().longValue() > 0){
			predicate = builder.and(predicate, builder.ge(from.get("cod_moeda"), moeda.getCodMoeda()));
		}
		
		if(moeda.getSigla() != null && !moeda.getSigla().trim().equals("")){
			predicate = builder.and(predicate, builder.like(from.<String>get("sigla"), moeda.getSigla()));
		}
		
		if(moeda.getData() != null && !moeda.getData().equals("")){
			predicate = builder.and(predicate, builder.equal(from.get("data"), moeda.getData()));
		}
		
		
		TypedQuery<Moeda> typedQuery = manager.createQuery(
			    query.select(from )
			    .where( predicate )
			    //.orderBy(builder.asc(from.get("sigla")))
			);
			List<Moeda> results = typedQuery.getResultList();
			if(results != null && results.size() > 0 )
				return results.get(0);
		
		return new Moeda();
	}

	
	public List<Moeda> buscarMoedaFiltros(Moeda moeda){
	
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Moeda> query = builder.createQuery(Moeda.class);
		Root<Moeda> from = query.from(Moeda.class);
		
		Predicate predicate = builder.and();
		
		if(moeda.getCodMoeda() != null && moeda.getCodMoeda().longValue() > 0){
			predicate = builder.and(predicate, builder.ge(from.get("cod_moeda"), moeda.getCodMoeda()));
		}
		
		if(moeda.getSigla() != null && !moeda.getSigla().trim().equals("")){
			predicate = builder.and(predicate, builder.like(from.<String>get("sigla"), moeda.getSigla()));
		}
		
		if(moeda.getData() != null && !moeda.getData().equals("")){
			predicate = builder.and(predicate, builder.equal(from.get("data"), moeda.getData()));
		}
		
		
		TypedQuery<Moeda> typedQuery = manager.createQuery(
			    query.select(from )
			    .where( predicate )
			    //.orderBy(builder.asc(from.get("sigla")))
			);
			List<Moeda> results = typedQuery.getResultList();
			if(results != null && results.size() > 0 )
				return results;
			
		return new ArrayList<Moeda>();	
	}
		
}
