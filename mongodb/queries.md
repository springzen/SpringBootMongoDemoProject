## MongoDB Introduction: queries

### Ref: <https://www.pluralsight.com/guides/sql/introduction-to-mongodb>

### Insert

```javascript
db.students.insert({
  "name": "Praveen Kumar",
  "degree": "Cloud Computing",
  "email": "praveen@example.com",
  "subjects": [
    {
      "name": "Internet Networks",
      "prof": "Prof. Awesome Blossom"
    },
    {
      "name": "Cloud Computing",
      "prof": "Prof. Tech Ninja"
    },
    {
      "name": "Web Development",
      "prof": "Prof. Chunky Monkey"
    }
  ],
  "phone": ["9840035007", "9967728336", "7772844242"]
});
```

### Find pretty

```javascript
db.students.find().pretty();
```

```javascript
db.students.insert(
  [
    {
      "name": "Purushothaman",
      "degree": "Management",
      "email": "purush@example.com"
    },
    {
      "name": "Meaow Meaow",
      "degree": "Cat Study",
      "email": "meaow@example.com",
      "phone": ["9850420420"]
    },
  ]
);
```

### Find

```javascript
db.students.find();
```

```javascript
db.students.find().pretty();
```
```javascript
db.students.find({"name" : "Meaow Meaow"});
```

```javascript
db.students.find({"name" : "Meaow Meaow"}).pretty();
```

```javascript
db.students.find({"phone": "9840035007"}).pretty();
```

### Update

```javascript
db.students.update({"name": "Praveen Kumar"}, {
  "name": "Praveen Kumar",
  "degree": "Cloud Computing MSc",
  "email": "praveen@example.net",
  "subjects": [
    {
      "name": "Internet Networks",
      "prof": "Prof. Awesome Blossom"
    },
    {
      "name": "Cloud Computing",
      "prof": "Prof. Tech Ninja"
    },
    {
      "name": "Web Development",
      "prof": "Prof. Chunky Monkey"
    }
  ],
  "phone": ["9840035007", "9967728336", "7772844242"]
});
```

### Find

```javascript
db.students.find({"phone": "9840035007"}).pretty();
```

### Using **$set**

```javascript
db.students.update({
  "name": "Praveen Kumar"
}, {
  $set: {
    "email" : "praveen@example.net"
  }
});
```

### Incrementing Numeric Values

```javascript
db.students.update({
  "name": "Praveen Kumar"
}, {
  $set: {
    "points" : 15
  }
});
```

```javascript
db.students.find({"phone": "9840035007"}).pretty();
```

```javascript
db.students.update({
  "name": "Praveen Kumar"
}, {
  $inc: {
    "points" : 5
  }
});
```

```javascript
db.students.find({"phone": "9840035007"}).pretty();
```


### Removing Fields

```javascript
db.students.update({
  "name": "Praveen Kumar"
}, {
  $unset: {
    "points" : ""
  }
});
```

### Upsert

```javascript
db.students.update({
  "name": "Baahubali"
}, {
  "name": "Baahubali",
  "degree" : "Hill Climbing",
  "email" : "baahu@mahishmati.com"
});
```

```javascript
db.students.update({
  "name": "Baahubali"
}, {
  "name": "Baahubali",
  "degree" : "Hill Climbing",
  "email" : "baahu@mahishmati.com"
}, {
  "upsert": true
});

db.students.find().pretty();
```

### Renaming fields

```javascript
db.students.update({
  "name": "Baahubali"
}, {
  $rename: {
    "email" : "pigeon"
  }
});

db.students.find({"name": "Baahubali"}).pretty();
```

### Removing records

```javascript
db.students.insert([{"name": "Bleh"}, {"name": "Bleh"}, {"name": "Blah"}]);
```

```javascript
db.students.find();
```

```javascript
db.students.remove({
  "name": "Blah"
});
```

```javascript
db.students.remove({
  "name": "Bleh"
});
```

### Remove just one

```javascript
db.students.insert([{"name": "Bleh"}, {"name": "Bleh"}]);
```

```javascript
db.students.remove({
  "name": "Bleh"
}, {
  "justOne": true
});
```

### Querying with Multiple Conditions

```javascript
db.students.insert([
  {"name": "Bleh"},
  {"name": "Blah"}
]);
```

```javascript
db.students.find({
  $or: [
    {"name": "Blah"},
    {"name": "Bleh"}
  ]
});
```

```javascript
db.students.remove({$or: [{"name": "Blah"}, {"name": "Bleh"}]});
```


### Operators

```javascript
db.students.insert([
  {"name": "Bebo", "age": 5},
  {"name": "Chinna", "age": 10},
  {"name": "Elukaludha", "age": 15},
  {"name": "Kaalejoo", "age": 20},
  {"name": "Vela Thedoo", "age": 25},
  {"name": "Kanna Lammoo", "age": 30},
  {"name": "Daydee", "age": 40},
  {"name": "Paati", "age": 55},
  {"name": "Thathaa", "age": 60}
]);
```

```javascript
db.students.find();
```

```javascript
db.students.find({
  "age": {
     $gt: 18
   }
});
```

### Sorting

```javascript
db.students.find().sort({
  "name": 1
});
```

```javascript
db.students.find().sort({
  "name": -1
});
```

### Limiting Results

```javascript
db.students.find().sort({
  "age": -1
}).limit(1);
```

### Counting Records

```javascript
db.students.find().count();
```

### Iterating Results

```javascript
db.students.find().forEach(function (stud) {
  print("Student Name: " + stud.name);
});
```
