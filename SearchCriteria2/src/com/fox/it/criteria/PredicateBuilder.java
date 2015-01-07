package com.fox.it.criteria;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PredicateBuilder<T> {
	
	private final Root<T> from;
	private final CriteriaBuilder builder;
	
	public PredicateBuilder(Root<T> from, CriteriaBuilder builder) {
		this.from = from;
		this.builder = builder;
	}
	
	public Predicate like(String fieldName, String value) {
		return CriteriaUtil.like(from, builder, fieldName, value);
	}
	
	public Predicate likeUpper(String fieldName, String value) {
		return CriteriaUtil.likeUpper(from, builder, fieldName, value);
	}
	
	public Predicate likeLower(String fieldName, String value) {
		return CriteriaUtil.likeLower(from, builder, fieldName, value);
	}
	
	public Predicate equal(String fieldName, Object value) {
		return CriteriaUtil.equal(from, builder, fieldName, value);
	}
	
	public Predicate isNotNull(String fieldName) {
		return CriteriaUtil.isNotNull(from, builder, fieldName);
	}
	
	public Predicate isNull(String fieldName) {
		return CriteriaUtil.isNull(from, builder, fieldName);
	}
	
	
	public Predicate ne(String fieldName,Object value) {
		return CriteriaUtil.ne(from, builder, fieldName, value);
	}
	public Predicate ge(String fieldName, Number value) {
		return CriteriaUtil.ge(from, builder, fieldName, value);
	}

	public Predicate le(String fieldName, Number value) {
		return CriteriaUtil.le(from, builder, fieldName, value);	
	}
	
	public Predicate ge(String fieldName, Date value) {
		return CriteriaUtil.ge(from, builder, fieldName, value);
	}

	public Predicate le(String fieldName, Date value) {
		return CriteriaUtil.le(from, builder, fieldName, value);	
	}
	
	
	
	public Predicate equalUpper(String fieldName, String value) {
		return CriteriaUtil.equalUpper(from, builder, fieldName, value);
	}
	
	public Predicate equalLower(String fieldName, String value) {
		return CriteriaUtil.equalLower(from, builder, fieldName, value);		
	}
	
	public Predicate in(String fieldName,List<? extends Object> values) {
		return CriteriaUtil.in(from, builder, fieldName, values);
	}
	
	public Predicate or(Predicate... p) {
		return builder.or(p); 
	}
	
}
