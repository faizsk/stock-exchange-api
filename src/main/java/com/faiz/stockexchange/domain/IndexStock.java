package com.faiz.stockexchange.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode

public class IndexStock {

  @NotNull(message = "{indexcode.notnull}")
  @Size(min = 2, max = 15, message = "{indexcode.size}")
  private String stockCode;

  @NotNull(message = "{weight.notnull}")
  private Double weight;

  public IndexStock() {
  }

  public IndexStock(String stockCode, Double weight) {
    this.stockCode = stockCode;
    this.weight = weight;
  }


}
