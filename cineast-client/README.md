# openapi-android-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-android-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "org.openapitools:openapi-android-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

- target/openapi-android-client-1.0.0.jar
- target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import org.openapitools.client.api.DefaultApi;

public class DefaultApiExample {

    public static void main(String[] args) {
        DefaultApi apiInstance = new DefaultApi();
        String id = null; // String | 
        try {
            apiInstance.getObjectsWithId(id);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#getObjectsWithId");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *http://city-stories.dmi.unibas.ch:5105*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*DefaultApi* | [**getObjectsWithId**](docs/DefaultApi.md#getObjectsWithId) | **GET** /objects/{id} | Get objects with id
*DefaultApi* | [**getThumbnailsWithId**](docs/DefaultApi.md#getThumbnailsWithId) | **GET** /thumbnails/{id} | Get thumbnails with id
*MetadataApi* | [**findMetaById**](docs/MetadataApi.md#findMetaById) | **GET** /api/v1/find/metadata/by/id/{id} | Find metadata for the given object id
*MetadataApi* | [**findMetaFullyQualified**](docs/MetadataApi.md#findMetaFullyQualified) | **GET** /api/v1/find/metadata/of/{id}/in/{domain}/with/{key} | Find metadata for specific object id in given domain with given key
*MetadataApi* | [**findMetadataByDomain**](docs/MetadataApi.md#findMetadataByDomain) | **GET** /api/v1/find/metadata/in/{domain}/by/id/{domain} | Find metadata for specific object id in given domain
*MetadataApi* | [**findMetadataByDomainBatched**](docs/MetadataApi.md#findMetadataByDomainBatched) | **POST** /api/v1/find/metadata/in/{domain} | Find metadata in the specified domain for all the given ids
*MetadataApi* | [**findMetadataByKey**](docs/MetadataApi.md#findMetadataByKey) | **GET** /api/v1/find/metadata/with/{key}/by/id/{id} | Find metadata for a given object id with specified key
*MetadataApi* | [**findMetadataByKeyBatched**](docs/MetadataApi.md#findMetadataByKeyBatched) | **POST** /api/v1/find/metadata/with/{key} | Find metadata for a given object id with specified key
*MetadataApi* | [**findMetadataForObjectIdBatched**](docs/MetadataApi.md#findMetadataForObjectIdBatched) | **POST** /api/v1/find/metadata/by/id | Finds metadata for the given list of object ids
*MetadataApi* | [**findSegFeatById**](docs/MetadataApi.md#findSegFeatById) | **GET** /api/v1/find/feature/all/by/id/{id} | Find features for the given id
*MetadataApi* | [**findSegMetaById**](docs/MetadataApi.md#findSegMetaById) | **GET** /api/v1/find/metadata/by/segmentid/{id} | Find metadata for the given segment id
*MetadataApi* | [**findTagsByIdId**](docs/MetadataApi.md#findTagsByIdId) | **GET** /api/v1/find/feature/tags/by/id/{id} | Find tag ids for the given id
*MetadataApi* | [**findTextByIDAndCat**](docs/MetadataApi.md#findTextByIDAndCat) | **GET** /api/v1/find/feature/text/by/{id}/{category} | Find Text for the given id and retrieval category
*MiscApi* | [**findDistinctElementsByColumn**](docs/MiscApi.md#findDistinctElementsByColumn) | **POST** /api/v1/find/boolean/column/distinct | Find all distinct elements of a given column
*MiscApi* | [**selectFromTable**](docs/MiscApi.md#selectFromTable) | **POST** /api/v1/find/boolean/table/select | Find all elements of given columns
*ObjectApi* | [**findObjectsAll**](docs/ObjectApi.md#findObjectsAll) | **GET** /api/v1/find/objects/all | Find all objects for a certain type
*ObjectApi* | [**findObjectsByAttribute**](docs/ObjectApi.md#findObjectsByAttribute) | **GET** /api/v1/find/object/by/{attribute}/{value} | Find object by specified attribute value. I.e by id, name or path
*ObjectApi* | [**findObjectsByIdBatched**](docs/ObjectApi.md#findObjectsByIdBatched) | **POST** /api/v1/find/object/by/id | Find objects by id
*SegmentApi* | [**findSegmentById**](docs/SegmentApi.md#findSegmentById) | **GET** /api/v1/find/segments/by/id/{id} | Finds segments for specified id
*SegmentApi* | [**findSegmentByIdBatched**](docs/SegmentApi.md#findSegmentByIdBatched) | **POST** /api/v1/find/segments/by/id | Finds segments for specified ids
*SegmentApi* | [**findSegmentByObjectId**](docs/SegmentApi.md#findSegmentByObjectId) | **GET** /api/v1/find/segments/all/object/{id} | Find segments by their media object&#39;s id
*SegmentsApi* | [**findSegmentSimilar**](docs/SegmentsApi.md#findSegmentSimilar) | **POST** /api/v1/find/segments/similar | Find similar segments based on the given query
*SessionApi* | [**endExtraction**](docs/SessionApi.md#endExtraction) | **POST** /api/v1/session/extract/end | End the active extraction session
*SessionApi* | [**endSession**](docs/SessionApi.md#endSession) | **GET** /api/v1/session/end/{id} | End the session for given id
*SessionApi* | [**extractItem**](docs/SessionApi.md#extractItem) | **POST** /api/v1/session/extract/new | Extract new item
*SessionApi* | [**startExtraction**](docs/SessionApi.md#startExtraction) | **POST** /api/v1/session/extract/start | Start extraction session
*SessionApi* | [**startSession**](docs/SessionApi.md#startSession) | **POST** /api/v1/session/start | Start new session for given credentials
*SessionApi* | [**validateSession**](docs/SessionApi.md#validateSession) | **GET** /api/v1/session/validate/{id} | Validates the session with given id
*StatusApi* | [**status**](docs/StatusApi.md#status) | **GET** /api/v1/status | Get the status of the server
*TagApi* | [**findAllTags**](docs/TagApi.md#findAllTags) | **GET** /api/v1/find/tags/all | Find all tags
*TagApi* | [**findTagsBy**](docs/TagApi.md#findTagsBy) | **GET** /api/v1/find/tags/by/{attribute}/{value} | Find all tags specified by attribute value
*TagApi* | [**findTagsById**](docs/TagApi.md#findTagsById) | **POST** /api/v1/tags/by/id | Find all tags by ids


## Documentation for Models

 - [AbstractMetadataFilterDescriptor](docs/AbstractMetadataFilterDescriptor.md)
 - [ColumnSpecification](docs/ColumnSpecification.md)
 - [Credentials](docs/Credentials.md)
 - [DistinctElementsResult](docs/DistinctElementsResult.md)
 - [ExtractionContainerMessage](docs/ExtractionContainerMessage.md)
 - [ExtractionItemContainer](docs/ExtractionItemContainer.md)
 - [FeaturesAllCategoriesQueryResult](docs/FeaturesAllCategoriesQueryResult.md)
 - [FeaturesTextCategoryQueryResult](docs/FeaturesTextCategoryQueryResult.md)
 - [IdList](docs/IdList.md)
 - [MediaObjectDescriptor](docs/MediaObjectDescriptor.md)
 - [MediaObjectMetadataDescriptor](docs/MediaObjectMetadataDescriptor.md)
 - [MediaObjectMetadataQueryResult](docs/MediaObjectMetadataQueryResult.md)
 - [MediaObjectQueryResult](docs/MediaObjectQueryResult.md)
 - [MediaSegmentDescriptor](docs/MediaSegmentDescriptor.md)
 - [MediaSegmentMetadataDescriptor](docs/MediaSegmentMetadataDescriptor.md)
 - [MediaSegmentMetadataQueryResult](docs/MediaSegmentMetadataQueryResult.md)
 - [MediaSegmentQueryResult](docs/MediaSegmentQueryResult.md)
 - [MetadataDomainFilter](docs/MetadataDomainFilter.md)
 - [MetadataKeyFilter](docs/MetadataKeyFilter.md)
 - [OptionallyFilteredIdList](docs/OptionallyFilteredIdList.md)
 - [Ping](docs/Ping.md)
 - [QueryComponent](docs/QueryComponent.md)
 - [QueryConfig](docs/QueryConfig.md)
 - [QueryTerm](docs/QueryTerm.md)
 - [SelectResult](docs/SelectResult.md)
 - [SelectSpecification](docs/SelectSpecification.md)
 - [SessionState](docs/SessionState.md)
 - [SimilarityQuery](docs/SimilarityQuery.md)
 - [SimilarityQueryResult](docs/SimilarityQueryResult.md)
 - [SimilarityQueryResultBatch](docs/SimilarityQueryResultBatch.md)
 - [StartSessionMessage](docs/StartSessionMessage.md)
 - [StringDoublePair](docs/StringDoublePair.md)
 - [Tag](docs/Tag.md)
 - [TagIDsForElementQueryResult](docs/TagIDsForElementQueryResult.md)
 - [TagsQueryResult](docs/TagsQueryResult.md)


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

contact@vitrivr.org

