/**
 * Cineast RESTful API
 * Cineast is vitrivr's content-based multimedia retrieval engine. This is it's RESTful API.
 *
 * The version of the OpenAPI document: v1
 * Contact: contact@vitrivr.org
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package org.openapitools.client.model;

import java.util.*;
import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;

@ApiModel(description = "")
public class FeaturesTextCategoryQueryResult {
  
  @SerializedName("queryId")
  private String queryId = null;
  @SerializedName("featureValues")
  private List<String> featureValues = null;
  @SerializedName("category")
  private String category = null;
  @SerializedName("elementID")
  private String elementID = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public String getQueryId() {
    return queryId;
  }
  public void setQueryId(String queryId) {
    this.queryId = queryId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<String> getFeatureValues() {
    return featureValues;
  }
  public void setFeatureValues(List<String> featureValues) {
    this.featureValues = featureValues;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getElementID() {
    return elementID;
  }
  public void setElementID(String elementID) {
    this.elementID = elementID;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FeaturesTextCategoryQueryResult featuresTextCategoryQueryResult = (FeaturesTextCategoryQueryResult) o;
    return (this.queryId == null ? featuresTextCategoryQueryResult.queryId == null : this.queryId.equals(featuresTextCategoryQueryResult.queryId)) &&
        (this.featureValues == null ? featuresTextCategoryQueryResult.featureValues == null : this.featureValues.equals(featuresTextCategoryQueryResult.featureValues)) &&
        (this.category == null ? featuresTextCategoryQueryResult.category == null : this.category.equals(featuresTextCategoryQueryResult.category)) &&
        (this.elementID == null ? featuresTextCategoryQueryResult.elementID == null : this.elementID.equals(featuresTextCategoryQueryResult.elementID));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.queryId == null ? 0: this.queryId.hashCode());
    result = 31 * result + (this.featureValues == null ? 0: this.featureValues.hashCode());
    result = 31 * result + (this.category == null ? 0: this.category.hashCode());
    result = 31 * result + (this.elementID == null ? 0: this.elementID.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class FeaturesTextCategoryQueryResult {\n");
    
    sb.append("  queryId: ").append(queryId).append("\n");
    sb.append("  featureValues: ").append(featureValues).append("\n");
    sb.append("  category: ").append(category).append("\n");
    sb.append("  elementID: ").append(elementID).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
