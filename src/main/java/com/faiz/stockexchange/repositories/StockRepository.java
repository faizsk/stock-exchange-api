package com.faiz.stockexchange.repositories;

import com.faiz.stockexchange.domain.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockRepository extends MongoRepository<Stock, String> {

  // @Query(value = "{ 'stockName' : ?0 }")
  Stock findOneByStockName(String stockName);

  //@Query(value = "{ 'stockCode' : ?0 }")
  Stock findOneByStockCode(String stockCode);



}
