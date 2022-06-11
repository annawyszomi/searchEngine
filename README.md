# searchEngine
The search engine is implemented as an inverted index that runs in memory and return a result list that is sorted by TF-IDF.

Endpoints descriptions 
| HTTP Method | URI |Description|
| --- | --- | --- |
| POST | `/documents` | Add documents list|
| GET | `/{term}` | Get search result for term |

## Technologies
* Java 17
* Spring Boot 
* Gradle
* JUnit5

## Run Application
```
$ cd ../searchEngine
$ ./gradlew bootRun
```

## Requests Examples
Add documents:
```
curl -X POST \
  http://localhost:8080/documents \
  -H 'accept: application/json' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 06d70613-f642-9b52-6ed5-4ebda2916ce6' \
  -d '[
 	{
 		"id": "doc1", 
 		"content": "the brown fox jumped over the brown dog"
 		
 	},
	{
 	 	"id": "docs2 ", 
 		"content": "he lazy brown dog sat in the corner"
 	 },
 	 {
 	 	"id": "docs3 ", 
 		"content": "the red fox bit the lazy dog"
 	 }
]
'
```


Search for term brown
```
curl -X GET \
  http://localhost:8080/brown \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 3bd09966-420f-8c9b-d7ea-a5e9974fc1a5'
```

## Results
The following documents are indexed:
Document 1: “the brown fox jumped over the brown dog” Document 2: “the lazy brown dog sat in the corner” Document 3: “the red fox bit the lazy dog”

A search for “brown” should now return the list: [document 2, document 1]

A search for “fox” should return the list: [document 1, document 3]

