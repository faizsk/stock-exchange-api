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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class IndexService {

  @Autowired
  StockService stockService;

  @Autowired
  IndexService indexService;

  @Autowired
  MongoTemplate mongoTemplate;

  private IndexRepository indexRepository;

  public IndexService(IndexRepository indexRepository) {
    this.indexRepository = indexRepository;
  }

  public List<Index> getAllIndices() {
    return indexRepository.findAll();
  }

  public Index getIndexByIndexCode(String indexCode) {
    return indexRepository.findOneByIndexCode(indexCode.toLowerCase());
  }

  public void calculateWeight() {
    List<Stock> currentStock = stockService.getAllStocks();
    IndexValues indexValues;
    for (Index index : indexService.getAllIndices()) {
      if (null != index) {

        Set<IndexValues> indexValuesList = new HashSet<>();
        index.setIndexValues(null);

        if (null != index.getStocks()) {
          for (IndexStock indexStock : index.getStocks()) {
            for (Stock stock : currentStock) {
              if (stock.getStockCode().equalsIgnoreCase(indexStock.getStockCode())) {
                if (null == index.getIndexValues()) {
                  for (StockValue stockValue : stock.getStockValue()) {
                    indexValues = new IndexValues();
                    indexValues.setDateTime(stockValue.getDateTime());
                    indexValues
                        .setIndexValue((stockValue.getStockPrice() * indexStock.getWeight()) / index
                            .getStocks().size());
                    indexValuesList.add(indexValues);
                    index.setIndexValues(indexValuesList);
                  }
                } else {
                  for (StockValue stockValue : stock.getStockValue()) {
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
  }

  public void addIndex(Index index) {
    index.setIndexCode(index.getIndexCode().toLowerCase());
    index.setIndexCode(index.getIndexCode().toLowerCase());
    indexRepository.save(index);
  }

  public void deleteIndexByIndexCode(String indexCode) {
    Query deleteIndexQuery = new Query(Criteria.where("indexCode").is(indexCode));
    mongoTemplate.remove(deleteIndexQuery, "Indices");
  }

  public void updateIndex(Index index) {
    indexRepository.save(index);
    updateAllIndex();
  }

  public void updateAllIndex() {
    calculateWeight();
  }

  public Index getIndexByIndexName(String indexName) {
    return indexRepository.findOneByIndexName(indexName);
  }

  public boolean isIndexNameExists(String indexName) {
    Query isIndexNameExistsQuery = new Query(Criteria.where("indexName").is(indexName));
    return mongoTemplate.exists(isIndexNameExistsQuery, "Indices");
  }

  public boolean isIndexCodeExists(String indexCode) {
    Query isStockCodeExistsQuery = new Query(Criteria.where("indexCode").is(indexCode));
    return mongoTemplate.exists(isStockCodeExistsQuery, "Indices");
  }

}