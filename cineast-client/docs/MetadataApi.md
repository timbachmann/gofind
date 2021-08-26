# MetadataApi

All URIs are relative to *http://city-stories.dmi.unibas.ch:5105*

Method | HTTP request | Description
------------- | ------------- | -------------
[**findMetaById**](MetadataApi.md#findMetaById) | **GET** /api/v1/find/metadata/by/id/{id} | Find metadata for the given object id
[**findMetaFullyQualified**](MetadataApi.md#findMetaFullyQualified) | **GET** /api/v1/find/metadata/of/{id}/in/{domain}/with/{key} | Find metadata for specific object id in given domain with given key
[**findMetadataByDomain**](MetadataApi.md#findMetadataByDomain) | **GET** /api/v1/find/metadata/in/{domain}/by/id/{domain} | Find metadata for specific object id in given domain
[**findMetadataByDomainBatched**](MetadataApi.md#findMetadataByDomainBatched) | **POST** /api/v1/find/metadata/in/{domain} | Find metadata in the specified domain for all the given ids
[**findMetadataByKey**](MetadataApi.md#findMetadataByKey) | **GET** /api/v1/find/metadata/with/{key}/by/id/{id} | Find metadata for a given object id with specified key
[**findMetadataByKeyBatched**](MetadataApi.md#findMetadataByKeyBatched) | **POST** /api/v1/find/metadata/with/{key} | Find metadata for a given object id with specified key
[**findMetadataForObjectIdBatched**](MetadataApi.md#findMetadataForObjectIdBatched) | **POST** /api/v1/find/metadata/by/id | Finds metadata for the given list of object ids
[**findSegFeatById**](MetadataApi.md#findSegFeatById) | **GET** /api/v1/find/feature/all/by/id/{id} | Find features for the given id
[**findSegMetaById**](MetadataApi.md#findSegMetaById) | **GET** /api/v1/find/metadata/by/segmentid/{id} | Find metadata for the given segment id
[**findTagsByIdId**](MetadataApi.md#findTagsByIdId) | **GET** /api/v1/find/feature/tags/by/id/{id} | Find tag ids for the given id
[**findTextByIDAndCat**](MetadataApi.md#findTextByIDAndCat) | **GET** /api/v1/find/feature/text/by/{id}/{category} | Find Text for the given id and retrieval category



## findMetaById

> MediaObjectMetadataQueryResult findMetaById(id)

Find metadata for the given object id

Find metadata by the given object id

### Example

```java
// Import classes:
//import org.openapitools.client.api.MetadataApi;

MetadataApi apiInstance = new MetadataApi();
String id = null; // String | The object id to find metadata of
try {
    MediaObjectMetadataQueryResult result = apiInstance.findMetaById(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#findMetaById");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| The object id to find metadata of | [default to null]

### Return type

[**MediaObjectMetadataQueryResult**](MediaObjectMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## findMetaFullyQualified

> MediaObjectMetadataQueryResult findMetaFullyQualified(id, domain, key)

Find metadata for specific object id in given domain with given key

The description

### Example

```java
// Import classes:
//import org.openapitools.client.api.MetadataApi;

MetadataApi apiInstance = new MetadataApi();
String id = null; // String | The object id
String domain = null; // String | The domain name
String key = null; // String | Metadata key
try {
    MediaObjectMetadataQueryResult result = apiInstance.findMetaFullyQualified(id, domain, key);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#findMetaFullyQualified");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| The object id | [default to null]
 **domain** | **String**| The domain name | [default to null]
 **key** | **String**| Metadata key | [default to null]

### Return type

[**MediaObjectMetadataQueryResult**](MediaObjectMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## findMetadataByDomain

> MediaObjectMetadataQueryResult findMetadataByDomain(domain, id)

Find metadata for specific object id in given domain

Find metadata for specific object id in given domain

### Example

```java
// Import classes:
//import org.openapitools.client.api.MetadataApi;

MetadataApi apiInstance = new MetadataApi();
String domain = null; // String | The domain of the metadata to find
String id = null; // String | The object id of the multimedia object to find metadata for
try {
    MediaObjectMetadataQueryResult result = apiInstance.findMetadataByDomain(domain, id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#findMetadataByDomain");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| The domain of the metadata to find | [default to null]
 **id** | **String**| The object id of the multimedia object to find metadata for | [default to null]

### Return type

[**MediaObjectMetadataQueryResult**](MediaObjectMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## findMetadataByDomainBatched

> MediaObjectMetadataQueryResult findMetadataByDomainBatched(domain, idList)

Find metadata in the specified domain for all the given ids

Find metadata in the specified domain for all the given ids

### Example

```java
// Import classes:
//import org.openapitools.client.api.MetadataApi;

MetadataApi apiInstance = new MetadataApi();
String domain = null; // String | The domain of the metadata to find
IdList idList = new IdList(); // IdList | 
try {
    MediaObjectMetadataQueryResult result = apiInstance.findMetadataByDomainBatched(domain, idList);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#findMetadataByDomainBatched");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| The domain of the metadata to find | [default to null]
 **idList** | [**IdList**](IdList.md)|  | [optional]

### Return type

[**MediaObjectMetadataQueryResult**](MediaObjectMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


## findMetadataByKey

> MediaObjectMetadataQueryResult findMetadataByKey(key, id)

Find metadata for a given object id with specified key

Find metadata for a given object id with specified key

### Example

```java
// Import classes:
//import org.openapitools.client.api.MetadataApi;

MetadataApi apiInstance = new MetadataApi();
String key = null; // String | The key of the metadata to find
String id = null; // String | The object id of for which the metadata should be find
try {
    MediaObjectMetadataQueryResult result = apiInstance.findMetadataByKey(key, id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#findMetadataByKey");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **key** | **String**| The key of the metadata to find | [default to null]
 **id** | **String**| The object id of for which the metadata should be find | [default to null]

### Return type

[**MediaObjectMetadataQueryResult**](MediaObjectMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## findMetadataByKeyBatched

> MediaObjectMetadataQueryResult findMetadataByKeyBatched(key, idList)

Find metadata for a given object id with specified key

Find metadata with a the speicifed key from the path across all domains and for the provided ids

### Example

```java
// Import classes:
//import org.openapitools.client.api.MetadataApi;

MetadataApi apiInstance = new MetadataApi();
String key = null; // String | The key of the metadata to find
IdList idList = new IdList(); // IdList | 
try {
    MediaObjectMetadataQueryResult result = apiInstance.findMetadataByKeyBatched(key, idList);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#findMetadataByKeyBatched");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **key** | **String**| The key of the metadata to find | [default to null]
 **idList** | [**IdList**](IdList.md)|  | [optional]

### Return type

[**MediaObjectMetadataQueryResult**](MediaObjectMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


## findMetadataForObjectIdBatched

> MediaObjectMetadataQueryResult findMetadataForObjectIdBatched(optionallyFilteredIdList)

Finds metadata for the given list of object ids

Finds metadata for the given list of object ids

### Example

```java
// Import classes:
//import org.openapitools.client.api.MetadataApi;

MetadataApi apiInstance = new MetadataApi();
OptionallyFilteredIdList optionallyFilteredIdList = new OptionallyFilteredIdList(); // OptionallyFilteredIdList | 
try {
    MediaObjectMetadataQueryResult result = apiInstance.findMetadataForObjectIdBatched(optionallyFilteredIdList);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#findMetadataForObjectIdBatched");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **optionallyFilteredIdList** | [**OptionallyFilteredIdList**](OptionallyFilteredIdList.md)|  | [optional]

### Return type

[**MediaObjectMetadataQueryResult**](MediaObjectMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


## findSegFeatById

> FeaturesAllCategoriesQueryResult findSegFeatById(id)

Find features for the given id

Find features by the given id

### Example

```java
// Import classes:
//import org.openapitools.client.api.MetadataApi;

MetadataApi apiInstance = new MetadataApi();
String id = null; // String | The id to find features of
try {
    FeaturesAllCategoriesQueryResult result = apiInstance.findSegFeatById(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#findSegFeatById");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| The id to find features of | [default to null]

### Return type

[**FeaturesAllCategoriesQueryResult**](FeaturesAllCategoriesQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## findSegMetaById

> MediaSegmentMetadataQueryResult findSegMetaById(id)

Find metadata for the given segment id

Find metadata by the given segment id

### Example

```java
// Import classes:
//import org.openapitools.client.api.MetadataApi;

MetadataApi apiInstance = new MetadataApi();
String id = null; // String | The segment id to find metadata of
try {
    MediaSegmentMetadataQueryResult result = apiInstance.findSegMetaById(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#findSegMetaById");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| The segment id to find metadata of | [default to null]

### Return type

[**MediaSegmentMetadataQueryResult**](MediaSegmentMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## findTagsByIdId

> TagIDsForElementQueryResult findTagsByIdId(id)

Find tag ids for the given id

Find tag ids for the given id

### Example

```java
// Import classes:
//import org.openapitools.client.api.MetadataApi;

MetadataApi apiInstance = new MetadataApi();
String id = null; // String | The id to find tagids of
try {
    TagIDsForElementQueryResult result = apiInstance.findTagsByIdId(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#findTagsByIdId");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| The id to find tagids of | [default to null]

### Return type

[**TagIDsForElementQueryResult**](TagIDsForElementQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## findTextByIDAndCat

> FeaturesTextCategoryQueryResult findTextByIDAndCat(id, category)

Find Text for the given id and retrieval category

Find Text by the given id and retrieval category

### Example

```java
// Import classes:
//import org.openapitools.client.api.MetadataApi;

MetadataApi apiInstance = new MetadataApi();
String id = null; // String | The id to find text of
String category = null; // String | The category for which retrieval shall be performed
try {
    FeaturesTextCategoryQueryResult result = apiInstance.findTextByIDAndCat(id, category);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MetadataApi#findTextByIDAndCat");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| The id to find text of | [default to null]
 **category** | **String**| The category for which retrieval shall be performed | [default to null]

### Return type

[**FeaturesTextCategoryQueryResult**](FeaturesTextCategoryQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

