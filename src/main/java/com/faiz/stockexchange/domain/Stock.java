package com.faiz.stockexchange.domain;

import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Stocks")
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Stock {

  @Id
  private String _id;

  @NotNull(message = "{stockcode.notnull}")
  @Size(min = 2, max = 15, message = "{stockcode.size}")
  private String stockCode;

  @NotNull(message = "{stockname.notnull}")
  @Size(min = 3, max = 15, message = "{stockname.size}")
  private String stockName;

  private Set<StockValue> stockValue;

  public Stock() {

  }

  public Stock(String stockCode, String stockName) {
    this.stockCode = stockCode;
    this.stockName = stockName;
  }

  public Stock(String stockCode, String stockName,
      Set<StockValue> stockValue) {
    this.stockCode = stockCode;
    this.stockName = stockName;
    this.stockValue = stockValue;
  }


}

