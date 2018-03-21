package com.faiz.stockexchange.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
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
public class IndexValues {

  @Id
  @Field("id")
  private String _id;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dateTime;

  private double indexValue;

  public IndexValues() {
    this._id = ObjectId.get().toString();
  }

  public IndexValues(double indexValue, LocalDateTime dateTime) {
    this.indexValue = indexValue;
    this.dateTime = dateTime;
  }

  public IndexValues(String _id, LocalDateTime dateTime, double indexValue) {
    this._id = _id;
    this.dateTime = dateTime;
    this.indexValue = indexValue;
  }
}
