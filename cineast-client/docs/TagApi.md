# TagApi

All URIs are relative to *http://city-stories.dmi.unibas.ch:5105*

Method | HTTP request | Description
------------- | ------------- | -------------
[**findAllTags**](TagApi.md#findAllTags) | **GET** /api/v1/find/tags/all | Find all tags
[**findTagsBy**](TagApi.md#findTagsBy) | **GET** /api/v1/find/tags/by/{attribute}/{value} | Find all tags specified by attribute value
[**findTagsById**](TagApi.md#findTagsById) | **POST** /api/v1/tags/by/id | Find all tags by ids



## findAllTags

> TagsQueryResult findAllTags()

Find all tags

### Example

```java
// Import classes:
//import org.openapitools.client.api.TagApi;

TagApi apiInstance = new TagApi();
try {
    TagsQueryResult result = apiInstance.findAllTags();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TagApi#findAllTags");
    e.printStackTrace();
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**TagsQueryResult**](TagsQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## findTagsBy

> TagsQueryResult findTagsBy(attribute, value)

Find all tags specified by attribute value

Find all tags by attributes id, name or matchingname and filter value

### Example

```java
// Import classes:
//import org.openapitools.client.api.TagApi;

TagApi apiInstance = new TagApi();
String attribute = null; // String | The attribute to filter on. One of: id, name, matchingname
String value = null; // String | The value of the attribute to filter
try {
    TagsQueryResult result = apiInstance.findTagsBy(attribute, value);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TagApi#findTagsBy");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **attribute** | **String**| The attribute to filter on. One of: id, name, matchingname | [default to null]
 **value** | **String**| The value of the attribute to filter | [default to null]

### Return type

[**TagsQueryResult**](TagsQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## findTagsById

> TagsQueryResult findTagsById(idList)

Find all tags by ids

### Example

```java
// Import classes:
//import org.openapitools.client.api.TagApi;

TagApi apiInstance = new TagApi();
IdList idList = new IdList(); // IdList | 
try {
    TagsQueryResult result = apiInstance.findTagsById(idList);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TagApi#findTagsById");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **idList** | [**IdList**](IdList.md)|  | [optional]

### Return type

[**TagsQueryResult**](TagsQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

