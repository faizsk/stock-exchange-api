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

@Document(collection = "Indices")
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Index {

  @Id
  private String _id;

  @NotNull(message = "{indexcode.notnull}")
  @Size(min = 2, max = 15, message = "{indexcode.size}")
  private String indexCode;

  @NotNull(message = "{indexname.notnull}")
  @Size(min = 3, max = 15, message = "{indexname.size}")
  private String indexName;

  private Set<IndexStock> stocks;

  private Set<IndexValues> indexValues;

  public Index() {
  }

  public Index(String indexCode, String indexName) {
    this.indexCode = indexCode;
    this.indexName = indexName;
  }

  public Index(String indexCode, String indexName,
      Set<IndexStock> stocks) {
    this.indexCode = indexCode;
    this.indexName = indexName;
    this.stocks = stocks;
  }

  public Index(Set<IndexStock> stocks,
      Set<IndexValues> indexValues) {
    this.stocks = stocks;
    this.indexValues = indexValues;
  }

}
