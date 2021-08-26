# DefaultApi

All URIs are relative to *http://city-stories.dmi.unibas.ch:5105*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getObjectsWithId**](DefaultApi.md#getObjectsWithId) | **GET** /objects/{id} | Get objects with id
[**getThumbnailsWithId**](DefaultApi.md#getThumbnailsWithId) | **GET** /thumbnails/{id} | Get thumbnails with id



## getObjectsWithId

> getObjectsWithId(id)

Get objects with id

### Example

```java
// Import classes:
//import org.openapitools.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String id = null; // String | 
try {
    apiInstance.getObjectsWithId(id);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#getObjectsWithId");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  | [default to null]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


## getThumbnailsWithId

> getThumbnailsWithId(id)

Get thumbnails with id

### Example

```java
// Import classes:
//import org.openapitools.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String id = null; // String | 
try {
    apiInstance.getThumbnailsWithId(id);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#getThumbnailsWithId");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  | [default to null]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined

