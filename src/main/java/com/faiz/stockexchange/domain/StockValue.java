package com.faiz.stockexchange.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class StockValue {

  @NotNull(message = "{datetime.notnull}")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dateTime;

  @NotNull(message = "{stockprice.notnull}")
  private Float stockPrice;

  public StockValue() {
  }

  public StockValue(LocalDateTime dateTime, Float stockPrice) {
    this.dateTime = dateTime;
    this.stockPrice = stockPrice;
  }

}
