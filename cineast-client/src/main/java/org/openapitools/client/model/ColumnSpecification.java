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

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;

@ApiModel(description = "")
public class ColumnSpecification {
  
  @SerializedName("column")
  private String column = null;
  @SerializedName("table")
  private String table = null;
  public enum MessageTypeEnum {
     PING,  Q_SIM,  Q_MLT,  Q_NESEG,  Q_SEG,  M_LOOKUP,  Q_TEMPORAL,  SESSION_START,  QR_START,  QR_END,  QR_ERROR,  QR_OBJECT,  QR_METADATA_O,  QR_METADATA_S,  QR_SEGMENT,  QR_SIMILARITY, 
  };
  @SerializedName("messageType")
  private MessageTypeEnum messageType = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public String getColumn() {
    return column;
  }
  public void setColumn(String column) {
    this.column = column;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getTable() {
    return table;
  }
  public void setTable(String table) {
    this.table = table;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public MessageTypeEnum getMessageType() {
    return messageType;
  }
  public void setMessageType(MessageTypeEnum messageType) {
    this.messageType = messageType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ColumnSpecification columnSpecification = (ColumnSpecification) o;
    return (this.column == null ? columnSpecification.column == null : this.column.equals(columnSpecification.column)) &&
        (this.table == null ? columnSpecification.table == null : this.table.equals(columnSpecification.table)) &&
        (this.messageType == null ? columnSpecification.messageType == null : this.messageType.equals(columnSpecification.messageType));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.column == null ? 0: this.column.hashCode());
    result = 31 * result + (this.table == null ? 0: this.table.hashCode());
    result = 31 * result + (this.messageType == null ? 0: this.messageType.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ColumnSpecification {\n");
    
    sb.append("  column: ").append(column).append("\n");
    sb.append("  table: ").append(table).append("\n");
    sb.append("  messageType: ").append(messageType).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
