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
public class OptionallyFilteredIdList {

  @SerializedName("ids")
  private List<String> ids = null;
  public enum MessageTypeEnum {
     PING,  Q_SIM,  Q_MLT,  Q_NESEG,  Q_SEG,  M_LOOKUP,  Q_TEMPORAL,  SESSION_START,  QR_START,  QR_END,  QR_ERROR,  QR_OBJECT,  QR_METADATA_O,  QR_METADATA_S,  QR_SEGMENT,  QR_SIMILARITY, 
  };

  /**
   **/
  @ApiModelProperty(value = "")
  public List<String> getIds() {
    return ids;
  }
  public void setIds(List<String> ids) {
    this.ids = ids;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OptionallyFilteredIdList optionallyFilteredIdList = (OptionallyFilteredIdList) o;
    return (this.ids == null ? optionallyFilteredIdList.ids == null : this.ids.equals(optionallyFilteredIdList.ids));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.ids == null ? 0: this.ids.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class OptionallyFilteredIdList {\n");
    sb.append("  ids: ").append(ids).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
