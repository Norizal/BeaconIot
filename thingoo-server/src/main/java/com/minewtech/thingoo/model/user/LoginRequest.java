package com.minewtech.thingoo.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginRequest {

  @ApiModelProperty(example = "demo@beaconyun.com", required = true)
  private String username = "";

  @ApiModelProperty(example = "demo", required = true)
  private String password = "";

}
