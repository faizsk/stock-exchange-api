package com.faiz.stockexchange.service;

import com.faiz.stockexchange.domain.Stock;
import com.faiz.stockexchange.repositories.StockRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StockService {

  private StockRepository stockRepository;

  public StockService(StockRepository stockRepository) {
    this.stockRepository = stockRepository;
  }
 /* public List<Stock> stocks = new ArrayList<>(
      Arrays.asList(
          new Stock("a1", "Clairvoyant", new HashSet<>(
              Arrays
                  .asList(
                      new StockValue(LocalDateTime.of(2017, Month.JANUARY, 29, 01, 05, 40), 30f),
                      new StockValue(LocalDateTime.of(2017, Month.JANUARY, 29, 01, 05, 41), 32f),
                      new StockValue(LocalDateTime.of(2017, Month.JANUARY, 29, 01, 05, 42), 35.2f))
          )),
          new Stock("b1", "TCS", new HashSet<>(
              Arrays
                  .asList(
                      new StockValue(LocalDateTime.of(2017, Month.JANUARY, 29, 01, 05, 40), 20f),
                      new StockValue(LocalDateTime.of(2017, Month.JANUARY, 29, 01, 05, 41), 22f),
                      new StockValue(LocalDateTime.of(2017, Month.JANUARY, 29, 01, 05, 42), 23f))
          )),
          new Stock("c1", "Cognizant", new HashSet<>(
              Arrays
                  .asList(
                      new StockValue(LocalDateTime.of(2017, Month.JANUARY, 29, 01, 05, 40), 18f),
                      new StockValue(LocalDateTime.of(2017, Month.JANUARY, 29, 01, 05, 41), 23f),
                      new StockValue(LocalDateTime.of(2017, Month.JANUARY, 29, 01, 05, 42), 15f))
          ))));*/

  public List<Stock> getAllStocks() {
    return stockRepository.findAll();
  }

  public Stock getStockByStockName(String stockName) {
   /* return getAllStocks().stream().filter(t -> t.getStockName().equalsIgnoreCase(stockName))
        .findFirst()
        .orElse(null);*/
    return stockRepository.findOneByStockName(stockName);
  }

  public Stock getStockByStockCode(String stockCode) {
   /* return getAllStocks().stream().filter(n -> n.getStockCode().equalsIgnoreCase(stockCode))
        .findFirst()
        .orElse(null);*/
    return stockRepository.findOneByStockCode(stockCode.toLowerCase());
  }

  public void createStock(Stock stock) {
    stock.setStockCode(stock.getStockCode().toLowerCase());
    //getAllStocks().add(stock);
    stockRepository.save(stock);
  }

  public void deleteStockByStockCode(String stockCode) {
    //getAllStocks().removeIf(t -> t.getStockCode().equalsIgnoreCase(stockCode));
    stockRepository.delete(stockCode);
  }

  public void updateStock(Stock stock) {
    /*int index = getAllStocks().indexOf(stock);
    getAllStocks().set(index, stock);*/
    stockRepository.save(stock);
  }

}
