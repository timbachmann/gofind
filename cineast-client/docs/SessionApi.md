# SessionApi

All URIs are relative to *http://city-stories.dmi.unibas.ch:5105*

Method | HTTP request | Description
------------- | ------------- | -------------
[**endExtraction**](SessionApi.md#endExtraction) | **POST** /api/v1/session/extract/end | End the active extraction session
[**endSession**](SessionApi.md#endSession) | **GET** /api/v1/session/end/{id} | End the session for given id
[**extractItem**](SessionApi.md#extractItem) | **POST** /api/v1/session/extract/new | Extract new item
[**startExtraction**](SessionApi.md#startExtraction) | **POST** /api/v1/session/extract/start | Start extraction session
[**startSession**](SessionApi.md#startSession) | **POST** /api/v1/session/start | Start new session for given credentials
[**validateSession**](SessionApi.md#validateSession) | **GET** /api/v1/session/validate/{id} | Validates the session with given id



## endExtraction

> SessionState endExtraction()

End the active extraction session

CAUTION. Untested

### Example

```java
// Import classes:
//import org.openapitools.client.api.SessionApi;

SessionApi apiInstance = new SessionApi();
try {
    SessionState result = apiInstance.endExtraction();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SessionApi#endExtraction");
    e.printStackTrace();
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**SessionState**](SessionState.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## endSession

> SessionState endSession(id)

End the session for given id

Ends the session for the given id

### Example

```java
// Import classes:
//import org.openapitools.client.api.SessionApi;

SessionApi apiInstance = new SessionApi();
String id = null; // String | The id of the session to end
try {
    SessionState result = apiInstance.endSession(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SessionApi#endSession");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| The id of the session to end | [default to null]

### Return type

[**SessionState**](SessionState.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## extractItem

> SessionState extractItem(extractionContainerMessage)

Extract new item

### Example

```java
// Import classes:
//import org.openapitools.client.api.SessionApi;

SessionApi apiInstance = new SessionApi();
ExtractionContainerMessage extractionContainerMessage = new ExtractionContainerMessage(); // ExtractionContainerMessage | 
try {
    SessionState result = apiInstance.extractItem(extractionContainerMessage);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SessionApi#extractItem");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **extractionContainerMessage** | [**ExtractionContainerMessage**](ExtractionContainerMessage.md)|  | [optional]

### Return type

[**SessionState**](SessionState.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


## startExtraction

> SessionState startExtraction()

Start extraction session

Changes the session&#39;s state to extraction

### Example

```java
// Import classes:
//import org.openapitools.client.api.SessionApi;

SessionApi apiInstance = new SessionApi();
try {
    SessionState result = apiInstance.startExtraction();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SessionApi#startExtraction");
    e.printStackTrace();
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**SessionState**](SessionState.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## startSession

> SessionState startSession(startSessionMessage)

Start new session for given credentials

### Example

```java
// Import classes:
//import org.openapitools.client.api.SessionApi;

SessionApi apiInstance = new SessionApi();
StartSessionMessage startSessionMessage = new StartSessionMessage(); // StartSessionMessage | 
try {
    SessionState result = apiInstance.startSession(startSessionMessage);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SessionApi#startSession");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **startSessionMessage** | [**StartSessionMessage**](StartSessionMessage.md)|  | [optional]

### Return type

[**SessionState**](SessionState.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


## validateSession

> SessionState validateSession(id)

Validates the session with given id

### Example

```java
// Import classes:
//import org.openapitools.client.api.SessionApi;

SessionApi apiInstance = new SessionApi();
String id = null; // String | The id to validate the session of
try {
    SessionState result = apiInstance.validateSession(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SessionApi#validateSession");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| The id to validate the session of | [default to null]

### Return type

[**SessionState**](SessionState.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

