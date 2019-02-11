/**
    This is the common structure for all responses
    if the response contains a list(array) then it will have 'items' field
    if the response contains a single item then it will have 'item'  field
 */


package com.minewtech.thingoo.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data //for getters and setters
public class OperationResponse {
  @ApiModelProperty(required = true)
  private int status;
  @JsonInclude(Include.NON_NULL)
  private String  message;
  @JsonInclude(Include.NON_NULL)
  private Object data;

  @JsonInclude(Include.NON_NULL)
  private String loggedInUserId;
}
