# [fit] MongoDB with Spring Boot
Presented by IMWSoftware LLC

---

## MongoTemplate
#### The class MongoTemplate, located in the package org.springframework.data.mongodb.core, is the central class of the Spring’s MongoDB support providing a rich feature set to interact with the database. The template offers convenience operations to create, update, delete and query for MongoDB documents and provides a mapping between your domain objects and MongoDB documents.  
##### Once configured, MongoTemplate is thread-safe and can be reused across multiple instances.  
Ref: [Introduction to MongoTemplate](https://docs.spring.io/spring-data/data-mongo/docs/current/reference/html/#mongo-template)

---

#### Instantiating MongoTemplate
```java
@Configuration
@EnableMongoRepositories
class ApplicationConfig extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return "sdjug";
	}

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient();
	}

	@Override
	protected String getMappingBasePackage() {
		return "com.imwsoftware.mongo.model";
	}
}
```

---
##### MongoTemplate

```java
@Autowired
private MongoTemplate mongoTemplate;
```

---

## Repositories

### For the most part, interaction with MongoDB can be done with Spring Repositories

### Most common queries can be accessed by simply creating a repository with a domain class as well as the id type of the domain

---

## Repositories
```java
@Repository
public interface ZipRepository extends PagingAndSortingRepository<Zip, String> {
}
```

---

## Extending Repositories
### When the default repository does not provide the required functionally, it can be extended. By using this approach the DAO pattern can be avoided.

---

## Adding custom behavior to single repositories
* To enrich a repository with custom functionality you first define an interface and an implementation for the custom functionality. Make your initial repository interface extend the custom interface.

---

```java
public interface ZipRepositoryCustom {
}

public class ZipRepositoryImpl implements ZipRepositoryCustom {
}

@Repository
public interface ZipRepository extends PagingAndSortingRepository<Zip, String>, ZipRepositoryCustom {
}
```

---

`The implementation itself does not depend on Spring Data and can be a regular Spring bean. So you can use standard dependency injection behavior to inject references to other beans like a MongoTemplate, take part in aspects, and so on.`
Ref: [Custom implementations for Spring Data repositories](http://docs.spring.io/spring-data/data-mongo/docs/1.10.4.RELEASE/reference/html/#repositories.custom-implementations)


---

##### Insert a single record
```java
@Override
public void insertStudent(Student student) {
  mongoTemplate.insert(student);
}
```

---
##### Insert multiple records
```java
@Override
public void insertStudents(List<Student> students) {
  mongoTemplate.insert(students, COLLECTION_STUDENTS);
}
```

---
# [fit] But... duplicates happen

---

##### Bulk operations to avoid duplicate exceptions
```java
  @Override
  public <T> int bulkInsert(List<T> records, String collectionName) {
    int size = Optional.ofNullable(records).orElse(new ArrayList<>(0)).size();

    if (CollectionUtils.isNotEmpty(records)) {
      try {
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkMode.UNORDERED, StringUtils.isNotBlank(collectionName) ? collectionName : COLLECTION_STUDENTS);
        BulkOperations insert = bulkOps.insert(records);
        BulkWriteResult execute = insert.execute();
        size = execute.getInsertedCount();
      } catch (BulkOperationException e) {
        logger.debug("Received: " + size);
        if (e.getResult() != null) {
          size = e.getResult().getInsertedCount();
          logger.debug("Inserted: " + size);
        }
      } catch (Exception e) {
        logger.warn("Exception: Failed bulk insert.", e);
      }
    } else {
      size = 0;
    }

    return size;
  }
```

---

## Text Search

```java
	public List<Zip> textSearch(String... words) {
		TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(words);

		Query query = TextQuery
				.queryText(criteria);

		List<Zip> zips = mongoTemplate.find(query, Zip.class);

		return zips;
	}
```

---

# [fit] Aggregation Methods


---

## MongoDB Aggregation: mongo shell

```javascript
db.zipcodes.aggregate( [
			 { $group: { _id: { state: "$state", city: "$city" }, pop: { $sum: "$pop" } } },
			 { $group: { _id: "$_id.state", avgCityPop: { $avg: "$pop" } } }
	 ] )
```

---

## MongoDB Aggregation: Spring implementation

```java
	group("state", "city").sum("pop").as("pop"),
	group("_id.state").avg("pop").as("avgCityPop"))
		.withOptions(newAggregationOptions()
		.allowDiskUse(true).build());
```
---

# [fit] Unit Testing Spring Boot

---

### Testing MongoDB queries

```java
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class MongoTestsStudent {

	@Autowired
	StudentRepository repository;

	@Test
	public void findAllSortedDesc() {
		Iterable<Student> all = repository.findAll(new Sort(new Order(Direction.DESC, "name")));
		assertThat(((Collection<?>) all).size() > 0, is(true));
	}
}

```

---

### Testing REST endpoints

```java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = ApplicationConfig.class)
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.imwsoftware" })
public class TestStudentRestEndpoints {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void exampleTest() {
		String body = this.restTemplate.getForObject("/students/list", String.class);

		System.out.println(body);

		assertThat(body != null, is(true));

		try {
			List<Student> students = Utils.convert(body, Student.class);
			students.forEach(System.out::println);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
```

---

# [fit] The End
