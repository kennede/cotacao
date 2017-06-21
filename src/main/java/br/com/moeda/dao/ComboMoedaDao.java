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

import br.com.moeda.model.ComboMoeda;
import br.com.moeda.model.Moeda;

@Repository
@Transactional
public class ComboMoedaDao {

	@PersistenceContext
	private EntityManager manager;
	
	public void persist(ComboMoeda comboMoeda){
		manager.persist(comboMoeda);
	}
	
	public void merge(ComboMoeda comboMoeda){
		manager.merge(comboMoeda);
	}
	
	public ComboMoeda buscarMoedaPorSigla(String sigla){
		
	List<ComboMoeda> combos = manager.createQuery("select c from combo_moeda c where c.sigla = :pSigla", ComboMoeda.class)
		.setParameter("pSigla", sigla).getResultList();
	if(combos != null && combos.size() > 0)	
		return combos.get(0);
		
	return new ComboMoeda();
	}
	
	public List<ComboMoeda> findMoedasAll(){
		return manager.createQuery("select c from combo_moeda c order by c.nome", ComboMoeda.class).getResultList();
	}
	
	public List<ComboMoeda> buscarComboMoedaFiltros(ComboMoeda comboMoeda){
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ComboMoeda> query = builder.createQuery(ComboMoeda.class);
		Root<ComboMoeda> from = query.from(ComboMoeda.class);
		
		Predicate predicate = builder.and();
		
		if(comboMoeda.getCodigo() != null && comboMoeda.getCodigo().longValue() > 0){
			predicate = builder.and(predicate, builder.ge(from.get("codigo"), comboMoeda.getCodigo()));
		}
		
		if(comboMoeda.getSigla() != null && !comboMoeda.getSigla().trim().equals("")){
			predicate = builder.and(predicate, builder.like(from.<String>get("sigla"), comboMoeda.getSigla()));
		}
		
		if(comboMoeda.getData() != null && !comboMoeda.getData().equals("")){
			predicate = builder.and(predicate, builder.equal(from.get("data"), comboMoeda.getData()));
		}
		
		
		TypedQuery<ComboMoeda> typedQuery = manager.createQuery(
			    query.select(from )
			    .where( predicate )
			    //.orderBy(builder.asc(from.get("sigla")))
			);
			List<ComboMoeda> results = typedQuery.getResultList();
			if(results != null && results.size() > 0 )
				return results;
		
		return new ArrayList<ComboMoeda>();

		
	}
}
