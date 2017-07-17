# MongoDB Aggregation Introduction
By IMWSoftware LLC

---

## The MongoDB Aggregation Guide

This session is based on MongoDB official guide titled: [Aggregation with the Zip Code Data Set](https://docs.mongodb.com/manual/tutorial/aggregation-zip-code-data-set/)


---

## Aggregation with the Zip Code Data Set

---

## Data model
```javascript
{
  "_id": "10280",
  "city": "NEW YORK",
  "state": "NY",
  "pop": 5574,
  "loc": [
    -74.016323,
    40.710537
  ]
}
```

---

## Data model

- The `_id` field holds the zip code as a string.
- The `city` field holds the city name. A city can have more than one zip code associated with it as different sections of the city can each have a different zip code.
- The `state` field holds the two letter state abbreviation.
- The `pop` field holds the population.
- The `loc` field holds the location as a latitude longitude pair.

---

## `aggregate()` Method

All of the following examples use the [aggregate()](https://docs.mongodb.com/manual/reference/method/db.collection.aggregate/#db.collection.aggregate) helper in the [mongo](https://docs.mongodb.com/manual/reference/program/mongo/#bin.mongo) shell.

---

## `aggregate()` Method

The [aggregate()](https://docs.mongodb.com/manual/reference/method/db.collection.aggregate/#db.collection.aggregate)  method uses the [aggregation pipeline](https://docs.mongodb.com/manual/core/aggregation-pipeline/#id1) to processes documents into aggregated results. An [aggregation pipeline](https://docs.mongodb.com/manual/core/aggregation-pipeline/#id1) consists of [stages](https://docs.mongodb.com/manual/reference/operator/aggregation/#aggregation-pipeline-operator-reference) with each stage processing the documents as they pass along the pipeline. Documents pass through the stages in sequence.

---

## `aggregate()` Method

The [aggregate()](https://docs.mongodb.com/manual/reference/method/db.collection.aggregate/#db.collection.aggregate) method in the [mongo](https://docs.mongodb.com/manual/reference/program/mongo/#bin.mongo) shell provides a wrapper around the [aggregate](https://docs.mongodb.com/manual/reference/command/aggregate/#dbcmd.aggregate) database command. See the documentation for your [driver](https://docs.mongodb.com/manual/applications/drivers/) for a more idiomatic interface for data aggregation operations.

---

## Return States with Populations above 10 Million

The following aggregation operation returns all states with total population greater than 10 million:


```javascript
db.zipcodes.aggregate( [
   { $group: { _id: "$state", totalPop: { $sum: "$pop" } } },
   { $match: { totalPop: { $gte: 10*1000*1000 } } }
] )
```

---

## Return States with Populations above 10 Million

###### In this example, the aggregation pipeline consists of the $group stage followed by the $match stage:

###### The [$group](https://docs.mongodb.com/manual/reference/operator/aggregation/group/#pipe._S_group) stage groups the documents of the `zipcode` collection by the `state` field, calculates the `totalPop` field for each state, and outputs a document for each unique state. The new per-state documents have two fields: the `_id` field and the `totalPop` field. The `_id` field contains the value of the `state`; i.e. the group by field. The `totalPop` field is a calculated field that contains the total population of each state. To calculate the value, [$group](https://docs.mongodb.com/manual/reference/operator/aggregation/group/#pipe._S_group) uses the [$sum](https://docs.mongodb.com/manual/reference/operator/aggregation/sum/#grp._S_sum) operator to add the population field (`pop`) for each state.


---

## Return States with Populations above 10 Million

After the [$group](https://docs.mongodb.com/manual/reference/operator/aggregation/group/#pipe._S_group) stage, the documents in the pipeline resemble the following:

```javascript
{
  "_id" : "AK",
  "totalPop" : 550043
}
```

---

## Return States with Populations above 10 Million

The [$match](https://docs.mongodb.com/manual/reference/operator/aggregation/match/#pipe._S_match) stage filters these grouped documents to output only those documents whose `totalPop` value is greater than or equal to 10 million. The [$match](https://docs.mongodb.com/manual/reference/operator/aggregation/match/#pipe._S_match) stage does not alter the matching documents but outputs the matching documents unmodified.

---

## Return States with Populations above 10 Million

The equivalent [SQL](https://docs.mongodb.com/manual/reference/glossary/#term-sql) for this aggregation operation is:

```sql
SELECT state, SUM(pop) AS totalPop
FROM zipcodes
GROUP BY state
HAVING totalPop >= (10*1000*1000)
```

---

## Return Average City Population by State
The following aggregation operation returns the average populations for cities in each state:

```javascript
db.zipcodes.aggregate( [
   { $group: { _id: { state: "$state", city: "$city" }, pop: { $sum: "$pop" } } },
   { $group: { _id: "$_id.state", avgCityPop: { $avg: "$pop" } } }
] )
```

---

## Return Average City Population by State

In this example, the [aggregation pipeline](https://docs.mongodb.com/manual/core/aggregation-pipeline/#id1) consists of the [$group](https://docs.mongodb.com/manual/reference/operator/aggregation/group/#pipe._S_group) stage followed by another [$group](https://docs.mongodb.com/manual/reference/operator/aggregation/group/#pipe._S_group) stage:

The first [$group](https://docs.mongodb.com/manual/reference/operator/aggregation/group/#pipe._S_group) stage groups the documents by the combination of `city` and `state`, uses the [$sum](https://docs.mongodb.com/manual/reference/operator/aggregation/sum/#grp._S_sum) expression to calculate the population for each combination, and outputs a document for each city and state combination. [1](https://docs.mongodb.com/manual/tutorial/aggregation-zip-code-data-set/#multiple-zips-per-city)

---

## Return Average City Population by State
After this stage in the pipeline, the documents resemble the following:

```javascript
{
  "_id" : {
    "state" : "CO",
    "city" : "EDGEWATER"
  },
  "pop" : 13154
}
```
---

## Return Average City Population by State

A second [$group](https://docs.mongodb.com/manual/reference/operator/aggregation/group/#pipe._S_group) stage groups the documents in the pipeline by the `_id.state` field (i.e. the state field inside the `_id` document), uses the [$avg](https://docs.mongodb.com/manual/reference/operator/aggregation/avg/#grp._S_avg expression to calculate the average city population (`avgCityPop`) for each state, and outputs a document for each state.
The documents that result from this aggregation operation resembles the following:

```javascript
{
  "_id" : "MN",
  "avgCityPop" : 5335
}
```

---

## Return Largest and Smallest Cities by State

The following aggregation operation returns the smallest and largest cities by population for each state:

---

#### Return Largest and Smallest Cities by State

```javascript
db.zipcodes.aggregate( [
   { $group: { _id: { state: "$state", city: "$city" }, pop: { $sum: "$pop" } } },
   { $sort: { pop: 1 } },
   { $group: {  _id : "$_id.state", biggestCity:  { $last: "$_id.city" },
        biggestPop:   { $last: "$pop" },
        smallestCity: { $first: "$_id.city" },
        smallestPop:  { $first: "$pop" }
      }
   },
  { $project:
    { _id: 0,
      state: "$_id",
      biggestCity:  { name: "$biggestCity",  pop: "$biggestPop" },
      smallestCity: { name: "$smallestCity", pop: "$smallestPop" }
    }
  }
] )
```

---

## Return Largest and Smallest Cities by State

In this example, the [aggregation pipeline](https://docs.mongodb.com/manual/core/aggregation-pipeline/#id1) consists of a [$group](https://docs.mongodb.com/manual/reference/operator/aggregation/group/#pipe._S_group) stage, a [$sort](https://docs.mongodb.com/manual/reference/operator/aggregation/sort/#pipe._S_sort) stage, another [$group](https://docs.mongodb.com/manual/reference/operator/aggregation/group/#pipe._S_group) stage, and a [$project](https://docs.mongodb.com/manual/reference/operator/aggregation/project/#pipe._S_project) stage:

The first [$group](https://docs.mongodb.com/manual/reference/operator/aggregation/group/#pipe._S_group) stage groups the documents by the combination of the city and state, calculates the [sum](https://docs.mongodb.com/manual/reference/operator/aggregation/sum/#grp._S_sum) of the pop values for each combination, and outputs a document for each city and state combination.

---

## Return Largest and Smallest Cities by State
At this stage in the pipeline, the documents resemble the following:

```javascript
{
  "_id" : {
    "state" : "CO",
    "city" : "EDGEWATER"
  },
  "pop" : 13154
}
```

---

## Return Largest and Smallest Cities by State

#### The [$sort](https://docs.mongodb.com/manual/reference/operator/aggregation/sort/#pipe._S_sort) stage orders the documents in the pipeline by the pop field value, from smallest to largest; i.e. by increasing order. This operation does not alter the documents.
#### The next [$group](https://docs.mongodb.com/manual/reference/operator/aggregation/group/#pipe._S_group) stage groups the now-sorted documents by the `_id.state` field (i.e. the `state` field inside the `_id` document) and outputs a document for each state.
#### The stage also calculates the following four fields for each state. Using the [$last](https://docs.mongodb.com/manual/reference/operator/aggregation/last/#grp._S_last) expression, the [$group](https://docs.mongodb.com/manual/reference/operator/aggregation/group/#pipe._S_group) operator creates the biggestCity and biggestPop fields that store the city with the largest population and that population. Using the [$first](https://docs.mongodb.com/manual/reference/operator/aggregation/first/#grp._S_first) expression, the [$group](https://docs.mongodb.com/manual/reference/operator/aggregation/group/#pipe._S_group) operator creates the smallestCity and smallestPop fields that store the city with the smallest population and that population.

---

## Return Largest and Smallest Cities by State

The documents, at this stage in the pipeline, resemble the following:

```javascript
{
  "_id" : "WA",
  "biggestCity" : "SEATTLE",
  "biggestPop" : 520096,
  "smallestCity" : "BENGE",
  "smallestPop" : 2
}
```
---

The final [$project](https://docs.mongodb.com/manual/reference/operator/aggregation/project/#pipe._S_project) stage renames the `_id` field to `state` and moves the `biggestCity`, `biggestPop`, `smallestCity`, and `smallestPop` into `biggestCity` and `smallestCity` embedded documents.
The output documents of this aggregation operation resemble the following:

```javascript
{
  "state" : "RI",
  "biggestCity" : {
    "name" : "CRANSTON",
    "pop" : 176404
  },
  "smallestCity" : {
    "name" : "CLAYVILLE",
    "pop" : 45
  }
}
```

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


* This works with both local files and web images
* You don’t _have_ to drag the file, you can also type the Markdown yourself if you know how

![left,filtered](http://deckset-assets.s3-website-us-east-1.amazonaws.com/colnago1.jpg)
