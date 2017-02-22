# Content management for polydata records

Service for management of polydata records stored in different poly stores: flat file, sqlite
 
Modules
=======

Current version: 0.2.0-SNAPSHOT

Releases:

 0.1.1 - hateaos api for fetching records 

 0.0.1 - basic functionality release

```
polycms-app-hateoas - hateoas based API 
polycms-core - services for accessing poly records
polycms-model - model objects
```

HATEAOS links
 
```json
{
  "version": "0.2.0",
  "data": "localhost",
  "_links": {
    "categories": {
      "href": "http://localhost:8080/storage/localhost/categories"
    },
    "tags": {
      "href": "http://localhost:8080/storage/localhost/tags"
    },
    "properties": {
      "href": "http://localhost:8080/storage/localhost/properties"
    },
    "metadata": {
      "href": "http://localhost:8080/storage/localhost/metadata"
    },
    "raw": {
      "href": "http://localhost:8080/storage/localhost/raw"
    },
    "query": {
      "href": "http://localhost:8080/storage/localhost/query"
    }
  }
}

```

To transfer image db, set in metadata "storageTransferAllowed": "true" 

License
=======
 
    Copyright (c) 2016, 2017 Denis O <denis@universal-development.com>
 
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

