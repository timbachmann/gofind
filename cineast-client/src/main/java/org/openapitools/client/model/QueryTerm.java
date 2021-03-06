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
public class QueryTerm {
  
  public enum TypeEnum {
     IMAGE,  AUDIO,  MOTION,  MODEL3D,  LOCATION,  TIME,  TEXT,  TAG,  SEMANTIC,  ID,  BOOLEAN, 
  };
  @SerializedName("type")
  private TypeEnum type = null;
  @SerializedName("data")
  private String data = null;
  @SerializedName("categories")
  private List<String> categories = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public TypeEnum getType() {
    return type;
  }
  public void setType(TypeEnum type) {
    this.type = type;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getData() {
    return data;
  }
  public void setData(String data) {
    this.data = data;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<String> getCategories() {
    return categories;
  }
  public void setCategories(List<String> categories) {
    this.categories = categories;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueryTerm queryTerm = (QueryTerm) o;
    return (this.type == null ? queryTerm.type == null : this.type.equals(queryTerm.type)) &&
        (this.data == null ? queryTerm.data == null : this.data.equals(queryTerm.data)) &&
        (this.categories == null ? queryTerm.categories == null : this.categories.equals(queryTerm.categories));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.type == null ? 0: this.type.hashCode());
    result = 31 * result + (this.data == null ? 0: this.data.hashCode());
    result = 31 * result + (this.categories == null ? 0: this.categories.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryTerm {\n");
    
    sb.append("  type: ").append(type).append("\n");
    sb.append("  data: ").append(data).append("\n");
    sb.append("  categories: ").append(categories).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
