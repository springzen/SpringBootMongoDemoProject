# [fit] MongoDB with Spring Boot
Presented by IMWSoftware LLC

---

# The entire presentation is written in Markdown and it is saved in the same repository as **presentation.md**

---

## [MongoDB Introduction](https://docs.mongodb.com/manual/introduction/)
MongoDB is an open-source document database that provides high performance, high availability, and automatic scaling.

### Document Database
A record in MongoDB is a document, which is a data structure composed of field and value pairs. MongoDB documents are similar to JSON objects. The values of fields may include other documents, arrays, and arrays of documents.  

---
## Document Database

The advantages of using documents are:  

* Documents (i.e. objects) correspond to native data types in many programming languages.
* Embedded documents and arrays reduce need for expensive joins.
* Dynamic schema supports fluent polymorphism.


---
#### Installation

[Linux instructions](https://docs.mongodb.com/manual/administration/install-on-linux/)

```
sudo yum install -y mongodb-org
sudo apt-get install -y mongodb-org

# from a tarball
curl -O https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-3.4.6.tgz
tar -zxvf mongodb-linux-x86_64-3.4.6.tgz
mkdir -p mongodb
cp -R -n mongodb-linux-x86_64-3.4.6/ mongodb
export PATH=<mongodb-install-directory>/bin:$PATH
```

---

#### Installation

[MacOS instructions](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-os-x/)
```
brew install mongodb
```

---
#### Installation

[Windows instructions](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows/)

On Windows an installer is provided, but you must check your system before installing. Follow the instructions from the above link to install on Windows.

---

#### After the installation
Once installed, make sure the `bin` directory is in the system path.  

* `mongod` is the primary daemon process for the MongoDB system. It handles data requests, manages data access, and performs
background management operations.
* `mongo` is an interactive JavaScript shell interface to MongoDB

> **Note:** MongoDB default port is `27017`.

---

#### Run MongoDB

Manually invoke `mongod` (if not running as a Daemon/Service)  
Run `mongo` to connect to the default port `27017` -> specify --port if mongod is not running on the default port

Commands:
```
mongod
mongo
mongo --port <non default port>
```

---

## The MongoDB Introduction Guide

To simplify this session, I am using an already existing guide from PluralSight  
[Introduction to MongoDB](https://github.com/pluralsight/guides/blob/master/published/sql/introduction-to-mongodb/article.md)  
[Original](https://www.pluralsight.com/guides/sql/introduction-to-mongodb)  
[Local](IntroToMongoDB.md)  

---
## The MongoDB Aggregation Guide

This session is based on MongoDB official guide [Aggregation with the Zip Code Data Set](https://docs.mongodb.com/manual/tutorial/aggregation-zip-code-data-set/)  

---

#### Count all all the Springfield(s) in the USA

```javascript
db.zipcodes.aggregate(
	[
		{
			$match: {
				city: "SPRINGFIELD"
			}
		},
		{
			$count: "count"
		},

	]
);
```
[How many towns or cities called Springfield are there in the US and what states are they located in?](https://www.quora.com/How-many-towns-or-cities-called-Springfield-are-there-in-the-US-and-what-states-are-they-located-in)

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
		return "imwsoftware";
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
MongoTemplate mongoTemplate;
```

---

## For the most part, interaction with MongoDB can be done with Spring Repositories

### Most common can be accessed by simply creating a repository with a domain class as well as the id type of the domain

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
* To enrich a repository with custom functionality you first define an interface and an implementation for the custom functionality. Use the repository interface you provided to extend the custom interface.

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

```java
@Override
public void insertStudents(List<Student> students) {
  mongoTemplate.insert(students, COLLECTION_STUDENTS);
}
```

---


# [fit] Is

---

# [fit] Beautiful

---

## Make your titles fill the screen.

### _Note:_ This only works if you have nothing but headlines on a slide. Otherwise things are going to start looking ridiculous.
