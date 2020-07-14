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

  public StatusMessageDTO(String message) {
    this.status = "error";
    this.message = message;
  }
}
