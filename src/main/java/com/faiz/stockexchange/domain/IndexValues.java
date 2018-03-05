package com.faiz.stockexchange.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class IndexValues {

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dateTime;

  private double indexValue;

  public IndexValues() {
  }

  public IndexValues(double indexValue, LocalDateTime dateTime) {
    this.indexValue = indexValue;
    this.dateTime = dateTime;
  }

}
