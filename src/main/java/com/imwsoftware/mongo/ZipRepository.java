package com.imwsoftware.mongo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.imwsoftware.mongo.model.Zip;

/**
 * Class: ZipRepository.java
 *
 * @author: Springzen
 * @since: Jul 6, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 * 
 *           references:
 *           - for more on limiting results using Repositories
 *           https://docs.spring.io/spring-data/data-mongo/docs/current/reference/html/#repositories.limit-query-result
 *
 */
@Repository
public interface ZipRepository extends PagingAndSortingRepository<Zip, String> {

	/**
	 * Find by top 10 cities and page results
	 * 
	 * @param city
	 * @param pageable
	 * @return
	 */
	List<Zip> findTop10ByCity(String city, Pageable pageable);

	/**
	 * Find first 10 entries by state and sort
	 * 
	 * @param state
	 * @param sort
	 * @return
	 */
	List<Zip> findFirst10ByState(String state, Sort sort);
}
