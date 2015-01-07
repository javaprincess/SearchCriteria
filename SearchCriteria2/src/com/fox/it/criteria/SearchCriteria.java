package com.fox.it.criteria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;


public class SearchCriteria<T> {
	private static final Logger logger = Logger.getLogger(SearchCriteria.class.getName());
	private final CriteriaBuilder builder;
	private final CriteriaQuery<T> criteria;
	private final Root<T> from;
	private final EntityManager em;
	private List<Predicate> predicates = new ArrayList<Predicate>();
	private List<Order> sort = new ArrayList<Order>();
	private Integer maxResults;	
	private final Class<T> clazz;
	
	private String nativeSQL = null;
	private Map<String,Object> parameters = null;
	
	
	
	private final PredicateBuilder<T> predicateBuilder;
	
	public SearchCriteria(EntityManager em,Class<T> clazz) {
		this.em = em;
		this.builder = em.getCriteriaBuilder();
		this.criteria = builder.createQuery(clazz);
		this.clazz = clazz;
		this.from = criteria.from(clazz);
		this.predicateBuilder = new PredicateBuilder<T>(from, builder);
	}
	
	public static <T> SearchCriteria<T> get(EntityManager em, Class<T> clazz) {
		return new SearchCriteria<T>(em, clazz);
	}
	
	public boolean isNativeSQL() {
		return nativeSQL!=null;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public SearchCriteria<T> setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	public CriteriaQuery<T> getCriteria() {
		return criteria;
	}
	
	
	public Root<T> from() {
		return from;
	}
	public CriteriaBuilder builder() {
		return builder;
	}
	
	public PredicateBuilder<T> getPredicateBulder() {
		return predicateBuilder;
	}
	
	public void add(Predicate predicate) {
		predicates.add(predicate);
	}
	
	public SearchCriteria<T> like(String fieldName, String value) {
		Predicate predicate = getPredicateBulder().like(fieldName, value);
		add(predicate);
		return this;
	}
	
	public SearchCriteria<T> equal(String fieldName, Object value) {
		Predicate predicate = getPredicateBulder().equal(fieldName, value);
		add(predicate);
		return this;
	}
	
	public SearchCriteria<T> notEqual(String fieldName,Object value) {
		Predicate predicate = getPredicateBulder().ne(fieldName, value);
		add(predicate);
		return this;		
	}
	
	public SearchCriteria<T> equalUpper(String fieldName, String value) {
		Predicate predicate = getPredicateBulder().equalUpper(fieldName, value);
		add(predicate);
		return this;
	}
	
	public SearchCriteria<T> equalLower(String fieldName, String value) {
		Predicate predicate = getPredicateBulder().equalLower(fieldName, value);
		add(predicate);
		return this;
	}
	
	public SearchCriteria<T> le(String fieldName, Date value) {
		Predicate predicate = getPredicateBulder().le(fieldName, value);
		add(predicate);
		return this;
		
	}
	
	
	public SearchCriteria<T> ge(String fieldName, Integer value) {
		Predicate predicate = getPredicateBulder().ge(fieldName, value);
		add(predicate);
		return this;
		
	}
	
 
	
	public SearchCriteria<T> likeUpper(String fieldName, String value) {
		Predicate predicate = getPredicateBulder().likeUpper(fieldName, value);
		add(predicate);
		return this;
	}
	
	public SearchCriteria<T> likeLower(String fieldName, String value) {
		Predicate predicate = getPredicateBulder().likeLower(fieldName, value);
		add(predicate);
		return this;
	}
	
	public SearchCriteria<T> in(String fieldName, List<? extends Object> values){
		Predicate predicate = getPredicateBulder().in(fieldName, values);
		add(predicate);
		return this;
	}
	
	public SearchCriteria<T> isNotNull(String fieldName) {
		Predicate predicate = getPredicateBulder().isNotNull(fieldName);
		add(predicate);
		return this;		
	}
	
	public SearchCriteria<T> isNull(String fieldName) {
		Predicate predicate = getPredicateBulder().isNull(fieldName);
		add(predicate);
		return this;		
	}
	
	
	public T getSingleResult() {
		try {
			setPredicates(criteria,predicates);			
			TypedQuery<T> query  = em.createQuery(criteria);
			setHints(query);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public void addSort(String fieldName,boolean asc) {
		Order order = null;
		if (asc) {
			order = builder.asc(from.get(fieldName));
		} else {
			order = builder.desc(from.get(fieldName));
		}
		sort.add(order);
		
	}
	
	public void addSort(String fieldName) {
		addSort(fieldName,true);
	}
	
	
	/**
	 * Allows sublcasses to provide their hints
	 * Default implementation is empty
	 */
	protected void setHints(TypedQuery<T> query) {
		setNoCacheHints(query);
	}
	
	
	private void setSort() {
		if (isNativeSQL()) return;
		if (sort.size()>0) {
			criteria.orderBy(sort);
		}
	}
	
	
	private void bindParameters(Query q,Map<String,Object> parameters) {
		if (parameters==null||parameters.size()==0) return;
		for (String name:parameters.keySet()) {
			Object value = parameters.get(name);
			q.setParameter(name, value);
		}
	}
	
	private Query getNativeQuery() {
		logger.info("Creating native query " + nativeSQL);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		TypedQuery<T> query = (TypedQuery)em.createNativeQuery(nativeSQL, clazz);
		bindParameters(query, parameters);
		return query;
	}
	
	public void setNativeSQL(String sql, Map<String,Object> params) {
		this.nativeSQL = sql;
		this.parameters = params;
	}
	
	
	private void setNoCacheHints(Query query) {
		query.setHint(QueryHints.CACHE_USAGE, CacheUsage.DoNotCheckCache);
		query.setHint(QueryHints.REFRESH, HintValues.TRUE);					
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getResultList() {
		TypedQuery<T> query  = null;
		if (isNativeSQL()) {			
			query = (TypedQuery<T>) getNativeQuery();
			setHints(query);			
		} else {
			setPredicates(criteria,predicates);
			setSort();					
			query = em.createQuery(criteria);
			setHints(query);			
		}

		if (maxResults!=null) {
			query.setMaxResults(maxResults.intValue());
		}
		return query.getResultList();
	}

	private void setPredicates(CriteriaQuery<T> criteria,
			List<Predicate> predicates) {
		Predicate[] pred = predicates.toArray(new Predicate[0]);
		criteria.where(pred);
	}

	public List<Predicate> getPredicates() {
		return predicates;
	}
	
}
