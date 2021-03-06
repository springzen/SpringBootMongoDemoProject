# [fit] MongoDB Introduction
Presented by IMWSoftware LLC

---
## The MongoDB Introduction Guide

To simplify this session, I am using an already existing guide from PluralSight  
[Introduction to MongoDB](https://github.com/pluralsight/guides/blob/master/published/sql/introduction-to-mongodb/article.md)  
[Original](https://www.pluralsight.com/guides/sql/introduction-to-mongodb)  
[Local](IntroToMongoDB.md)  

---

## [MongoDB Introduction](https://docs.mongodb.com/manual/introduction/)
MongoDB is an open-source document database that provides high performance, high availability, and automatic scaling.

---

### Document Database
A record in MongoDB is a document, which is a data structure composed of field and value pairs. MongoDB documents are similar to JSON objects. The values of fields may include other documents, arrays, and arrays of documents.  

---
## Document Database

The advantages of using documents are:  

* Documents (i.e. objects) correspond to native data types in many programming languages.
* Embedded documents and arrays reduce need for expensive joins.
* Dynamic schema supports fluent polymorphism.

---

## Polymorphic type [Source](https://stackoverflow.com/questions/36946572/meaning-of-dynamic-schema-supports-fluent-polymorphism-in-mongodb-doc)

A **polymorphic** type is one whose operations can also be applied to values of some other type, or types. Lets have an example, consider the following MongoDB collection of cars

```javascript
{
  "TYPE": "BASIC CAR",
  "MAX_SPEED": 100,
  "MILEAGE": 20,
  "GEARS": 4,
  "FEATURES": [
     {
      "AC": "yes"
     }
  ]
}
```

The first 4 keys will be same for almost all cars ----> **polymorphic type**

---
## Polymorphic type...
```javascript
{
  "TYPE": "SPORTS CAR",
  "MAX_SPEED": 300,
  "MILEAGE": 10,
  "GEARS": 8,
  "FEATURES": [
    {
      "AC": "yes"
    },
    {
      "NITRO": "yes"
    },
    {
      "NAVIGATION": "yes"
    },
    .
    .
    .
    .
    ... so on
    ]
    "ADVANCED PROTECTION" : "yes",
    "SENSORS" : [
      {
        "OBSTACLE" : "yes"
      }
  ]
}
```

---

## Polymorphic type...

The sports inherits the features of **BASIC CAR** and also has some its own features, that's satisfies Polymorphism.

And coming to the part **Dynamic Schema** we can see that structure of document is different for both documents MongoDB won't restrict to a particular structure so that's satisfies **Dynamic Schema**.

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

## Mongo shell

```bash
$ mongo
```
```
MongoDB shell version v3.4.2
connecting to: mongodb://127.0.0.1:27017
MongoDB server version: 3.4.2
Server has startup warnings:
2017-06-28T18:44:57.856-0700 I CONTROL  [initandlisten]
2017-06-28T18:44:57.856-0700 I CONTROL  [initandlisten] ** WARNING: Access control is not enabled for the database.
2017-06-28T18:44:57.856-0700 I CONTROL  [initandlisten] **          Read and write access to data and configuration is unrestricted.
2017-06-28T18:44:57.856-0700 I CONTROL  [initandlisten]
>
```

---
## List all databases


```bash
> show dbs
```
```
imw     3.952GB
local   0.078GB
sdjug   0.078GB
test    0.078GB
tester  0.078GB
>
```

---
## Create a Database

Create a new database with `use` command.

```bash
> use imw
switched to db imw
>
```
---

## Check current database with `db` command

```bash
> db
imw
>
```

---

## MongoDB Documents

<https://docs.mongodb.com/manual/core/document/>

```javascript
{
   field1: value1,
   field2: value2,
   field3: value3,
   ...
   fieldN: valueN
}
```

---

## MongoDB Documents: arrays

<https://docs.mongodb.com/manual/core/document/#arrays>

```javascript
{
   ...
   contribs: [ "Turing machine", "Turing test", "Turingery" ],
   ...
}
```

---

## MongoDB Documents: Embedded Documents

<https://docs.mongodb.com/manual/core/document/#embedded-documents>

```javascript
{
   ...
   name: { first: "Alan", last: "Turing" },
   contact: { phone: { type: "cell", number: "111-222-3333" } },
   ...
}
```

---

## Creating Collections

[`db.createCollection()`](https://docs.mongodb.com/manual/reference/method/db.createCollection/)   

- parameter (collection name)

```javascript
> db.createCollection("students");
{ "ok" : 1 }
>
```

---

#### Listing Collections

To list out all the collections in this particular database, there's a command for it too. We can use `show collections`. The output will be similar to:

```bash
> show collections
students
>
```
---

##### IDEs
* [Robo 3T](https://robomongo.org/) -> Free
* [Studio 3T](https://studio3t.com/) -> Free for non-commercial uses. [FAQ](https://studio3t.com/buy/#faq)

---

##### Tools: mongostat
* [mongostat](https://docs.mongodb.com/manual/reference/program/mongostat/)
The **mongostat** utility provides a quick overview of the status of a currently running mongod or mongos instance. **mongostat** is functionally similar to the UNIX/Linux file system utility **vmstat**, but provides data regarding mongod and mongos instances.
```
mongostat --humanReadable true
```

---
##### Tools: mongotop
* [mongotop](https://docs.mongodb.com/manual/reference/program/mongotop/)
**mongotop** provides a method to track the amount of time a MongoDB instance spends reading and writing data. **mongotop** provides statistics on a per-collection level. By default, **mongotop** returns values every second.
Example:  In this example, **mongotop** will return every 15 seconds
```
mongotop 15
```

---

# [fit] ...
