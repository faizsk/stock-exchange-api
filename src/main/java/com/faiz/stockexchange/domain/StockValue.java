package com.faiz.stockexchange.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@Document
public class StockValue {

  @Id
  @Field("id")
  private String _id;

  @NotNull(message = "{datetime.notnull}")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dateTime;

  @NotNull(message = "{stockprice.notnull}")
  private Float stockPrice;

  public StockValue() {
    this._id = ObjectId.get().toString();
  }

  public StockValue(LocalDateTime dateTime, Float stockPrice) {
    this.dateTime = dateTime;
    this.stockPrice = stockPrice;
  }

  public StockValue(String _id, LocalDateTime dateTime, Float stockPrice) {
    this._id = _id;
    this.dateTime = dateTime;
    this.stockPrice = stockPrice;
  }
}
