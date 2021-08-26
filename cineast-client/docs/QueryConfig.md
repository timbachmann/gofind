

# QueryConfig

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**queryId** | [**UUID**](UUID.md) |  |  [optional]
**hints** | [**Set&lt;HintsEnum&gt;**](#Set&lt;HintsEnum&gt;) |  |  [optional]
**distance** | [**DistanceEnum**](#DistanceEnum) |  |  [optional]
**distanceWeights** | **List&lt;Float&gt;** |  |  [optional]
**norm** | **Float** |  |  [optional]
**resultsPerModule** | **Integer** |  |  [optional]
**maxResults** | **Integer** |  |  [optional]
**relevantSegmentIds** | **Set&lt;String&gt;** |  |  [optional]
**correspondenceFunctionIfEmpty** | [**QueryConfig**](QueryConfig.md) |  |  [optional]
**distanceIfEmpty** | [**QueryConfig**](QueryConfig.md) |  |  [optional]
**normIfEmpty** | [**QueryConfig**](QueryConfig.md) |  |  [optional]
**distanceWeightsIfEmpty** | [**QueryConfig**](QueryConfig.md) |  |  [optional]
**correspondenceFunction** | **Object** |  |  [optional]
**rawResultsPerModule** | **Integer** |  |  [optional]


## Enum: Set&lt;HintsEnum&gt;

Name | Value
---- | -----
EXACT | &quot;exact&quot;
INEXACT | &quot;inexact&quot;
LSH | &quot;lsh&quot;
ECP | &quot;ecp&quot;
MI | &quot;mi&quot;
PQ | &quot;pq&quot;
SH | &quot;sh&quot;
VA | &quot;va&quot;
VAF | &quot;vaf&quot;
VAV | &quot;vav&quot;
SEQUENTIAL | &quot;sequential&quot;
EMPIRICAL | &quot;empirical&quot;


## Enum: DistanceEnum

Name | Value
---- | -----




