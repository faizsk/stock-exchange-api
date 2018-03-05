package com.faiz.stockexchange.service;


import com.faiz.stockexchange.domain.Index;
import com.faiz.stockexchange.domain.IndexStock;
import com.faiz.stockexchange.domain.IndexValues;
import com.faiz.stockexchange.domain.Stock;
import com.faiz.stockexchange.domain.StockValue;
import com.faiz.stockexchange.repositories.IndexRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexService {

  Stock firstStock = null;
  Stock currentStock;
  Set<IndexValues> indexValuesList = new HashSet<>();
  IndexValues indexValues;

  /* public List<Index> indices = new ArrayList<>(
       Arrays.asList(new Index("1001", "Sensex", new HashSet<>(
           Arrays
               .asList(new IndexStock("a1", 0.4f),
                   new IndexStock("b1", 0.3f),
                   new IndexStock("c1", 0.3f))))));*/
  @Autowired
  StockService stockService;

  private IndexRepository indexRepository;

  public IndexService(IndexRepository indexRepository) {
    this.indexRepository = indexRepository;
  }

  public List<Index> getAllIndices() {
    // calculateWeight();
    return indexRepository.findAll();
  }

  public Index getIndexByIndexCode(String indexCode) {
   /* return getAllIndices().stream().filter(n -> n.getIndexCode().equalsIgnoreCase(indexCode))
        .findFirst()
        .orElse(null);*/

    return indexRepository.findOneByIndexCode(indexCode.toLowerCase());
  }

  public void calculateWeight(Index index) {
    List<Stock> currentStock = stockService.getAllStocks();
    IndexValues indexValues;

    if (null != index) {

      Set<IndexValues> indexValuesList = new HashSet<>();
      index.setIndexValues(null);

      if (null != index.getStocks()) {
        for (IndexStock indexStock : index.getStocks()) {
          for (Stock stock : currentStock) {
            if (stock.getStockCode().equalsIgnoreCase(indexStock.getStockCode())) {
              // System.out.println(stock.getStockCode());
              //System.out.println(indexStock.getStockCode());
              if (null == index.getIndexValues()) {
                System.out.println(stock.getStockValue().size());

                for (StockValue stockValue : stock.getStockValue()) {
                  indexValues = new IndexValues();
                  //System.out.print(stockValue.getDateTime());
                  //System.out.println(" " + stockValue.getStockPrice() * indexStock.getWeight());
                  indexValues.setDateTime(stockValue.getDateTime());
                  indexValues
                      .setIndexValue((stockValue.getStockPrice() * indexStock.getWeight()) / index
                          .getStocks().size());
                  indexValuesList.add(indexValues);
                  index.setIndexValues(indexValuesList);
                  // System.out.println("NEXT STOCK VALUE");
                }
              } else {
                for (StockValue stockValue : stock.getStockValue()) {
                  //System.out.print(stockValue.getDateTime());
                  // System.out.println(" " + stockValue.getStockPrice() * indexStock.getWeight());
                  for (IndexValues sIndexValues : index.getIndexValues()) {
                    if (sIndexValues.getDateTime().isEqual(stockValue.getDateTime())) {
                      sIndexValues.setIndexValue(
                          sIndexValues.getIndexValue() + ((stockValue.getStockPrice() * indexStock
                              .getWeight()) / index
                              .getStocks().size()));
                    }
                  }
                }
              }
            }
          }
        }
      }
      indexValuesList.removeAll(indexValuesList);
    }
    indexRepository.save(index);
  }

  public void addIndex(Index index) {
    index.setIndexCode(index.getIndexCode().toLowerCase());
    // getAllIndices().add(index);
    index.setIndexCode(index.getIndexCode().toLowerCase());
    indexRepository.save(index);
  }

  public void deleteIndexByIndexCode(String indexCode) {
    //getAllIndices().removeIf(t -> t.getIndexCode().equalsIgnoreCase(indexCode));
    indexRepository.delete(indexCode);
  }

  public void updateIndex(Index index) {
   /* int pos = getAllIndices().indexOf(index);
    getAllIndices().set(pos, index);*/
    //indexRepository.save(index);
    calculateWeight(index);
    //newCalculateWeight(index);
  }

  private void newCalculateWeight(Index index) {
    System.out.println(index.getStocks().stream().map(IndexStock::getStockCode));

  }

  public Index getIndexByIndexName(String indexName) {
  /*  return getAllIndices().stream().filter(n -> n.getIndexName().equalsIgnoreCase(indexName))
        .findFirst()
        .orElse(null);*/
    return indexRepository.findOneByIndexName(indexName);
  }
}