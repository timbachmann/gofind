# SegmentsApi

All URIs are relative to *http://city-stories.dmi.unibas.ch:5105*

Method | HTTP request | Description
------------- | ------------- | -------------
[**findSegmentSimilar**](SegmentsApi.md#findSegmentSimilar) | **POST** /api/v1/find/segments/similar | Find similar segments based on the given query



## findSegmentSimilar

> SimilarityQueryResultBatch findSegmentSimilar(similarityQuery)

Find similar segments based on the given query

Performs a similarity search based on the formulated query

### Example

```java
// Import classes:
//import org.openapitools.client.api.SegmentsApi;

SegmentsApi apiInstance = new SegmentsApi();
SimilarityQuery similarityQuery = new SimilarityQuery(); // SimilarityQuery | 
try {
    SimilarityQueryResultBatch result = apiInstance.findSegmentSimilar(similarityQuery);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SegmentsApi#findSegmentSimilar");
    e.printStackTrace();
}
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **similarityQuery** | [**SimilarityQuery**](SimilarityQuery.md)|  | [optional]

### Return type

[**SimilarityQueryResultBatch**](SimilarityQueryResultBatch.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

