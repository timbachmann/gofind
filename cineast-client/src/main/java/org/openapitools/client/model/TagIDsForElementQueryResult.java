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
public class TagIDsForElementQueryResult {
  
  @SerializedName("queryId")
  private String queryId = null;
  @SerializedName("tagIDs")
  private List<String> tagIDs = null;
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
  public List<String> getTagIDs() {
    return tagIDs;
  }
  public void setTagIDs(List<String> tagIDs) {
    this.tagIDs = tagIDs;
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
    TagIDsForElementQueryResult tagIDsForElementQueryResult = (TagIDsForElementQueryResult) o;
    return (this.queryId == null ? tagIDsForElementQueryResult.queryId == null : this.queryId.equals(tagIDsForElementQueryResult.queryId)) &&
        (this.tagIDs == null ? tagIDsForElementQueryResult.tagIDs == null : this.tagIDs.equals(tagIDsForElementQueryResult.tagIDs)) &&
        (this.elementID == null ? tagIDsForElementQueryResult.elementID == null : this.elementID.equals(tagIDsForElementQueryResult.elementID));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.queryId == null ? 0: this.queryId.hashCode());
    result = 31 * result + (this.tagIDs == null ? 0: this.tagIDs.hashCode());
    result = 31 * result + (this.elementID == null ? 0: this.elementID.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class TagIDsForElementQueryResult {\n");
    
    sb.append("  queryId: ").append(queryId).append("\n");
    sb.append("  tagIDs: ").append(tagIDs).append("\n");
    sb.append("  elementID: ").append(elementID).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
