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
import org.openapitools.client.model.AbstractMetadataFilterDescriptor;
import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;

@ApiModel(description = "")
public class MetadataKeyFilter extends AbstractMetadataFilterDescriptor {
  
  @SerializedName("keywords")
  private List<String> keywords = null;
  @SerializedName("type")
  private String type = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public List<String> getKeywords() {
    return keywords;
  }
  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetadataKeyFilter metadataKeyFilter = (MetadataKeyFilter) o;
    return (this.keywords == null ? metadataKeyFilter.keywords == null : this.keywords.equals(metadataKeyFilter.keywords)) &&
        (this.type == null ? metadataKeyFilter.type == null : this.type.equals(metadataKeyFilter.type));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.keywords == null ? 0: this.keywords.hashCode());
    result = 31 * result + (this.type == null ? 0: this.type.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetadataKeyFilter {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  keywords: ").append(keywords).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
