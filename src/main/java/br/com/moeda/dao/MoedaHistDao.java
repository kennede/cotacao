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

import br.com.moeda.model.MoedaHist;

@Repository
@Transactional
public class MoedaHistDao {

	@PersistenceContext
	private EntityManager manager;
	
	public void grava(MoedaHist moeda){
		manager.persist(moeda);
	}
	
	public void merge(MoedaHist moeda){
		manager.merge(moeda);
	}
	
	public MoedaHist findOne (MoedaHist moeda){
		List<MoedaHist> moedas = manager.createQuery("select m from moeda_hist m where m.sigla = :pSigla", MoedaHist.class).setParameter("pSigla", moeda.getSigla()).getResultList();
		if(moedas != null && moedas.size() > 0)
			return moedas.get(0);
		
		return new MoedaHist();
	}
	
	public MoedaHist getMoedaFiltros(MoedaHist moeda){
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<MoedaHist> query = builder.createQuery(MoedaHist.class);
		Root<MoedaHist> from = query.from(MoedaHist.class);
		
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
		
		
		TypedQuery<MoedaHist> typedQuery = manager.createQuery(
			    query.select(from )
			    .where( predicate )
			    //.orderBy(builder.asc(from.get("sigla")))
			);
			List<MoedaHist> results = typedQuery.getResultList();
			if(results != null && results.size() > 0 )
				return results.get(0);
		
		return new MoedaHist();
	}

	
	public List<MoedaHist> buscarMoedaFiltros(MoedaHist moeda){
	
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<MoedaHist> query = builder.createQuery(MoedaHist.class);
		Root<MoedaHist> from = query.from(MoedaHist.class);
		
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
		
		
		TypedQuery<MoedaHist> typedQuery = manager.createQuery(
			    query.select(from )
			    .where( predicate )
			    //.orderBy(builder.asc(from.get("sigla")))
			);
			List<MoedaHist> results = typedQuery.getResultList();
			if(results != null && results.size() > 0 )
				return results;
			
		return new ArrayList<MoedaHist>();	
	}
		
}
