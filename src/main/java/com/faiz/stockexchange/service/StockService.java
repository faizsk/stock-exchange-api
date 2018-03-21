package com.faiz.stockexchange.service;

import com.faiz.stockexchange.domain.Stock;
import com.faiz.stockexchange.domain.StockValue;
import com.faiz.stockexchange.repositories.StockRepository;
import com.mongodb.BasicDBObject;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class StockService {

  @Autowired
  MongoTemplate mongoTemplate;

  private StockRepository stockRepository;

  public StockService(StockRepository stockRepository) {
    this.stockRepository = stockRepository;
  }

  public List<Stock> getAllStocks() {
    return stockRepository.findAll(new Sort(Sort.Direction.DESC, "stockValue"));
  }

  private Sort sortByIdAsc() {
    return new Sort(Sort.Direction.ASC, "stockValue.dateTime");
  }

  public Stock getStockByStockName(String stockName) {
    return stockRepository.findOneByStockName(stockName);
  }

  public Stock getStockByStockCode(String stockCode) {
    return stockRepository.findOneByStockCode(stockCode.toLowerCase());
  }

  public void createStock(Stock stock) {
    stock.setStockCode(stock.getStockCode().toLowerCase());
    stockRepository.save(stock);
  }

  public boolean deleteStockByStockCode(String stockCode) {
    Query deleteStockQuery = new Query(Criteria.where("stockCode").is(stockCode));
    mongoTemplate.remove(deleteStockQuery, Stock.class);
    return !isStockCodeExists(stockCode);
  }

  public void deleteStockValue(String stockCode, String stockValueIndex) {
    Query deleteStockQuery = new Query(Criteria.where("stockCode").is(stockCode));
    System.out.println(mongoTemplate.updateFirst(deleteStockQuery,
        new Update().pull("stockValue", new BasicDBObject("_id", stockValueIndex)), Stock.class));
  }

  public void updateStock(Stock stock) {
    Query upsertStockValueQuery = new Query(Criteria.where("stockName").is(stock.getStockName()));
    for (StockValue stockValue : stock.getStockValue()) {
      Criteria criteria = new Criteria()
          .andOperator(Criteria.where("stockName").is(stock.getStockName()),
              Criteria.where("stockValue")
                  .elemMatch(Criteria.where("dateTime").is(stockValue.getDateTime())));
      Query isDateTimeExitsQuery = new Query(criteria);

      if (!mongoTemplate.exists(isDateTimeExitsQuery, "Stocks")) {
        mongoTemplate
            .upsert(upsertStockValueQuery, new Update().push("stockValue", stockValue),
                Stock.class);
      }
    }
  }

  public boolean isStockNameExists(String stockName) {
    Query isStockNameExistsQuery = new Query(Criteria.where("stockName").is(stockName));
    return mongoTemplate.exists(isStockNameExistsQuery, "Stocks");
  }

  public boolean isStockCodeExists(String stockCode) {
    Query isStockCodeExistsQuery = new Query(Criteria.where("stockCode").is(stockCode));
    return mongoTemplate.exists(isStockCodeExistsQuery, "Stocks");
  }

  public void updateStockValue(String stockCode, String stockValueIndex, StockValue stockValue) {
    Query upsertStockValueQuery = new Query(Criteria.where("stockCode").is(stockCode));
    mongoTemplate
        .upsert(upsertStockValueQuery, new Update().push("stockValue", stockValue),
            Stock.class);
  }
}