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
public class MediaObjectDescriptor {
  
  @SerializedName("objectId")
  private String objectId = null;
  @SerializedName("name")
  private String name = null;
  @SerializedName("path")
  private String path = null;
  public enum MediatypeEnum {
     VIDEO,  IMAGE,  AUDIO,  MODEL3D,  IMAGE_SEQUENCE,  UNKNOWN, 
  };
  @SerializedName("mediatype")
  private MediatypeEnum mediatype = null;
  @SerializedName("exists")
  private Boolean exists = null;
  @SerializedName("contentURL")
  private String contentURL = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public String getObjectId() {
    return objectId;
  }
  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getPath() {
    return path;
  }
  public void setPath(String path) {
    this.path = path;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public MediatypeEnum getMediatype() {
    return mediatype;
  }
  public void setMediatype(MediatypeEnum mediatype) {
    this.mediatype = mediatype;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Boolean getExists() {
    return exists;
  }
  public void setExists(Boolean exists) {
    this.exists = exists;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getContentURL() {
    return contentURL;
  }
  public void setContentURL(String contentURL) {
    this.contentURL = contentURL;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaObjectDescriptor mediaObjectDescriptor = (MediaObjectDescriptor) o;
    return (this.objectId == null ? mediaObjectDescriptor.objectId == null : this.objectId.equals(mediaObjectDescriptor.objectId)) &&
        (this.name == null ? mediaObjectDescriptor.name == null : this.name.equals(mediaObjectDescriptor.name)) &&
        (this.path == null ? mediaObjectDescriptor.path == null : this.path.equals(mediaObjectDescriptor.path)) &&
        (this.mediatype == null ? mediaObjectDescriptor.mediatype == null : this.mediatype.equals(mediaObjectDescriptor.mediatype)) &&
        (this.exists == null ? mediaObjectDescriptor.exists == null : this.exists.equals(mediaObjectDescriptor.exists)) &&
        (this.contentURL == null ? mediaObjectDescriptor.contentURL == null : this.contentURL.equals(mediaObjectDescriptor.contentURL));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.objectId == null ? 0: this.objectId.hashCode());
    result = 31 * result + (this.name == null ? 0: this.name.hashCode());
    result = 31 * result + (this.path == null ? 0: this.path.hashCode());
    result = 31 * result + (this.mediatype == null ? 0: this.mediatype.hashCode());
    result = 31 * result + (this.exists == null ? 0: this.exists.hashCode());
    result = 31 * result + (this.contentURL == null ? 0: this.contentURL.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaObjectDescriptor {\n");
    
    sb.append("  objectId: ").append(objectId).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  path: ").append(path).append("\n");
    sb.append("  mediatype: ").append(mediatype).append("\n");
    sb.append("  exists: ").append(exists).append("\n");
    sb.append("  contentURL: ").append(contentURL).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
