package com.imwsoftware.mongo;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregationOptions;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Class: ZipRepositoryImpl.java
 *
 * @author: Springzen
 * @since: Jul 10, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 *
 */
public class ZipRepositoryImpl implements ZipRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void aggregatePopulationBySizeGte10Million() {
		aggregatePopulationBySize(10 * 1000 * 1000);
	}

	@Override
	public void aggregatePopulationBySizeGte10MillionOut() {
		aggregatePopulationBySize(10 * 1000 * 1000, Optional.of("StateTotalPopAggregation"));
	}

	@Override
	public void aggregatePopulationBySize(long size) {
		aggregatePopulationBySize(size, Optional.empty());
	}

	/**
	 * <code>
	  	db.zipcodes.aggregate( [
			{ $group: { _id: "$state", totalPop: { $sum: "$pop" } } },
			{ $match: { totalPop: { $gte: 10*1000*1000 } } }
		] )
	 * </code>
	 */
	@Override
	public void aggregatePopulationBySize(long size, Optional<String> outCollection) {

		@SuppressWarnings("unused")
		AggregationOperation aggregationOperation = new AggregationOperation() {
			@Override
			public DBObject toDBObject(AggregationOperationContext context) {
				return new BasicDBObject("$out", "StateTotalPopAggregation");
			}
		};

		Criteria criteria = Criteria.where("totalPop").gte(size);

		List<AggregationOperation> aggregationOperations = new ArrayList<>();

		aggregationOperations.add(group("state").sum("pop").as("totalPop"));
		aggregationOperations.add(match(criteria));
		aggregationOperations.add(project("_id").and("totalPop").as("totalPop"));
		if (outCollection.isPresent()) {
			aggregationOperations.add(Aggregation.out(outCollection.get()));
		}

		Aggregation agg = newAggregation(aggregationOperations).withOptions(newAggregationOptions().allowDiskUse(true).build());

		System.out.println(agg.toString());

		// Aggregation agg = newAggregation(
		// group("state").sum("pop").as("totalPop"),
		// match(criteria),
		// project("_id")
		// .and("totalPop").as("totalPop"))
		// .withOptions(newAggregationOptions()
		// .allowDiskUse(true).build());

		List<String> strings = aggregateToString(agg, "zipcodes");
		printStringAggregates(strings);

	}

	@Override
	public boolean stateTotalPopAggregationExists() {
		return mongoTemplate.collectionExists("StateTotalPopAggregation");
	}

	/**
	 * <code>
		 db.zipcodes.aggregate( [
	   		{ $group: { _id: { state: "$state", city: "$city" }, pop: { $sum: "$pop" } } },
	   		{ $group: { _id: "$_id.state", avgCityPop: { $avg: "$pop" } } }
		] )
	 * </code>
	 */
	@Override
	public void averageCityPopulationByState() {
		Aggregation agg = newAggregation(
				group("state", "city").sum("pop").as("pop"),
				group("_id.state").avg("pop").as("avgCityPop"))
						.withOptions(newAggregationOptions()
								.allowDiskUse(true).build());

		List<String> strings = aggregateToString(agg, "zipcodes");
		printStringAggregates(strings);
	}

	/**
	 * <code>
		db.zipcodes.aggregate( [
		   { $group:
		      {
		        _id: { state: "$state", city: "$city" },
		        pop: { $sum: "$pop" }
		      }
		   },
		   { $sort: { pop: 1 } },
		   { $group:
		      {
		        _id : "$_id.state",
		        biggestCity:  { $last: "$_id.city" },
		        biggestPop:   { $last: "$pop" },
		        smallestCity: { $first: "$_id.city" },
		        smallestPop:  { $first: "$pop" }
		      }
		   },
	
	  		// the following $project is optional, and
	  		// modifies the output format.
	
	  		{ $project:
	    		{ 
	    			_id: 0,
	      			state: "$_id",
	      			biggestCity:  { name: "$biggestCity",  pop: "$biggestPop" },
	      			smallestCity: { name: "$smallestCity", pop: "$smallestPop" }
	    		}
	  		}
		] )
	 * </code>
	 * 
	 * @ref https://github.com/spring-projects/spring-data-mongodb/blob/master/spring-data-mongodb/src/test/java/org/springframework/data/mongodb/core/aggregation/AggregationTests.java
	 */
	@Override
	public void returnLargestAndSmallestCityByState() {

		/**
		 * Group smallest and biggest city by state
		 */
		GroupOperation groupSmallestAndBiggestCities = group("state")
				.last("city").as("biggestCity")
				.last("pop").as("biggestPop")
				.first("city").as("smallestCity")
				.first("pop").as("smallestPop");

		/**
		 * Project using nested objects
		 */
		ProjectionOperation project = project() //
				.and("state").previousOperation() //
				.and("biggestCity").nested(Aggregation.bind("name", "biggestCity").and("population", "biggestPop")) //
				.and("smallestCity").nested(Aggregation.bind("name", "smallestCity").and("population", "smallestPop"));

		/**
		 * Assemble the aggregation
		 */
		Aggregation agg = newAggregation(
				group("state", "city").sum("pop").as("pop"),
				sort(Sort.Direction.ASC, "pop"),
				groupSmallestAndBiggestCities,
				project)
						.withOptions(newAggregationOptions()
								.allowDiskUse(true).build());

		/**
		 * assertThat(agg, is(notNullValue()));
		 * assertThat(agg.toString(), is(notNullValue()));
		 */
		List<String> strings = aggregateToString(agg, "zipcodes");
		printStringAggregates(strings);

	}

	//
	// .. Helpers
	//
	public List<String> aggregateToString(Aggregation aggregation, String collection) {
		AggregationResults<String> results = mongoTemplate.aggregate(aggregation, collection, String.class);
		List<String> result = results.getMappedResults();

		return result;
	}

	public void printStringAggregates(List<String> strings) {
		for (String string : strings) {
			System.out.println(string);
		}
	}

}
