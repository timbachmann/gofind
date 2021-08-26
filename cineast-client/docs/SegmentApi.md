# SegmentApi

All URIs are relative to *http://city-stories.dmi.unibas.ch:5105*

Method | HTTP request | Description
------------- | ------------- | -------------
[**findSegmentById**](SegmentApi.md#findSegmentById) | **GET** /api/v1/find/segments/by/id/{id} | Finds segments for specified id
[**findSegmentByIdBatched**](SegmentApi.md#findSegmentByIdBatched) | **POST** /api/v1/find/segments/by/id | Finds segments for specified ids
[**findSegmentByObjectId**](SegmentApi.md#findSegmentByObjectId) | **GET** /api/v1/find/segments/all/object/{id} | Find segments by their media object&#39;s id



## findSegmentById

> MediaSegmentQueryResult findSegmentById(id)

Finds segments for specified id

Finds segments for specified id

### Example

```java
// Import classes:
//import org.openapitools.client.api.SegmentApi;

SegmentApi apiInstance = new SegmentApi();
String id = null; // String | The id of the segments
try {
    MediaSegmentQueryResult result = apiInstance.findSegmentById(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SegmentApi#findSegmentById");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| The id of the segments | [default to null]

### Return type

[**MediaSegmentQueryResult**](MediaSegmentQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## findSegmentByIdBatched

> MediaSegmentQueryResult findSegmentByIdBatched(idList)

Finds segments for specified ids

Finds segments for specified ids

### Example

```java
// Import classes:
//import org.openapitools.client.api.SegmentApi;

SegmentApi apiInstance = new SegmentApi();
IdList idList = new IdList(); // IdList | 
try {
    MediaSegmentQueryResult result = apiInstance.findSegmentByIdBatched(idList);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SegmentApi#findSegmentByIdBatched");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **idList** | [**IdList**](IdList.md)|  | [optional]

### Return type

[**MediaSegmentQueryResult**](MediaSegmentQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


## findSegmentByObjectId

> MediaSegmentQueryResult findSegmentByObjectId(id)

Find segments by their media object&#39;s id

Find segments by their media object&#39;s id

### Example

```java
// Import classes:
//import org.openapitools.client.api.SegmentApi;

SegmentApi apiInstance = new SegmentApi();
String id = null; // String | The id of the media object to find segments fo
try {
    MediaSegmentQueryResult result = apiInstance.findSegmentByObjectId(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SegmentApi#findSegmentByObjectId");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| The id of the media object to find segments fo | [default to null]

### Return type

[**MediaSegmentQueryResult**](MediaSegmentQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

