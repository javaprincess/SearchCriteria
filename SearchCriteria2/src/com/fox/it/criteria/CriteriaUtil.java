package com.fox.it.criteria;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CriteriaUtil {
 public  static <T> CriteriaQuery<T> get(EntityManager em,Class<T> clazz) {
		CriteriaBuilder builder = em.getCriteriaBuilder();		
		CriteriaQuery<T> criteria = builder.createQuery(clazz);
		return criteria;
	 
 }
 
 public static <T> Predicate like(Root<T> from, CriteriaBuilder builder,String fieldName, String value) {
	 return builder.like(from.<String>get(fieldName),value);
 }
 
 public static <T> Predicate likeUpper(Root<T> from,CriteriaBuilder builder,String fieldName, String value) {
	 return builder.like(builder.upper(from.<String>get(fieldName)),value.toUpperCase());
 }
 
 public static <T> Predicate likeLower(Root<T> from,CriteriaBuilder builder,String fieldName, String value) {
	 return builder.like(builder.lower(from.<String>get(fieldName)),value.toLowerCase());
 }
 
 public static <T> Predicate equal(Root<T> from, CriteriaBuilder builder, String fieldName, Object value) {
	 return builder.equal(from.get(fieldName), value);
 }

 public static <T> Predicate isNotNull(Root<T> from,CriteriaBuilder builder,String fieldName) {
	 return builder.isNotNull(from.get(fieldName));
 }

 public static <T> Predicate isNull(Root<T> from,CriteriaBuilder builder,String fieldName) {
	 return builder.isNull(from.get(fieldName));
 }
 
 
 public static <T> Predicate ne(Root<T> from, CriteriaBuilder builder, String fieldName, Object value) {
	 return builder.notEqual(from.get(fieldName), value);
 }

 
 
 public static <T> Predicate ge(Root<T> from, CriteriaBuilder builder, String fieldName, Date value) {	 
	 return builder.greaterThanOrEqualTo(from.<Date>get(fieldName), value);	 
 }
 
 public static <T> Predicate le(Root<T> from, CriteriaBuilder builder, String fieldName, Date value) {	 
	 return builder.lessThanOrEqualTo(from.<Date>get(fieldName), value);	 
 }

 
 public static <T> Predicate ge(Root<T> from, CriteriaBuilder builder, String fieldName, Number value) {
	 return builder.ge(from.<Number>get(fieldName), value);	 
 }

 public static <T> Predicate le(Root<T> from, CriteriaBuilder builder, String fieldName, Number value) {
	 return builder.le(from.<Number>get(fieldName), value);
 }
 
 public static <T> Predicate equalUpper(Root<T> from, CriteriaBuilder builder, String fieldName, String value) {
	 return builder.equal(builder.upper(from.<String>get(fieldName)),value.toUpperCase());
 }
 
 public static <T> Predicate equalLower(Root<T> from, CriteriaBuilder builder, String fieldName, String value) {
	 return builder.equal(builder.lower(from.<String>get(fieldName)),value.toLowerCase());
 }
 
 public static <T> Predicate in(Root<T> from, CriteriaBuilder builder, String fieldName, List<? extends Object> values) {
	 In<Object> in = builder.in(from.get(fieldName));
	 for (Object value: values) {
		 in.value(value);
	 }
	 return in;
 }
 
}
