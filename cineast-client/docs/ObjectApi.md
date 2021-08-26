# ObjectApi

All URIs are relative to *http://city-stories.dmi.unibas.ch:5105*

Method | HTTP request | Description
------------- | ------------- | -------------
[**findObjectsAll**](ObjectApi.md#findObjectsAll) | **GET** /api/v1/find/objects/all | Find all objects for a certain type
[**findObjectsByAttribute**](ObjectApi.md#findObjectsByAttribute) | **GET** /api/v1/find/object/by/{attribute}/{value} | Find object by specified attribute value. I.e by id, name or path
[**findObjectsByIdBatched**](ObjectApi.md#findObjectsByIdBatched) | **POST** /api/v1/find/object/by/id | Find objects by id



## findObjectsAll

> MediaObjectQueryResult findObjectsAll()

Find all objects for a certain type

Find all objects for a certain type

### Example

```java
// Import classes:
//import org.openapitools.client.api.ObjectApi;

ObjectApi apiInstance = new ObjectApi();
try {
    MediaObjectQueryResult result = apiInstance.findObjectsAll();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ObjectApi#findObjectsAll");
    e.printStackTrace();
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**MediaObjectQueryResult**](MediaObjectQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## findObjectsByAttribute

> MediaObjectQueryResult findObjectsByAttribute(attribute, value)

Find object by specified attribute value. I.e by id, name or path

Find object by specified attribute value. I.e by id, name or path

### Example

```java
// Import classes:
//import org.openapitools.client.api.ObjectApi;

ObjectApi apiInstance = new ObjectApi();
String attribute = null; // String | The attribute type of the value. One of: id, name, path
String value = null; // String | 
try {
    MediaObjectQueryResult result = apiInstance.findObjectsByAttribute(attribute, value);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ObjectApi#findObjectsByAttribute");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **attribute** | **String**| The attribute type of the value. One of: id, name, path | [default to null]
 **value** | **String**|  | [default to null]

### Return type

[**MediaObjectQueryResult**](MediaObjectQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## findObjectsByIdBatched

> MediaObjectQueryResult findObjectsByIdBatched(idList)

Find objects by id

Find objects by id

### Example

```java
// Import classes:
//import org.openapitools.client.api.ObjectApi;

ObjectApi apiInstance = new ObjectApi();
IdList idList = new IdList(); // IdList | 
try {
    MediaObjectQueryResult result = apiInstance.findObjectsByIdBatched(idList);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ObjectApi#findObjectsByIdBatched");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **idList** | [**IdList**](IdList.md)|  | [optional]

### Return type

[**MediaObjectQueryResult**](MediaObjectQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

