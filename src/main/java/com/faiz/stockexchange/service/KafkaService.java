package com.faiz.stockexchange.service;

import com.faiz.stockexchange.domain.Stock;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

  @Autowired
  StockService stockService;

  @Autowired
  IndexService indexService;

  public void addStock(Message message) {
    List<Stock> stocks = null;
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    try {
      stocks = objectMapper
          .readValue(message.getPayload().toString(), new TypeReference<List<Stock>>() {
          });
      saveStocks(stocks);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("problem with object deserialization ");
    }
  }

  public void saveStocks(List<Stock> stocks) {
    for (Stock stock : stocks) {
      if (stockService.isStockNameExists(stock.getStockName())) {
        stockService.updateStock(stock);
      } else {
        stockService.createStock(stock);
      }
      indexService.calculateWeight();
    }
  }
}



