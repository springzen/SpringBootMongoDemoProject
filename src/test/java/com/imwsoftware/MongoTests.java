package com.imwsoftware;

import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.imwsoftware.mongo.ZipRepository;
import com.imwsoftware.mongo.model.Zip;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class MongoTests {

	private static Logger logger = LoggerFactory.getLogger(MongoTests.class);

	@Autowired
	ZipRepository repository;

	@Autowired
	public MongoTemplate mongoTemplate;

	public MongoTests() {
	}

	@Test
	public void readsFirstPageCorrectly() {

		Page<Zip> zips = repository.findAll(new PageRequest(0, 10));
		assertThat(zips.isFirst(), is(true));
	}

	@Test
	public void findTop10ByCity() {

		List<Zip> zips = repository.findTop10ByCity("SAN DIEGO", new PageRequest(0, 10));
		assertThat(zips.size() > 0, is(true));
		zips.forEach(zip -> {
			System.out.println(zip);
		});
	}

	@Test
	public void findFirst10ByState() {
		// Sort sortOrder = new Sort(new Order(Direction.DESC, "publishedDate"));
		List<Zip> zips = repository.findFirst10ByState("TX", new Sort(new Order(Direction.DESC, "pop")));
		zips.forEach(zip -> {
			System.out.println(zip);
		});
		assertThat(zips.size() > 0, is(true));
	}

	@Test
	public void mongoTemplateReadOne() {
		Query query = new Query(Criteria.where("_id").is("92122"));
		Zip zip = mongoTemplate.findOne(query, Zip.class);
		assertTrue("92122 is in San Diego", zip.getCity().equalsIgnoreCase("san diego"));
	}

	@Test
	public void readOneInSanDiego() {

		try {
			Zip zipo = repository.findOne("92122");
			assertTrue("92122 is in San Diego", zipo.getCity().equalsIgnoreCase("san diego"));
		} catch (Exception e) {
			fail("This database does not have the Zip you are looking for");
		}

	}

	@Test
	public void findAllRecords() {

		Iterable<Zip> all = repository.findAll();
		logger.info("Collection size: " + ((Collection<?>) all).size());
		assertThat(((Collection<?>) all).size() > 0, is(true));
	}

}
