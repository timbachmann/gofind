# MiscApi

All URIs are relative to *http://city-stories.dmi.unibas.ch:5105*

Method | HTTP request | Description
------------- | ------------- | -------------
[**findDistinctElementsByColumn**](MiscApi.md#findDistinctElementsByColumn) | **POST** /api/v1/find/boolean/column/distinct | Find all distinct elements of a given column
[**selectFromTable**](MiscApi.md#selectFromTable) | **POST** /api/v1/find/boolean/table/select | Find all elements of given columns



## findDistinctElementsByColumn

> DistinctElementsResult findDistinctElementsByColumn(columnSpecification)

Find all distinct elements of a given column

Find all distinct elements of a given column. Please note that this operation does cache results.

### Example

```java
// Import classes:
//import org.openapitools.client.api.MiscApi;

MiscApi apiInstance = new MiscApi();
ColumnSpecification columnSpecification = new ColumnSpecification(); // ColumnSpecification | 
try {
    DistinctElementsResult result = apiInstance.findDistinctElementsByColumn(columnSpecification);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MiscApi#findDistinctElementsByColumn");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **columnSpecification** | [**ColumnSpecification**](ColumnSpecification.md)|  | [optional]

### Return type

[**DistinctElementsResult**](DistinctElementsResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


## selectFromTable

> SelectResult selectFromTable(selectSpecification)

Find all elements of given columns

Find all elements of given columns

### Example

```java
// Import classes:
//import org.openapitools.client.api.MiscApi;

MiscApi apiInstance = new MiscApi();
SelectSpecification selectSpecification = new SelectSpecification(); // SelectSpecification | 
try {
    SelectResult result = apiInstance.selectFromTable(selectSpecification);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MiscApi#selectFromTable");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **selectSpecification** | [**SelectSpecification**](SelectSpecification.md)|  | [optional]

### Return type

[**SelectResult**](SelectResult.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

