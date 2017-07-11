package com.imwsoftware.mongo;

import java.util.Optional;

/**
 * Class: ZipRepositoryCustom.java
 *
 * @author: Springzen
 * @since: Jul 10, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 *
 */
public interface ZipRepositoryCustom {
	void aggregatePopulationBySize(long size);

	void aggregatePopulationBySize(long size, Optional<String> outCollection);

	void aggregatePopulationBySizeGte10Million();

	void aggregatePopulationBySizeGte10MillionOut();

	boolean stateTotalPopAggregationExists();

	void averageCityPopulationByState();

	void returnLargestAndSmallestCityByState();
}
