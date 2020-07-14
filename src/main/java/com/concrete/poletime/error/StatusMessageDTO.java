package com.concrete.poletime.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StatusMessageDTO {

  private String status;
  private String message;
  private String type;

  public StatusMessageDTO(String message, String type) {
    this.status = "error";
    this.message = message;
    this.type = type;
  }
}
