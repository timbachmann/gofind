# StatusApi

All URIs are relative to *http://city-stories.dmi.unibas.ch:5105*

Method | HTTP request | Description
------------- | ------------- | -------------
[**status**](StatusApi.md#status) | **GET** /api/v1/status | Get the status of the server



## status

> Ping status()

Get the status of the server

### Example

```java
// Import classes:
//import org.openapitools.client.api.StatusApi;

StatusApi apiInstance = new StatusApi();
try {
    Ping result = apiInstance.status();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling StatusApi#status");
    e.printStackTrace();
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**Ping**](Ping.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

